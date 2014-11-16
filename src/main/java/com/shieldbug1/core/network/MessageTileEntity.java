package com.shieldbug1.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
/**
 * Default IMessage class to synchronise most tile entities.
 *
 * Should only be used internally.
 */
public class MessageTileEntity implements IMessage
{
	private NBTTagCompound compound;
	/**
	 * @deprecated Used internally to construct the class.
	 */
	@Deprecated
	public MessageTileEntity(){}
	
	public MessageTileEntity(TileEntity tileEntity)
	{
		this.compound = new NBTTagCompound();
		tileEntity.writeToNBT(this.compound);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, this.compound);
	}
	
	/**
	 * Should only be used internally.
	 */
	public static class Handler implements IMessageHandler<MessageTileEntity, IMessage>
	{

		@Override
		public IMessage onMessage(MessageTileEntity message, MessageContext ctx)
		{
			TileEntity tileEntity = new TileEntity();
			tileEntity.readFromNBT(message.compound);
			FMLClientHandler.instance().getWorldClient().getTileEntity(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord).readFromNBT(message.compound);
			return null;
		}
		
	}

}
