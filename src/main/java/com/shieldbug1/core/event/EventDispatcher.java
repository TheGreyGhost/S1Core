package com.shieldbug1.core.event;

import net.minecraftforge.fml.common.eventhandler.EventBus;

import com.shieldbug1.lib.util.Holiday;

public final class EventDispatcher
{
	/** Singleton Instance */
	private static final EventDispatcher INSTANCE = new EventDispatcher();
	
	/** Event Bus.*/
	private final EventBus EVENT_BUS = new EventBus();
	
	private EventDispatcher(){}
	
	/**
	 * @return the singleton instance.
	 */
	public static EventDispatcher instance()
	{
		return INSTANCE;
	}
	
	/**
	 * @return the EventBus belonging to the dispatcher.
	 */
	public EventBus bus()
	{
		return EVENT_BUS;
	}
	
	public void fireHolidayEvent(Holiday holiday)
	{
		EVENT_BUS.post(new HolidayEvent(holiday));
	}
	
}
