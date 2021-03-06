package com.shieldbug1.core.internal.launch;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;

import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.internal.Hacks;
import com.shieldbug1.core.util.ResourceHelper;

public class InternalEnvironment implements IFMLCallHook
{
	private static boolean isDevEnv;
	
	@Override
	public Void call() throws Exception
	{
		if(isDevEnv)
		{
			Hacks.setErrToOriginal();
			Hacks.setOutToOriginal();
		}
		try
		{
			Class.forName("com.shieldbug1.lib.java.Java");
		}
		catch(Exception e) //No S1Lib downloaded!
		{
			
		}
		S1Core.discoverLoadingModules();
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		ResourceHelper.init();
		isDevEnv = !(boolean) data.get("runtimeDeobfuscationEnabled");
	}
	
	public static boolean isDevEnv()
	{
		return isDevEnv;
	}
}
