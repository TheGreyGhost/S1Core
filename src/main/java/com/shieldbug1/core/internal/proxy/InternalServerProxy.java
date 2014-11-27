package com.shieldbug1.core.internal.proxy;

import static com.shieldbug1.core.internal.handler.InternalServerEventHandler.INSTANCE;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.shieldbug1.core.event.EventDispatcher;

public final class InternalServerProxy extends InternalProxyBase
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
