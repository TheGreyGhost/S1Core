package com.shieldbug1.core.util;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import net.minecraftforge.fml.relauncher.Side;

/**
 * Use on classes that should only be loaded on certain sides,
 * and crash the game with a more detailed exception if anything else is attempted.
 */
@Retention(RUNTIME)
@Inherited
@Target(TYPE)
public @interface SidedClass
{
	public static final String ASM_DESC = 'L' + SidedClass.class.getName().replace('.', '/') + ';';
	public abstract Side value();
}
