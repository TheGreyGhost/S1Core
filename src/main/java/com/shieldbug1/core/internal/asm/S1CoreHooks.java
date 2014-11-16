package com.shieldbug1.core.internal.asm;

import net.minecraft.tileentity.TileEntity;

import com.shieldbug1.core.api.IPlaceNotified;

public final class S1CoreHooks
{
	private S1CoreHooks(){}
	
	/**
	 * Called for the given tile entity whenever it is placed.
	 * This is done through patching the {@code setTileEntity} method in the {@code World} class.
	 */
	public static void onTileEntityCreated(TileEntity tileEntity)
	{
		if(tileEntity instanceof IPlaceNotified && !tileEntity.getWorldObj().isRemote)
		{
			((IPlaceNotified)tileEntity).onPlaced();
		}
	}
}
