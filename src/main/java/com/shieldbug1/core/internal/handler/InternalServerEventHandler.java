package com.shieldbug1.core.internal.handler;

import com.shieldbug1.lib.util.ServerUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public final class InternalServerEventHandler
{
	public static final InternalServerEventHandler INSTANCE = new InternalServerEventHandler();
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event)
	{
		if(event.phase.equals(TickEvent.Phase.START))
		{
			ServerUtils.preTick();
		}
		else if(event.phase.equals(TickEvent.Phase.END))
		{
			ServerUtils.postTick();
		}
	}
}
