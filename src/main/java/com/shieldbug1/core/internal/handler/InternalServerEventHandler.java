package com.shieldbug1.core.internal.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class InternalServerEventHandler
{
	public static final InternalServerEventHandler INSTANCE = new InternalServerEventHandler();
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		
	}
}
