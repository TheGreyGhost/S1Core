package com.shieldbug1.core.api;

import com.shieldbug1.core.event.EventDispatcher;
/**
 * A convenience class for other mod authors. Any methods contained by this class are
 * kept under the API contract specified in this package's package-info. It is preferred to use these methods
 * opposed to the 'non-convenient' as those methods are not guaranteed to stay the same.
 */
public final class S1CoreAPI
{
	private S1CoreAPI(){}
	
	private static final S1CoreAPI INSTANCE = new S1CoreAPI();
	
	public static S1CoreAPI instance()
	{
		return INSTANCE;
	}
	
	/**
	 * Use this method to register any object to the {@code EventBus} belonging to S1Core.
	 * This works identically to the Forge and FML event busses. This method should be preferred to
	 * {@code EventDispatcher.instance().bus().register(Object)} as that class may change completely in the future,
	 * whilst this method won't.
	 * @param listener - the listener object to register to the EventBus.
	 */
	public void registerEventListener(Object listener)
	{
		EventDispatcher.instance().bus().register(listener);
	}
	
}
