package com.shieldbug1.core.api;

import static com.shieldbug1.lib.util.CoreFunctions.checkNotNull;

import java.util.Map;

import net.minecraft.block.Block;

import com.google.common.annotations.Beta;
import com.google.common.collect.Maps;
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
	private final Map<Block, CustomBlockRenderer> renderingHandlers = Maps.newHashMap();
	
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
	
	/**
	 * 
	 * @param block
	 * @param isbrh
	 */
	@Beta
	public void registerBlockRenderingHandler(Block block, CustomBlockRenderer isbrh)//TODO Javadoc
	{
		if(!this.renderingHandlers.containsKey(block))
		{
			this.renderingHandlers.put(block, isbrh);
		}
	}
	
	@Beta
	public CustomBlockRenderer getRenderingHandlerForBlock(Block block)//TODO Javadoc
	{
		return checkNotNull(this.renderingHandlers.get(block), String.format("Block %s doesn't have a ISBRH associated with it!", block.getUnlocalizedName()));
	}
}
