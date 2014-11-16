package com.shieldbug1.core.internal.handler;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;

import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.api.IConditionalBreak;
import com.shieldbug1.core.api.IStandAware;
import com.shieldbug1.lib.math.MathUtils;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/**
 * This is the class that 'subscribes' to events for any internal behaviour that is required by the pseudo-library.
 */
public final class InternalEventHandler
{
	public static final InternalEventHandler INSTANCE = new InternalEventHandler();

	@SubscribeEvent(priority = EventPriority.LOWEST) //We want to go last, to make sure the event isn't uncancelled.
	public void onBlockBroken(BlockEvent.BreakEvent event)
	{
		if(event.block instanceof IConditionalBreak)
		{
			event.setCanceled(!((IConditionalBreak)event.block).shouldBreak(event.x, event.y, event.z, event.getPlayer()));
		}
	}
	
	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent event)
	{
		if(event.entityLiving.onGround)
		{
			int x = MathUtils.floor(event.entityLiving.posX);
			int y = MathUtils.floor(event.entityLiving.posY);
			int z = MathUtils.floor(event.entityLiving.posZ);
			
			Block block = event.entityLiving.worldObj.getBlock(x, y - 1, z);
			if(block instanceof IStandAware)
			{
				((IStandAware)block).onStanding(event.entityLiving, x, y - 1, z);
			}
		}
	}
	
	public void initHandler()
	{
		S1Core.getProxy().initInternalEventHandler();
	}
}
