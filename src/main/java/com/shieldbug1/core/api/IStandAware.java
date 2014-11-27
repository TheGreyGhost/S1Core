package com.shieldbug1.core.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public interface IStandAware //TODO javadoc
{
	public abstract void onStanding(EntityLivingBase entity, BlockPos pos);
}
