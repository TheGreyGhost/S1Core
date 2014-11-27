package com.shieldbug1.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
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
			class FakeTileEntity extends TileEntity{}
			TileEntity tileEntity = new FakeTileEntity();
			tileEntity.readFromNBT(message.compound);
			FMLClientHandler.instance().getWorldClient().getTileEntity(tileEntity.getPos()).readFromNBT(message.compound);
			return null;
		}
	}
	


}
