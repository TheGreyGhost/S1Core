package com.shieldbug1.core.schematic;

import java.lang.reflect.Type;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.shieldbug1.lib.area.WorldPosition;

import cpw.mods.fml.common.registry.GameData;

/**
 * This is the schematic class for World generation. They should be cached, and only recreated when lost.
 * 
 * Arrays are kept in [y][x][z] ordering.
 */
public final class Schematic
{
	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Schematic.class, Serialiser.INSTNACE).setPrettyPrinting().create();
	
	private final SchematicSize size;
	/** This is the actual array of Blocks. It can be generated again using the nameArray. */
	private String[][][] nameArray;
	/** This is the array of metadata of the blocks. */
	private int[][][] metaArray;
	/** The actual block array. */
	private Block[][][] blockArray;
	
	private boolean flipX, flipY, flipZ;

	//TODO Worker thread to allow generation earlier while main thread continues? private volatile boolean isReady = false;
	
	/** Creates a blank schematic. */
	public Schematic(SchematicSize size)
	{
		this.size = size;
		this.metaArray = new int[this.size.value()][this.size.value()][this.size.value()];
		this.nameArray = emptyThreeDimensionalArray();
		this.blockArray = new Block[this.size.value()][this.size.value()][this.size.value()];
	}
	
	/** Creates a schematic with pre-set everything. This is for internal use only, for serialising. */
	private Schematic(SchematicSize size, String[][][] array, int[][][] meta, boolean flipX, boolean flipY, boolean flipZ)
	{
		this(size);
		this.nameArray = array;
		this.metaArray = meta;
		this.flipX = flipX;
		this.flipY = flipY;
		this.flipZ = flipZ;
		Serialiser.INSTNACE.deserialiseStringArray(this);
	}
	
	/**
	 * Sets the block in the given x, y, z positions in the schematic, relative to the schematic itself. (ranges are 0 to this.size.value())
	 */
	public void setBlock(int x, int y, int z, Block block)
	{
		this.nameArray[y][x][z] = GameData.getBlockRegistry().getNameForObject(block);
		this.blockArray[y][x][z] = block;
	}
	
	/**
	 * Sets the metadata in the given x, y, z positions in the schematic, relative to the schematic itself. (ranges are 0 to this.size.value())
	 */
	public void setMetadata(int x, int y, int z, int meta)
	{
		this.metaArray[y][x][z] = meta;
	}
	
	/**
	 * Convience method. See {@link #setBlock(int, int, int, Block)} and {@link #setMetadata(int, int, int, int)}
	 */
	public void setBlockWithMetadata(int x, int y, int z, Block block, int meta)
	{
		this.setBlock(x, y, z, block);
		this.setMetadata(x, y, z, meta);
	}
	
	/**
	 * Flips the x-axis.
	 */
	public void flipX()
	{
		this.flipX = !this.flipX;
	}
	
	/**
	 * Flips the y-axis.
	 */
	public void flipY()
	{
		this.flipY = !this.flipY;
	}
	
	/**
	 * Flips the z-axis.
	 */
	public void flipZ()
	{
		this.flipZ = !this.flipZ;
	}
	
	/**
	 * Spawns at given position. See {@link #spawnInWorld(World, int, int, int)}.
	 */
	public void spawnInWorld(WorldPosition position)
	{
		this.spawnInWorld(position.getWorld(), position.x, position.y, position.z);
	}
	
	/**
	 * Spawns in the given x, y, z  coordinates in the world.
	 */
	public void spawnInWorld(World world, int x, int y, int z)
	{
		if(!world.isRemote) //Only set blocks on server
		{
			for(int i = 0; i < this.size.value(); i++)//y-axis
			{
				for(int j = 0; j < this.size.value(); j++)//x-axis
				{
					for(int k = 0; k < this.size.value(); k++)//z-axis
					{
						final int meta = this.metaArray[i][j][k];
						final Block block = this.blockArray[i][j][k];
						if(block != null)
						{
							world.setBlock(
									x + (this.flipX ? this.size.value() - j : j),
									y + (this.flipY ? this.size.value() - i : i),
									z + (this.flipZ ? this.size.value() - k : k),
									block, meta, 3);
						}
					}
				}
			}			
		}
	}
	
	/**
	 * Spawns in the world, but doesn't override any blocks.
	 */
	public void spawnInWorldWherePossible(World world, int x, int y, int z)
	{
		if(!world.isRemote) //Only set blocks on server
		{
			for(int i = 0; i < this.size.value(); i++)//y-axis
			{
				for(int j = 0; j < this.size.value(); j++)//x-axis
				{
					for(int k = 0; k < this.size.value(); k++)//z-axis
					{
						final int meta = this.metaArray[i][j][k];
						final Block block = this.blockArray[i][j][k];
						if(block != null)
						{
							final int xPos = x + (this.flipX ? this.size.value() - j : j);
							final int yPos = y + (this.flipY ? this.size.value() - i : i);
							final int zPos = z + (this.flipZ ? this.size.value() - k : k);
							if(world.getBlock(xPos, yPos, zPos) == Blocks.air)
							{
								world.setBlock(xPos, yPos, zPos, block, meta, 3);
							}
						}
					}
				}
			}			
		}
	}
	
	/**
	 * See {@link #spawnInWorldWherePossible(World, int, int, int)}
	 */
	public void spawnInWorldWherePossible(WorldPosition position)
	{
		this.spawnInWorldWherePossible(position.getWorld(), position.x, position.y, position.z);
	}
	
	/**
	 * Breaks all the blocks in this schematic-defined area.
	 * @return an itemstack of all drops of the blocks on the server, and an empty list on the client.
	 */
	public List<ItemStack> breakBlocks(World world, int x, int y, int z)
	{
		List<ItemStack> drops = Lists.newArrayListWithExpectedSize(this.size.value() * this.size.value() * this.size.value() / 2); //Half the block expected to drop something approximately.
		if(!world.isRemote) //Only break blocks on the server.
		{
			for(int i = 0; i < this.size.value(); i++)//y-axis
			{
				for(int j = 0; j < this.size.value(); j++)//x-axis
				{
					for(int k = 0; k < this.size.value(); k++)//z-axis
					{
						final int xPos = x + (this.flipX ? this.size.value() - j : j);
						final int yPos = y + (this.flipY ? this.size.value() - i : i);
						final int zPos = z + (this.flipZ ? this.size.value() - k : k);
						final Block block = world.getBlock(xPos, yPos, zPos);
						if(block != Blocks.air)
						{
							drops.addAll(block.getDrops(world, xPos, yPos, zPos, world.getBlockMetadata(xPos, yPos, zPos), 0)); //Adds all drops to array list.
							world.setBlockToAir(xPos, yPos, zPos);
						}
					}
				}
			}			
		}
		return drops;
	}
	
	/**
	 * See {@link #breakBlocks(World, int, int, int)}
	 */
	public List<ItemStack> breakBlocks(WorldPosition position)
	{
		return this.breakBlocks(position.getWorld(), position.x, position.y, position.z);
	}
	
	/**
	 * Breaks and replaces all blocks.
	 * @return a list of all of the blocks drops on the server, and an empty list on the client.
	 */
	public List<ItemStack> breakAndReplaceBlocks(World world, int x, int y, int z)
	{
		List<ItemStack> drops = Lists.newArrayListWithExpectedSize(this.size.value() * this.size.value() * this.size.value() / 2); //Half the block expected to drop something approximately.
		if(!world.isRemote) //Only break blocks on the server.
		{
			for(int i = 0; i < this.size.value(); i++)//y-axis
			{
				for(int j = 0; j < this.size.value(); j++)//x-axis
				{
					for(int k = 0; k < this.size.value(); k++)//z-axis
					{
						final int xPos = x + (this.flipX ? this.size.value() - j : j);
						final int yPos = y + (this.flipY ? this.size.value() - i : i);
						final int zPos = z + (this.flipZ ? this.size.value() - k : k);
						final Block block = world.getBlock(xPos, yPos, zPos);
						final Block blockToSet = this.blockArray[i][j][k];
						final int metaToSet = this.metaArray[i][j][k];
						if(block != Blocks.air && blockToSet != null)
						{
							drops.addAll(block.getDrops(world, xPos, yPos, zPos, world.getBlockMetadata(xPos, yPos, zPos), 0)); //Adds all drops to array list.
							world.setBlock(xPos, yPos, zPos, blockToSet, metaToSet, 3);
						}
					}
				}
			}			
		}
		return drops;
	}
	
	/**
	 * See {@link #breakAndReplaceBlocks(World, int, int, int)}
	 */
	public List<ItemStack> breakAndReplaceBlocks(WorldPosition position)
	{
		return this.breakAndReplaceBlocks(position.getWorld(), position.x, position.y, position.z);
	}
	
	
	/*			SERIALISATION			*/
	
	public String toJson()
	{
		return gson.toJson(this);
	}
	
	public static Schematic fromJson(String json)
	{
		return gson.fromJson(json, Schematic.class);
	}
	
	/*########## INTERNAL USE ONLY ###########*/
	private String[][][] emptyThreeDimensionalArray()
	{
		String[][][] ret = new String[this.size.value()][this.size.value()][this.size.value()];
		for(int i = 0; i < this.size.value(); i++)
		{
			for(int j = 0; j < this.size.value(); j++)
			{
				for(int k = 0; k < this.size.value(); k++)
				{
					ret[i][j][k] = "";
				}
			}
		}
		return ret;
	}
	
	/** Privated nested class to serialise schematics easily, without the need of increasing visibility. */
	private static enum Serialiser implements JsonSerializer<Schematic>, JsonDeserializer<Schematic>
	{
		/** Singleton. */
		INSTNACE;
		
		@Override
		public JsonElement serialize(Schematic src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject jsonSchematic = new JsonObject();
			
			jsonSchematic.addProperty("size", src.size.ordinal());
			jsonSchematic.addProperty("flipX", src.flipX);
			jsonSchematic.addProperty("flipY", src.flipY);
			jsonSchematic.addProperty("flipZ", src.flipZ);		
			jsonSchematic.add("blocks", gson.toJsonTree(src.nameArray, String[][][].class));
			jsonSchematic.add("metadata", gson.toJsonTree(src.metaArray, int[][][].class));
			
			return jsonSchematic;
		}

		@Override
		public Schematic deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject jsonSchematic = (JsonObject) json;
			// Should throw exception for badly formated file.
			SchematicSize size = SchematicSize.values()[jsonSchematic.get("size").getAsInt()];
			boolean flipX = jsonSchematic.get("flipX").getAsBoolean();
			boolean flipY = jsonSchematic.get("flipY").getAsBoolean();
			boolean flipZ = jsonSchematic.get("flipZ").getAsBoolean();
			String[][][] blocks = gson.fromJson(jsonSchematic.get("blocks"), String[][][].class);
			int[][][] meta = gson.fromJson(jsonSchematic.get("metadata"), int[][][].class);
			return new Schematic(size, blocks, meta, flipX, flipY, flipZ);
		}
		
		public void deserialiseStringArray(Schematic schematic)
		{
			for(int i = 0; i < schematic.size.value(); i++)
			{
				for(int j = 0; j < schematic.size.value(); j++)
				{
					for(int k = 0; k < schematic.size.value(); k++)
					{
						schematic.blockArray[i][j][k] = Block.getBlockFromName(schematic.nameArray[i][j][k]);
					}
				}
			}
		}
	}
}