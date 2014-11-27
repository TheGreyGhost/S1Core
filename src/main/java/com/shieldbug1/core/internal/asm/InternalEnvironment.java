package com.shieldbug1.core.internal.asm;

import static org.apache.logging.log4j.Level.*;

import java.io.File;
import java.util.Map;

import com.google.common.base.Predicate;
import com.shieldbug1.core.S1ClassDiscoverer;
import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.api.LoadingModule;
import com.shieldbug1.core.internal.Hacks;
import com.shieldbug1.core.util.ResourceHelper;
import com.shieldbug1.lib.java.Java;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.IFMLCallHook;

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
		return null;
	}

	public static void discoverLoadingModules()
	{
		S1Core.loadingModules.clear();
		FMLLog.log(S1Core.MOD_ID, INFO, "Searching for LoadingModules.");
		S1ClassDiscoverer classDiscoverer = new S1ClassDiscoverer(
				new Predicate<String>()
				{

					@Override
					public boolean apply(String input)
					{
						return input.endsWith("Module.class");
					}
					
				},//XXX JAVA 8
				LoadingModule.class
				);
		classDiscoverer.find();
		FMLLog.log(S1Core.MOD_ID, INFO, "Finished searching for LoadingModules.");
		for(Class<?> clazz : classDiscoverer.found)
		{
			try
			{
				LoadingModule module = (LoadingModule) clazz.newInstance();
				module.setUp();
				S1Core.loadingModules.add(module);
				FMLLog.log(S1Core.MOD_ID, TRACE, "Loaded %s successfully.", clazz.getSimpleName());
			}
			catch(Exception e)
			{
				FMLLog.log(S1Core.MOD_ID, ERROR, e, "Failed to load %s.", clazz.getSimpleName());
				Java.<RuntimeException>throwUnchecked(e);
			}
		}
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		ResourceHelper.init((File)data.get("mcLocation"));
		isDevEnv = !(boolean) data.get("runtimeDeobfuscationEnabled");
	}
	
	public static boolean isDevEnv()
	{
		return isDevEnv;
	}
}
