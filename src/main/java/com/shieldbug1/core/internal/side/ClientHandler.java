package com.shieldbug1.core.internal.side;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientHandler implements SidedHandler
{
	public Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return mc.thePlayer;
	}

	@Override
	public World getClientWorld()
	{
		return mc.theWorld;
	}
}
