package com.shieldbug1.core.internal;

import static com.shieldbug1.lib.util.CoreFunctions.checkNotNull;
import static com.shieldbug1.lib.util.CoreFunctions.nullDefaultFunction;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.shieldbug1.lib.math.MathUtils;

/**
 * Class that contains all base properties.
 */
public final class InternalProperties
{
	private InternalProperties(){}

	/** Whether or not to throw incompatibility exceptions. */
	public static final Property<Boolean> throwIncompatibleExceptions = Property.create("S1Core.throwIncompatibleExceptions", new Function<String, Boolean>()
			{
		@Override public Boolean apply(String s)
		{
			return Boolean.valueOf(s);
		}}, Boolean.TRUE); //XXX JAVA 8
	
	/** The maximum size of the Schematic cache. */
	public static final Property<Long> schematicCacheMaxSize = Property.create("S1Core.schematicCacheSize", nullDefaultFunction(
			new Function<String, Long>()
			{
				@Override
				public Long apply(String s)
				{
					return MathUtils.longParseSafe(s);
				}
			}, new Supplier<Long>(){

		@Override
		public Long get()
		{
			return 100L;
		}}), 100L); //XXX JAVA 8
	
	/**
	 * Property class itself.
	 */
	public static class Property<T>
	{
		private final Function<String, T> transformation;
		private final String stringValue;
		private final T defaultValue;
		
		private Property(String valueName, Function<String, T> parseFunction, T defaultValue)
		{
			this.stringValue = System.getProperty(valueName);
			this.defaultValue = defaultValue;
			this.transformation = checkNotNull(parseFunction, "Property needs a parseFunction, and it can not be null!");
		}
		
		/**
		 * @return returns the value of this property.
		 */
		public T value()
		{
			if(this.stringValue == null)
			{
				return this.defaultValue;
			}
			else
			{
				try
				{
					return this.transformation.apply(this.stringValue);
				}
				catch(Exception e)
				{
					return this.defaultValue;
				}
			}
		}
		
		public static <T> Property<T> create(String valueName, Function<String, T> parseFunction, T defaultValue)
		{
			return new Property<T>(valueName, parseFunction, defaultValue);
		}
	}
}