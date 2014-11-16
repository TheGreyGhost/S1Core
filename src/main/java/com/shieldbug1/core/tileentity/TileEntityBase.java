package com.shieldbug1.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.shieldbug1.core.network.DefaultPackets;

public abstract class TileEntityBase extends TileEntity
{
	/** The direction this TileEntity is facing. */
	protected ForgeDirection orientation;
	/** The custom-name this TileEntity has. */
	protected String customName;
	/** The 'state' this TileEntity is in. (Like block metadata, except this isn't limited to 0 to 15). */
	protected byte state;
	
	protected TileEntityBase()
	{
		this.orientation = ForgeDirection.SOUTH; //default direction
		this.customName = "";
		this.state = 0;
	}
	
	@Override
	public final void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setByte("orientation", (byte)this.orientation.ordinal());
		compound.setString("customName", this.customName);
		compound.setByte("state", this.state);
		this.writeTileEntityData(compound);
	}

	@Override
	public final void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.orientation = ForgeDirection.values()[compound.getByte("orientation")];
		this.customName = compound.getString("customName");
		this.state = compound.getByte("state");
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
	public final ForgeDirection getOrientation()
	{
		return this.orientation;
	}
	
	/**
	 * @param direction - the direction to make this tile entity face.
	 */
	public final void setOrientation(ForgeDirection direction)
	{
		this.orientation = direction != null ? direction : ForgeDirection.SOUTH;
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
	
	/**
	 * @return the state of this TileEntity.
	 */
	public final byte getState()
	{
		return this.state;
	}
	
	/**
	 * Sets the state of this TileEntity to the given byte.
	 */
	public final void setState(byte state)
	{
		this.state = state;
		this.markDirty();
	}
	
	/**
	 * Sets the bit in this TileEntity's state as on.
	 * @param flag - a number between 0 and 7.
	 */
	public final void setStateFlagOn(byte flag)
	{
		Preconditions.checkArgument(0 <= flag && flag <= 7, "TileEntity flag must be between 0 and 7.");
		this.state |= (1 << flag);
		this.markDirty();
	}
	
	/**
	 * Sets the bit in this TileEntity's state as off.
	 * @param flag - a number between 0 and 7.
	 */
	public final void setStateFlagOff(byte flag)
	{
		Preconditions.checkArgument(0 <= flag && flag <= 7, "TileEntity flag must be between 0 and 7.");
		this.state &= ~(1 << flag);
		this.markDirty();
	}
	
	/**
	 * Checks whether a flag is set in this TileEntity's state.
	 * @param flag - a number between 0 and 7.
	 * @return whether or not this flag is set in this TileEntity's state.
	 */
	public final boolean isStateFlagSet(byte flag)
	{
		Preconditions.checkArgument(0 <= flag && flag <= 7, "TileEntity flag must be between 0 and 7.");
		return (this.state & (1 << flag)) != 0;
	}
	
	/**
	 * Toggles the flag in this TileEntity's state.
	 * @param flag - a number between 0 and 7.
	 */
	public final void toggleStateFlag(byte flag)
	{
		this.state ^= (1 << flag);
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
