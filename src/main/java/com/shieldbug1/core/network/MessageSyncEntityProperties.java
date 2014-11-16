package com.shieldbug1.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import com.shieldbug1.core.entity.extended.ISynchronisedEntityProperties;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;

/**
 * Should only be used internally.
 */
public final class MessageSyncEntityProperties implements IMessage
{
	private String uniqueIdentifier;
	private int entityID;
	private NBTTagCompound compound;
	
	@Deprecated
	public MessageSyncEntityProperties(){}
	
	public MessageSyncEntityProperties(ISynchronisedEntityProperties properties)
	{
		this.uniqueIdentifier = properties.getUniqueIdentifier(); //Gets the property name
		this.entityID = properties.getEntity().getEntityId(); //Gets the entity the properties belong to
		this.compound = new NBTTagCompound(); // initialises the compound
		properties.saveNBTData(this.compound); //writes all properties to the compound. XXX Java 8 writeProperties
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, this.uniqueIdentifier);
		buf.writeInt(this.entityID);
		ByteBufUtils.writeTag(buf, this.compound);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.uniqueIdentifier = ByteBufUtils.readUTF8String(buf);
		this.entityID = buf.readInt();
		this.compound = ByteBufUtils.readTag(buf);
	}
	
	/**
	 * Should only be used internally.
	 */
	public static final class Handler implements IMessageHandler<MessageSyncEntityProperties, IMessage>
	{

		@Override
		public IMessage onMessage(MessageSyncEntityProperties message, MessageContext ctx)
		{
			((ISynchronisedEntityProperties) FMLClientHandler.instance().getWorldClient().getEntityByID(message.entityID).getExtendedProperties(message.uniqueIdentifier)).loadNBTData(message.compound); //XXX Java 8 readProperties
			return null;
		}
		
	}
}