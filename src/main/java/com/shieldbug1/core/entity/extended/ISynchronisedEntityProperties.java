package com.shieldbug1.core.entity.extended;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.shieldbug1.core.network.DefaultPackets;
/**
 * A simple way to sync extend properties from Server to Client. To synchronise,
 * simply call {@link DefaultPackets#syncEntityProperties(ISynchronisedEntityProperties)}
 */
public abstract interface ISynchronisedEntityProperties extends IExtendedEntityProperties
{
	/**
	 * @return the entity id that these properties belong to.
	 */
	public abstract Entity getEntity();
	
	/**
	 * @return the unique string identifier for these properties.
	 */
	public abstract String getUniqueIdentifier();
	
//	/**
//	 * Override this method if you need to write properties to the NBTTagCompound that you do not want to write in {@link #saveNBTData(NBTTagCompound)}.
//	 */
//	public default void writeProperties(NBTTagCompound compound)
//	{
//		this.saveNBTData(compound);
//	}
//	
//	/**
//	 * Override this method if you need to read properties from the NBTTagCompound that you do not want to read in {@link #loadNBTData(NBTTagCompound)}.
//	 * Generally this shouldn't need to happen, unless you have overriden {@link #writeProperties(NBTTagCompound)}.
//	 */
//	public default void readProperties(NBTTagCompound compound)
//	{
//		this.loadNBTData(compound);
//	}
//	XXX JAVA 8 default methods.
}
