package com.shieldbug1.core.internal.proxy;

import static com.shieldbug1.core.internal.handler.InternalClientEventHandler.INSTANCE;
import net.minecraftforge.common.MinecraftForge;

import com.shieldbug1.core.event.EventDispatcher;

import cpw.mods.fml.common.FMLCommonHandler;

public final class InternalClientProxy extends InternalProxyBase
{
	@Override
	public void initInternalEventHandler()
	{
		super.initInternalEventHandler();
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		MinecraftForge.ORE_GEN_BUS.register(INSTANCE);
		MinecraftForge.TERRAIN_GEN_BUS.register(INSTANCE);
		FMLCommonHandler.instance().bus().register(INSTANCE);
		EventDispatcher.instance().bus().register(INSTANCE);
	}
}
