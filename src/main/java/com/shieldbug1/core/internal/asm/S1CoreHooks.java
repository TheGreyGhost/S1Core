package com.shieldbug1.core.internal.asm;

import java.util.List;

import net.minecraft.tileentity.TileEntity;

import com.shieldbug1.core.api.IPlaceNotified;
import com.shieldbug1.lib.util.Sides;

public final class S1CoreHooks
{
	public static final String NAME = S1CoreHooks.class.getName().replace('.', '/');

	private S1CoreHooks(){}
	
	/**
	 * Called for the given tile entity whenever it is placed.
	 * This is done through patching the {@code setTileEntity} method in the {@code World} class.
	 */
	public static void onTileEntityCreated(TileEntity tileEntity)
	{
		if(tileEntity instanceof IPlaceNotified && Sides.logicalServer(tileEntity.getWorld()))
		{
			((IPlaceNotified)tileEntity).onPlaced();
		}
		List<String> l = null;
	}
}
