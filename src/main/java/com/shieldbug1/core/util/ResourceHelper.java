package com.shieldbug1.core.util;

import java.io.*;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import com.google.common.cache.*;
import com.shieldbug1.core.S1Core;
import com.shieldbug1.core.internal.InternalProperties;
import com.shieldbug1.core.schematic.Schematic;
import com.shieldbug1.core.schematic.SchematicSize;
import com.shieldbug1.lib.java.Java;

import cpw.mods.fml.common.FMLLog;

public final class ResourceHelper
{
	private static File mcDir;
	
	private ResourceHelper(){}
	
	/** The schematic cache that keeps hold of all schematics.  */
	private static final LoadingCache<String, Schematic> schematicCache = CacheBuilder.newBuilder()
			.maximumSize(InternalProperties.schematicCacheMaxSize.value()).build(
			new CacheLoader<String, Schematic>()
			{
				@Override
				public Schematic load(String key) throws Exception
				{
					return loadSchematic(key);
				}
	
			});

	/**
	 * @return the Minecraft directory.
	 */
	public static File getMinecraftDirectory()
	{
		return mcDir;
	}
	
	/**
	 * @return the schematic directory.
	 */
	public static File getSchematicDirectory()
	{
		return new File(getMinecraftDirectory(), "mods" + File.separator + "schematics");
	}
	
	/**
	 * Saves the schematic to the fileName specified. Path is %MC_DIRECTORY%/mods/schematics/fileName.json
	 */
	public static void saveSchematic(Schematic schematic, String fileName, boolean override)
	{
		File file = new File(getSchematicDirectory(), fileName + ".json");
		
		if(override)
		{
			file.delete();
		}
		
		if(!file.exists())
		{
			try(FileOutputStream fileStream = new FileOutputStream(file))
			{
				file.createNewFile();
				IOUtils.write(schematic.toJson(), fileStream, "UTF-8");
			}
			catch(IOException e)
			{
				FMLLog.log(S1Core.MOD_ID, Level.ERROR, "Failed to save schematic " + fileName);
				e.printStackTrace();
			}
		}
		else //File already exists, and not told to override!
		{
			saveSchematic(schematic, fileName, 0);
		}
	}
	
	/**
	 * Loads a schematic from the %minecraftdirector%/mods/schematics/fliename.json -
	 * @return a Schematic with the given fileName.
	 */
	private static Schematic loadSchematic(String fileName)
	{
		File file = new File(getSchematicDirectory(), fileName + ".json");
		
		try(FileInputStream fileStream = new FileInputStream(file))
		{
			String json = IOUtils.toString(fileStream, "UTF-8");
			return Schematic.fromJson(json);
		}
		catch (IOException e)
		{
			FMLLog.log(S1Core.MOD_ID, Level.ERROR, "Failed to load schematic " + fileName);
			throw Java.<RuntimeException>throwUnchecked(e);
		}
	}
	
	/**
	 * @return a schematic from the given name.
	 */
	public static Schematic getSchematic(String name)
	{
		return schematicCache.getUnchecked(name);
	}
	
	//internal, to deal with clashes (and make code still look pretty).
	private static void saveSchematic(Schematic schematic, String fileName, int i)
	{
		File file = new File(getSchematicDirectory(), fileName + i + ".json");

		if(!file.exists())
		{
			saveSchematic(schematic, fileName + i, false);
		}
		else
		{
			saveSchematic(schematic, fileName, i++);
		}
	}
	
	public static void init(File dir)
	{
		mcDir = dir;
		getSchematicDirectory().mkdirs(); // Ensures schematic directory exists!
		makeSchematicTemplates();
	}

	@SuppressWarnings("deprecation") // Can ignore the warning, we're making sure we don't use it.
	private static void makeSchematicTemplates()
	{
		new File(getSchematicDirectory(), "templates").mkdirs(); //Ensure template directory exists
		for(SchematicSize size : SchematicSize.values())
		{
			if(size.ordinal() < SchematicSize.HUGE.ordinal())
			{
				Schematic schematic = new Schematic(size);
				saveSchematic(schematic, "templates" + File.separator + size.toString().toLowerCase(), true); //Create schematic templates.
			}
		}
	}
}
