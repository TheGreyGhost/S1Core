package com.shieldbug1.core.api;

import net.minecraftforge.fml.common.event.*;

import com.google.common.annotations.Beta;

/**
 * Implement this interface on any class that should run during loading.
 * The class's name must end with '{@code Module}' so that it gets run during the early stages of Minecraft loading.
 * This class allows for non-mod classes to benefit from the loading stages of FML.
 * 
 * @Beta This interface is still a work in progress, and I may change my mind at any time about it's usuage.
 */
@Beta
public interface LoadingModule
{
	public abstract void setUp();
	
	public abstract void preInit(FMLPreInitializationEvent event);
	
	public abstract void init(FMLInitializationEvent event);
	
	public abstract void postInit(FMLPostInitializationEvent event);
}
