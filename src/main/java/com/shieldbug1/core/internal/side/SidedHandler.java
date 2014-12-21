package com.shieldbug1.core.internal.side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface SidedHandler
{
	public EntityPlayer getClientPlayer();
	public World getClientWorld();
}
