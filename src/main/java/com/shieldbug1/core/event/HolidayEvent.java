package com.shieldbug1.core.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import com.shieldbug1.lib.util.Holiday;
/**
 * Fired once if there is a holiday that is currently active during post-init.
 */
public class HolidayEvent extends Event
{
	public final Holiday holiday;
	
	public HolidayEvent(Holiday holiday)
	{
		this.holiday = holiday;
	}

}
