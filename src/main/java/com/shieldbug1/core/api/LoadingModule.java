package com.shieldbug1.core.api;

import cpw.mods.fml.common.event.*;

/**
 * Implement this interface on any class that should run during loading.
 * The class's name must end with `{@code Module}` so that it gets run during the early stages of Minecraft loading.
 * This class allows for non-mod classes to benefit from the loading stages of FML.
 */
public interface LoadingModule
{
	public abstract void preInit(FMLPreInitializationEvent event);
	
	public abstract void init(FMLInitializationEvent event);
	
	public abstract void postInit(FMLPostInitializationEvent event);

	public abstract void setUp();
}
