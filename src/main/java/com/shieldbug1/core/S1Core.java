package com.shieldbug1.core;

import java.util.List;

import com.google.common.collect.Lists;
import com.shieldbug1.core.api.LoadingModule;
import com.shieldbug1.core.internal.proxy.InternalProxy;
import com.shieldbug1.core.network.DefaultPackets;
import com.shieldbug1.lib.mods.Mods;
import com.shieldbug1.lib.mods.S1Mods;
import com.shieldbug1.lib.mods.api.ModBase;
import com.shieldbug1.lib.mods.update.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;

@Mod(modid = S1Core.MOD_ID, name = S1Core.MOD_NAME, version = S1Core.VERSION,
dependencies = "required-after:Forge@[10.13.2.1236,);"
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
	public static final List<LoadingModule> loadingModules = Lists.newLinkedList();
	
	
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
		return new UpdaterBase("https://raw.githubusercontent.com/shieldbug1/S1Core/master/version.txt", "https://www.google.com", MOD_ID.toUpperCase(), MOD_ID); //TODO forum link.
	}
	
}
