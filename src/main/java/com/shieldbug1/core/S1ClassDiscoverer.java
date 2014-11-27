package com.shieldbug1.core;

import static org.apache.logging.log4j.Level.ERROR;
import static org.apache.logging.log4j.Level.WARN;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.CoreModManager;

import org.objectweb.asm.tree.ClassNode;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import com.shieldbug1.lib.asm.ASMHelper;
import com.shieldbug1.lib.java.Java;

public class S1ClassDiscoverer //Based off CB's ClassDiscoverer
{
	private final Predicate<String> nameChecker;
	private final Set<Class<?>> classTypes = Sets.newLinkedHashSet();
	private final ModClassLoader loader = (ModClassLoader) Loader.instance().getModClassLoader();
	private final Set<String> exclusions = ImmutableSet.<String>builder().addAll(this.loader.getDefaultLibraries()).addAll(CoreModManager.getLoadedCoremods()).build();
	public final List<Class<?>> found = Lists.newLinkedList();
	private final Set<String> superClasses = Sets.newHashSet();
	
	public S1ClassDiscoverer(Class<?>... classes)
	{
		this(Predicates.<String>alwaysTrue(), classes);
	}

	public S1ClassDiscoverer(Predicate<String> nameChecker, Class<?>... classes)
	{
		this.nameChecker = nameChecker;
		for(Class<?> clazz : classes)
		{
			this.classTypes.add(clazz);
			this.superClasses.add(clazz.getName().replace('.', '/'));
		}
	}
	
	public void find()
	{
		HashSet<String> searchedSources = Sets.newHashSet();
		for(File minecraftSource : this.loader.getParentSources())
		{
			System.out.println(minecraftSource.getName());
			if(searchedSources.contains(minecraftSource.getAbsolutePath()) || !this.checkModsDirectory(minecraftSource)) //Have we searched this already?
			{
				continue;
			}
			searchedSources.add(minecraftSource.getAbsolutePath()); //Not searching again.
			if(minecraftSource.isFile())
			{
				if(!exclusions.contains(minecraftSource.getName()))
				{
					this.readFromZipFile(minecraftSource);
				}
			}
			else if(minecraftSource.isDirectory())
			{
				this.readFromDirectory(minecraftSource, minecraftSource);
			}
		}
	}

	private boolean checkModsDirectory(File minecraftSource)
	{
		/* 
		 * This ensure we only search mods and the bin directory (otherwise we also search every single
		 * referenced library which takes way too long).
		 */
		if(minecraftSource.getName().equals("bin"))
		{
			return true;
		}
		File file = minecraftSource.getParentFile();
		do
		{
			if(file.getName().equals("mods"))
			{
				return true;
			}
			file = file.getParentFile();
		}
		while(file != null);
		return false;
	}

	private void readFromDirectory(File directory, File baseDirectory)
	{
		for(File file : directory.listFiles())
		{
			if(file.isDirectory())
			{
				this.readFromDirectory(file, baseDirectory);
			}
			else if(file.isFile() && this.nameChecker.apply(file.getName()))
			{
				String name = getRelativePath(baseDirectory, file);
				this.checkAndAddClass(name);
			}
		}
	}

	private void readFromZipFile(File file)
	{
		try(FileInputStream fileStream = new FileInputStream(file); ZipInputStream zipStream = new ZipInputStream(fileStream))
		{
			for(ZipEntry entry = zipStream.getNextEntry(); entry != null; )
			{
				String full = entry.getName().replace('\\', '/');
				int index = full.lastIndexOf('/');
				String name = index == -1 ? full : full.substring(index + 1);
				if(!entry.isDirectory() && this.nameChecker.apply(name))
				{
					this.checkAndAddClass(full);
				}
			}
			fileStream.close();
		}
		catch(Exception e)
		{
			FMLLog.log(S1Core.MOD_ID, WARN, e, "Error scanning zip file " + file.getName());
		}
	}

	private void checkAndAddClass(String name)
	{
		try
		{
			String className = name.replace(".class", "").replace("\\", ".").replace("/", ".");
			byte[] bytes = Launch.classLoader.getClassBytes(className);
			if(bytes != null)
			{
				ClassNode node = ASMHelper.createClassNode(bytes);
				for(String superClass : this.superClasses)
				{
					if(node.interfaces.contains(superClass) || node.superName.equals(superClass))
					{
						this.addClass(className);
					}
				}
			}
		}
		catch(Exception e)
		{
			FMLLog.log(S1Core.MOD_ID, ERROR, e, "Failed to load class: " + name);
		}
	}

	private void addClass(String className)
	{
		try
		{
			Class<?> clazz = Class.forName(className, true, this.loader);
			this.found.add(clazz);
		}
		catch(Exception e)
		{
			Java.<RuntimeException>throwUnchecked(e);
		}
	}

	private String getRelativePath(File parent, File child)
	{
		if(parent.isFile() || !child.getPath().startsWith(parent.getPath()))
		{
			return null;
		}
		else
		{
			return child.getPath().substring(parent.getPath().length() + 1);
		}
	}
}
