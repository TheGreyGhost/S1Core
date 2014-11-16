package com.shieldbug1.core.schematic;

/**
 * These haven't been benchmarked yet. Ignore all timing information.
 */
public enum SchematicSize
{
	/**
	 * Value of 8.
	 * Average of 51969.46 microseconds to create (0.05 seconds).
	 * Average of 6 to 64 microseconds to create from cached. (64 average at first, then time goes down as compiler guesses better).
	 */
	TINY,
	/***
	 * Value of 16.
	 * Average of 213202.54 microseconds to create (0.21 seconds).
	 * Average of 83 to 609 microseconds to create from cached. (609 average at first, then time goes down as compiler guesses better).
	 */
	SMALL,
	/**
	 * Value of 32.
	 * Average of 1958932.56 microseconds to create (1.96 seconds).
	 * Average of 31 to 5787 microseconds to create from cached. (5787 average at first, then time goes down as compiler guesses better).
	 */
	MEDIUM,
	/**
	 * Value of 64.
	 * Average of 15801024.3 microseconds to create (15.8).
	 * Average of 222 to 11469 microseconds to create from cached. (11469 average at first, then time goes down as compiler guessed better).
	 */
	LARGE,
	/**
	 * Value of 128.
	 * @deprecated json files are huge and laggy and bad so don't use this unless you absolutely have to.
	 * Time hasn't been benchmarked because it takes too long to create.
	 */
	HUGE;

	/**
	 * @return the actual value for the schematic.
	 */
	public int value()
	{
		return ( 1 << (this.ordinal() + 3));
	}
}