package com.shieldbug1.core.internal.asm;

import static com.shieldbug1.core.internal.asm.S1CoreHooks.HOOKS_CLASS;
import static com.shieldbug1.lib.asm.ASMHelper.*;
import static com.shieldbug1.lib.asm.ASMTypes.BLOCK_POS_TYPE;
import static com.shieldbug1.lib.asm.ASMTypes.IBLOCK_STATE_TYPE;
import static com.shieldbug1.lib.math.MathUtils.max;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Type.BOOLEAN_TYPE;
import static org.objectweb.asm.Type.getMethodDescriptor;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class RenderTransformer implements IClassTransformer, Opcodes
{
	private static final Type IBLOCK_ACCESS_TYPE = Type.getType("Lnet/minecraft/world/IBlockAccess;");
	private static final Type WORLD_RENDERER_TYPE = Type.getType("Lnet/minecraft/client/renderer/WorldRenderer;");
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		switch(transformedName)
		{
		case "net.minecraft.client.renderer.BlockRendererDispatcher":
			return transformBlockRenderDispatcher(basicClass);
		default:
			return basicClass;
		}
	}

	private byte[] transformBlockRenderDispatcher(byte[] basicClass)
	{
		ClassNode classNode = createClassNode(basicClass);
		patchRenderBlock(classNode);
		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private void patchRenderBlock(ClassNode classNode)
	{
		/*
		 * public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess access, WorldRenderer renderer)
		 * {
		 *    ...
		 *    case 10: return S1CoreHooks.renderCustomBlock(state, pos, access, renderer); <<< Patch
		 * }
		 */
		MethodNode method = findMethod(classNode, "renderBlock", "func_175018_a",
				BOOLEAN_TYPE, IBLOCK_STATE_TYPE, BLOCK_POS_TYPE, IBLOCK_ACCESS_TYPE, WORLD_RENDERER_TYPE);
		
		TableSwitchInsnNode tableSwitch = (TableSwitchInsnNode)findFirst(TABLESWITCH, method.instructions);
		tableSwitch.max = max(tableSwitch.max, 10); //Incase someone else raises it to above 10.
		LabelNode label = new LabelNode();
		for(int i = tableSwitch.labels.size(); i < 9; i++)
		{
			tableSwitch.labels.add(tableSwitch.dflt);
		}
		tableSwitch.labels.add(label);
		
		InsnList patch = new InsnList();
		patch.add(label);
		patch.add(new VarInsnNode(ALOAD, 1));
		patch.add(new VarInsnNode(ALOAD, 2));
		patch.add(new VarInsnNode(ALOAD, 3));
		patch.add(new VarInsnNode(ALOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, HOOKS_CLASS, "renderCustomBlock",
				getMethodDescriptor(BOOLEAN_TYPE, IBLOCK_STATE_TYPE, BLOCK_POS_TYPE, IBLOCK_ACCESS_TYPE, WORLD_RENDERER_TYPE), false));
		patch.add(new InsnNode(IRETURN));
		method.instructions.insert(tableSwitch, patch);
	}

}
