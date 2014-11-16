package com.shieldbug1.core.internal.asm;

import static com.shieldbug1.lib.asm.ASMHelper.*;
import static com.shieldbug1.lib.asm.ASMTypes.TILE_ENTITY_TYPE;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Type.*;
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
		
		// World.setTileEntity(int,int,int,TileEntity) VOID
		MethodNode method = findMethod(classNode, "setTileEntity", "func_147455_a", VOID_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, TILE_ENTITY_TYPE);
		
		AbstractInsnNode instruction = findLast(RETURN, method.instructions);
		InsnList patch = new InsnList();
		
		patch.add(new VarInsnNode(ALOAD, 4));
		patch.add(new MethodInsnNode(INVOKESTATIC, S1CorePlugin.class.getName().replace('.', '/'), "onTileEntityCreated", getMethodDescriptor(VOID_TYPE, TILE_ENTITY_TYPE), false));
		method.instructions.insertBefore(instruction, patch);

		return makeBytes(classNode, COMPUTE_MAXS | COMPUTE_FRAMES);
	}

}
