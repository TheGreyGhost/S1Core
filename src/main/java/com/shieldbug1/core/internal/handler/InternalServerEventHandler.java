package com.shieldbug1.core.internal.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.shieldbug1.lib.util.ServerUtils;

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
