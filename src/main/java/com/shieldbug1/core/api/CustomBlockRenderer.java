package com.shieldbug1.core.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.google.common.annotations.Beta;

@Beta
public interface CustomBlockRenderer //TODO Javadoc
{
	public abstract boolean render(IBlockState state, BlockPos pos, IBlockAccess access, WorldRenderer renderer);
}
