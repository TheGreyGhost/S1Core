package com.shieldbug1.core.api;

import net.minecraft.entity.EntityLivingBase;

public interface IStandAware //TODO javadoc
{
	public abstract void onStanding(EntityLivingBase entity, int x, int y, int z);
}
