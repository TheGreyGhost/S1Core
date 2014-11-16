package com.shieldbug1.core.internal.asm;

import java.io.File;
import java.util.Map;

import com.google.common.base.Predicate;
import com.shieldbug1.core.S1ClassDiscoverer;
import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.api.LoadingModule;
import com.shieldbug1.core.internal.Hacks;
import com.shieldbug1.core.util.ResourceHelper;
import com.shieldbug1.lib.java.Java;

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
		this.discoverLoadingModules();
		return null;
	}

	private void discoverLoadingModules()
	{
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
		
		for(Class<?> clazz : classDiscoverer.found)
		{
			try
			{
				LoadingModule module = (LoadingModule) clazz.newInstance();
				module.setUp();
				S1Core.loadingModules.add(module);
			}
			catch(Exception e)
			{
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
