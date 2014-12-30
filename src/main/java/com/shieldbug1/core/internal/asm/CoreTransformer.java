package com.shieldbug1.core.internal.asm;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public final class CoreTransformer implements IClassTransformer, Opcodes
{
	@Override
	public byte[] transform(String className, String transformedName, byte[] bytes)
	{
		return bytes;
	}
	
	private ClassNode createClassNode(byte[] bytes)
	{
		ClassReader reader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode(ASM5);
		reader.accept(classNode, EXPAND_FRAMES);
		return classNode;
	}
}
