package com.shieldbug1.core.api;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement this in a block class, to allow for it to only be broken conditionally.
 */
public interface IConditionalBreak
{
	/**
	 * Check whether or not the block should break here.
	 * @param x - xPos of the block
	 * @param y - yPos of the block
	 * @param z - zPos of the block
	 * @param player - the player attempting to break the block.
	 * @return true if the block should be allowed to break.
	 */
	public abstract boolean shouldBreak(int x, int y, int z, EntityPlayer player);
}
