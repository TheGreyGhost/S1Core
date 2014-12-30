package com.shieldbug1.core.internal.asm;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.shieldbug1.core.api.IPlaceNotified;
import com.shieldbug1.core.api.S1CoreAPI;
import com.shieldbug1.lib.util.Sides;

public final class S1CoreHooks
{
	public static final String HOOKS_CLASS = S1CoreHooks.class.getName().replace('.', '/');

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
	}
	
	//TODO JAVADOC
	public static boolean renderCustomBlock(IBlockState state, BlockPos pos, IBlockAccess access, WorldRenderer renderer)
	{
		return S1CoreAPI.instance().getRenderingHandlerForBlock(state.getBlock()).render(state, pos, access, renderer);
	}//TODO Fix rendering.
}
