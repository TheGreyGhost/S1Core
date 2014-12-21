package com.shieldbug1.core.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

/**
 * Implement this in a Block class, to allow for it to only be broken conditionally.
 */
public interface IConditionalBreak
{
	/**
	 * Check whether or not the block should break here.
	 * @param pos - position of the block.
	 * @param player - the player attempting to break the block.
	 * @return true if the block should be allowed to break.
	 */
	public abstract boolean shouldBreak(BlockPos pos, EntityPlayer player);
}
