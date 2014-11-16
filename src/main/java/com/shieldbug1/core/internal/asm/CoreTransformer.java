package com.shieldbug1.core.internal.asm;

import net.minecraft.launchwrapper.IClassTransformer;

public final class CoreTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String className, String transformedName, byte[] bytes)
	{
		return bytes;
	}

}
