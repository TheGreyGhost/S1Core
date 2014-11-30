package com.shieldbug1.core.internal.asm;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.Opcodes.ASM5;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import com.shieldbug1.core.util.SidedClass;

public final class CoreTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String className, String transformedName, byte[] bytes)
	{
		checkForSideViolation(bytes);
		return bytes;
	}

	private void checkForSideViolation(byte[] bytes)
	{
		ClassNode classNode = createClassNode(bytes);
		if(classNode.visibleAnnotations != null)
		{
			for(AnnotationNode annotation : classNode.visibleAnnotations)
			{
				if(annotation.desc.equals(SidedClass.ASM_DESC))
				{
					Side expectedSide = Side.valueOf(((String[]) annotation.values.get(1))[1]);
					if(FMLCommonHandler.instance().getSide() != expectedSide)
					{
						throw new IllegalSideException(classNode.name);
					}
				}
			}
		}
	}
	
	private ClassNode createClassNode(byte[] bytes)
	{
		ClassReader reader = new ClassReader(bytes);
		ClassNode classNode = new ClassNode(ASM5);
		reader.accept(classNode, EXPAND_FRAMES);
		return classNode;
	}
	
	public static class IllegalSideException extends RuntimeException
	{
		public IllegalSideException(String className)
		{
			super(String.format("Class %s attempted to be loaded on invalid side %s", className, FMLCommonHandler.instance().getSide()));
			FMLLog.bigWarning("Class %s attempted to be loaded on invalid side %s", className, FMLCommonHandler.instance().getSide());
		}
	}

}
