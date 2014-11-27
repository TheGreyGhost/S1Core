package com.shieldbug1.core.internal.handler;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.api.IConditionalBreak;
import com.shieldbug1.core.api.IStandAware;
import com.shieldbug1.lib.math.MathUtils;
/**
 * This is the class that 'subscribes' to events for any internal behaviour that is required by the pseudo-library.
 */
public final class InternalEventHandler
{
	public static final InternalEventHandler INSTANCE = new InternalEventHandler();

	@SubscribeEvent(priority = EventPriority.LOWEST) //We want to go last, to make sure the event isn't uncancelled.
	public void onBlockBroken(BlockEvent.BreakEvent event)
	{
		if(event.state.getBlock() instanceof IConditionalBreak)
		{
			event.setCanceled(!((IConditionalBreak)event.state.getBlock()).shouldBreak(event.pos, event.getPlayer()));
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
			BlockPos pos = new BlockPos(x, y-1, z);
			Block block = event.entityLiving.worldObj.getBlockState(pos).getBlock();
			if(block instanceof IStandAware)
			{
				((IStandAware)block).onStanding(event.entityLiving, pos);
			}
		}
	}
	
	public void initHandler()
	{
		S1Core.getProxy().initInternalEventHandler();
	}
}
