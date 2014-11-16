package com.shieldbug1.core.internal.handler;

import java.util.List;

import net.minecraftforge.client.event.GuiOpenEvent;

import com.shieldbug1.core.S1Core;
import com.shieldbug1.lib.java.ReflectionCache;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class InternalClientEventHandler
{
	public static final InternalClientEventHandler INSTANCE = new InternalClientEventHandler();
	
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) //XXX Dirty hack - might wanna find a better way to do this. 
	{
		if(event.gui instanceof GuiModList)
		{
			ModContainer container = FMLCommonHandler.instance().findContainerFor(S1Core.MOD_ID);
			((List<ModContainer>)ReflectionCache.getFieldValue(GuiModList.class, (GuiModList)event.gui, "mods")).remove(container);
		}
	}
}
