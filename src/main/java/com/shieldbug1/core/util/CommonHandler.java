package com.shieldbug1.core.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.shieldbug1.core.internal.side.SidedHandler;
import com.shieldbug1.lib.java.Java;
import com.shieldbug1.lib.util.Sides;

public class CommonHandler implements SidedHandler
{
	private static final CommonHandler INSTANCE = new CommonHandler();
	
	private final SidedHandler delegate;
	
	private CommonHandler()
	{
		String handlerClass = "com.shieldbug1.core.internal.side." + (Sides.environment().isClient() ? "Client" : "Server") + "Handler";
		try
		{
			this.delegate = (SidedHandler) Class.forName(handlerClass).newInstance();
		}
		catch (Exception impossible)
		{
			throw Java.<RuntimeException>throwUnchecked(impossible);
		}
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return delegate.getClientPlayer();
	}

	@Override
	public World getClientWorld()
	{
		return delegate.getClientWorld();
	}
	
	
}
