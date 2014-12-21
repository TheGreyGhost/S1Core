package com.shieldbug1.core;

import static org.apache.logging.log4j.Level.*;

import java.util.Set;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.shieldbug1.core.api.LoadingModule;
import com.shieldbug1.core.internal.proxy.InternalProxy;
import com.shieldbug1.core.network.DefaultPackets;
import com.shieldbug1.lib.java.Java;
import com.shieldbug1.lib.mods.Mods;
import com.shieldbug1.lib.mods.S1Mods;
import com.shieldbug1.lib.mods.api.ModBase;
import com.shieldbug1.lib.mods.update.*;

@Mod(modid = S1Core.MOD_ID, name = S1Core.MOD_NAME, version = S1Core.VERSION,
dependencies = "required-after:Forge@[11.14.0,);"
		+ "after:NotEnoughItems;"
		+ "after:Waila;"
		+ "after:ee3;"
		+ "after:Thaumcraft;"
		+ "after:TConstruct;"
		+ "after:AWWayofTime")
public final class S1Core implements ModBase
{
	public static final String MOD_NAME = "S1Core";
	public static final String MOD_ID = "S1CORE";
	public static final String VERSION = "@VERSION@";
	public static final int serialID = 0;
	private static Set<LoadingModule> loadingModules = Sets.newLinkedHashSet();
	
	@SidedProxy(clientSide = "com.shieldbug1.core.internal.proxy.InternalClientProxy", serverSide = "com.shieldbug1.core.internal.proxy.InternalServerProxy")
	public static InternalProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		for(LoadingModule module : loadingModules)
		{
			module.preInit(event);
		}//XXX forEach
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Mods.values();
		
		for(LoadingModule module : loadingModules)
		{
			module.init(event);
		}//XXX forEach
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		for(S1Mods mod : S1Mods.values())
		{
			if(mod.isLoaded() && mod.instance().getUpdater() != null)
			{
				new UpdateThread(mod.instance()).start();
			}
		}
		
		DefaultPackets.init();
		
//XXX JAVA 8		Holiday.checkHolidays();
		
		for(LoadingModule module : loadingModules)
		{
			module.postInit(event);
		}
	}
	
	@Override
	public int getSerialID()
	{
		return serialID;
	}

	public static InternalProxy getProxy() //TODO temporary solution - should be in InternalProxy instead, but needs the use of asm 5 api by forge.
	{
		return proxy;
	}

	@Override
	public Updater getUpdater()
	{
		return new UpdaterBase("https://raw.githubusercontent.com/shieldbug1/S1Core/master/version.txt", "https://www.google.com", MOD_ID, MOD_ID); //TODO forum link.
	}

	public static void discoverLoadingModules()
	{
		FMLLog.log(MOD_ID, INFO, "Searching for LoadingModules.");
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
		FMLLog.log(MOD_ID, INFO, "Finished searching for LoadingModules.");
		
		for(Class<?> clazz : classDiscoverer.found)
		{
			try
			{
				LoadingModule module = (LoadingModule) clazz.newInstance();
				module.setUp();
				loadingModules.add(module);
				FMLLog.log(MOD_ID, TRACE, "Loaded %s successfully.", clazz.getSimpleName());
			}
			catch(Exception e)
			{
				FMLLog.log(MOD_ID, ERROR, e, "Failed to load %s.", clazz.getSimpleName());
				Java.<RuntimeException>throwUnchecked(e);
			}
		}
		
		loadingModules = ImmutableSet.copyOf(loadingModules);
	}
	
	public static ImmutableSet<LoadingModule> loadingModules()
	{
		return (ImmutableSet<LoadingModule>) loadingModules; //Cast is safe - this method should never be called before LoadingModules are discovered. 
	}
}
