package com.shieldbug1.core.internal.asm;

import static com.shieldbug1.lib.asm.ASMHelper.*;
import static com.shieldbug1.lib.asm.ASMTypes.BLOCK_POS_TYPE;
import static com.shieldbug1.lib.asm.ASMTypes.TILE_ENTITY_TYPE;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.VOID_TYPE;
import static org.objectweb.asm.Type.getMethodDescriptor;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.tree.*;

public class WorldTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		switch(name)
		{
		case "net.minecraft.world.World":
			return transformWorld(bytes);
		default:
				return bytes;
		}
	}
	
	private byte[] transformWorld(byte[] bytes)
	{
		ClassNode classNode = createClassNode(bytes);
		patchSetTileEntity(classNode);
		

		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

	private void patchSetTileEntity(ClassNode classNode)// World.setTileEntity(BlockPos,TileEntity) VOID
	{
		/*
		 * public void setTileEntity(BlockPos pos, TileEntity tileEntity)
		 * {
		 *    ...
		 *    S1CoreHooks.onTileEntityCreated(tileEntity); <<< Patch
		 *    return; <<< last invisible return at end of method
		 * }
		 */
		MethodNode method = findMethod(classNode, "setTileEntity", "func_147455_a", VOID_TYPE, BLOCK_POS_TYPE, TILE_ENTITY_TYPE);
		InsnList patch = new InsnList();
		patch.add(new VarInsnNode(ALOAD, 2));
		patch.add(new MethodInsnNode(INVOKESTATIC, S1CoreHooks.NAME, "onTileEntityCreated", getMethodDescriptor(VOID_TYPE, TILE_ENTITY_TYPE), false));
		method.instructions.insertBefore(findLast(RETURN, method.instructions), patch);
	}

}
