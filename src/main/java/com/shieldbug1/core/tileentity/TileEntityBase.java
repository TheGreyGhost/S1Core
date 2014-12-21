package com.shieldbug1.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import com.google.common.base.Strings;
import com.shieldbug1.core.network.DefaultPackets;

public abstract class TileEntityBase extends TileEntity
{
	/** The direction this TileEntity is facing. */
	private EnumFacing orientation;
	/** The custom-name this TileEntity has. */
	private String customName;
	
	protected TileEntityBase()
	{
		this.orientation = EnumFacing.SOUTH; //default direction
		this.customName = "";
	}
	
	@Override
	public final void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setByte("orientation", (byte)this.orientation.ordinal());
		compound.setString("customName", this.customName);
		this.writeTileEntityData(compound);
	}

	@Override
	public final void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.orientation = EnumFacing.values()[compound.getByte("orientation")];
		this.customName = compound.getString("customName");
		this.readTileEntityData(compound);
	}
	
	/**
	 * Write all custom data to the NBTTagCompound here.
	 */
	public abstract void writeTileEntityData(NBTTagCompound compound);
	
	/**
	 * Read all custom data from the NBTTagCompound here.
	 */
	public abstract void readTileEntityData(NBTTagCompound compound);
	
	/**
	 * @return the direction this tile entity is facing.
	 */
	public final EnumFacing getOrientation()
	{
		return this.orientation;
	}
	
	/**
	 * @param direction - the direction to make this tile entity face.
	 */
	public final void setOrientation(EnumFacing direction)
	{
		this.orientation = direction != null ? direction : EnumFacing.SOUTH;
		this.markDirty();
	}
	
	/**
	 * @return whether or not this TileEntity has a custom name or not.
	 */
	public final boolean hasCustomName()
	{
		return !Strings.isNullOrEmpty(this.customName);
	}
	
	/**
	 * @return the custom name of this TileEntity.
	 */
	public final String getCustomName()
	{
		return this.customName != null ? this.customName : "";
	}
	
	/**
	 * Sets this TileEntity's custom name to the given String.
	 */
	public final void setCustomName(String name)
	{
		this.customName = name != null ? name : "";
		this.markDirty();
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		return DefaultPackets.getTileEntityPacket(this);
	}
	
	@Override
	public final void markDirty()
	{
		super.markDirty();
		this.onMarkedDirty();
	}
	
	/**
	 * Called when the {@link TileEntity#markDirty()} is called on this TileEntity.
	 */
	public void onMarkedDirty(){}
	
}
