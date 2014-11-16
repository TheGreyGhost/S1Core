package com.shieldbug1.core.network;

import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.entity.extended.ISynchronisedEntityProperties;
import com.shieldbug1.lib.network.NetworkHelper;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;

public final class DefaultPackets
{
	private DefaultPackets(){}
	
	private static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(S1Core.MOD_ID);
	private static short discriminator = 0;
	
	public static void init()
	{
		registerMessage(MessageTileEntity.class, MessageTileEntity.Handler.class, Side.CLIENT);
		registerMessage(MessageSyncEntityProperties.class, MessageSyncEntityProperties.Handler.class, Side.CLIENT);
	}
	
	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> requestMessageType, Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Side side)
	{
		WRAPPER.registerMessage(messageHandler, requestMessageType, discriminator++, side);
	}
	
	/**
	 * A method to get the a packet representing an instance of {@link MessageTileEntity}.
	 * @param tileEntity - the TileEntity to get the packet for.
	 * @return the packet for the TileEntity.
	 */
	public static Packet getTileEntityPacket(TileEntity tileEntity)
	{
		return WRAPPER.getPacketFrom(new MessageTileEntity(tileEntity));
	}
	
	/**
	 * A method to manually sync a TileEntity if the default TileEntity synchronisation isn't enough.
	 * @param tileEntity - the TileEntity to sync.
	 */
	public static void syncTileEntity(TileEntity tileEntity)
	{
		NetworkHelper.sendToAllTracking(WRAPPER, tileEntity, new MessageTileEntity(tileEntity));
	}
	
	/**
	 * Synchronises the values from server to client as specified by {@link ISynchronisedEntityProperties}
	 */
	public static void syncEntityProperties(ISynchronisedEntityProperties properties)
	{
		NetworkHelper.sendToAllTracking(WRAPPER, properties.getEntity(), new MessageSyncEntityProperties(properties));
	}
}
