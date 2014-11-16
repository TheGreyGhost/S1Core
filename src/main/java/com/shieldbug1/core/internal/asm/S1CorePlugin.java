package com.shieldbug1.core.internal.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.*;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"com.shieldbug1.core"})
@MCVersion("1.7.10")
@SortingIndex(0)
public final class S1CorePlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return
				new String[]
				{
					CoreTransformer.class.getName(),
					WorldTransformer.class.getName(),
				};
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return InternalEnvironment.class.getName();
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		
	}

}
