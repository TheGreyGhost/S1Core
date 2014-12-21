package com.shieldbug1.core.internal.side;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ServerHandler implements SidedHandler
{
	@Override
	public EntityPlayer getClientPlayer()
	{
		return null;
	}

	@Override
	public World getClientWorld()
	{
		return null;
	}
}
