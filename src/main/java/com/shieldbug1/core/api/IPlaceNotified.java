package com.shieldbug1.core.api;

/**
 * An interface to allow tile-entities to be notified of when they are created
 */
public interface IPlaceNotified
{
	/**
	 * Called whenever a tile entity is placed in the world, only on the server.
	 */
	public abstract void onPlaced();
}
