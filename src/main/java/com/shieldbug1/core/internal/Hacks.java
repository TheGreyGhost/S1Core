package com.shieldbug1.core.internal;

import java.io.FilterOutputStream;
import java.io.PrintStream;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class Hacks
{
	private Hacks(){}
	
	public static void setOutToOriginal()
	{
		System.setOut((PrintStream) ReflectionHelper.getPrivateValue(FilterOutputStream.class, System.out, "out"));
	}
	
	public static void setErrToOriginal()
	{
		System.setErr((PrintStream) ReflectionHelper.getPrivateValue(FilterOutputStream.class, System.err, "out"));
	}
}
