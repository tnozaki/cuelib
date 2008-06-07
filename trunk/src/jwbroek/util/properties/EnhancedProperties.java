/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007-2008 Jan-Willem van den Broek
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package jwbroek.util.properties;

import java.util.Properties;
import java.util.logging.Logger;

import jwbroek.util.NullUtil;

/**
 * <p>Properties implementation that has been enhanced with utility methods for convenient storing and loading
 * of various data types besides strings. Note that it is the responsibility of the user to use the correct
 * method for loading properties. This implementation has no way of knowing what the type of the property is.
 * All properties can always be retrieved as Strings. This method stores data in files that are identical to
 * those of {@link Properties}.</p>
 * <p>This class behaves differently then {@link Properties} when a null value is stored. {@link Properties} will
 * not accept null, while this class will remove the specified property.</p>
 * @author jwbroek
 */
public class EnhancedProperties extends Properties
{
  /**
   * Generated UID to comply with the contract of {@linkplain java.io.Serializable}.
   */
  private static final long serialVersionUID = 4279677865735630015L;
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(EnhancedProperties.class.getCanonicalName());
  /**
   * The PropertyHandlerFactory for handling property conversions.
   */
  public final PropertyHandlerFactory propertyHandlerFactory;
  
  /**
   * Create an empty EnhancedProperties.
   */
  public EnhancedProperties()
  {
    super();
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties()");
    this.propertyHandlerFactory = PropertyHandlerFactoryImpl.getInstance();
    EnhancedProperties.logger.exiting(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties()");
  }
  
  /**
   * Create an EnhancedProperties with identical values as the specified Properties.
   * @param properties The Properties to copy.
   */
  public EnhancedProperties(final Properties properties)
  {
    super(properties);
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties(Properties)");
    this.propertyHandlerFactory = PropertyHandlerFactoryImpl.getInstance();
    EnhancedProperties.logger.exiting(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties(Properties)");
  }
  
  /**
   * Create an EnhancedProperties with identical values as the specified Properties.
   * @param propertyHandlerFactory The PropertyHandlerFactory for handling property conversions.

   */
  public EnhancedProperties(final PropertyHandlerFactory propertyHandlerFactory)
  {
    super();
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties(Properties)");
    this.propertyHandlerFactory = propertyHandlerFactory;
    EnhancedProperties.logger.exiting(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties(Properties)");
  }
  
  /**
   * Create an EnhancedProperties with identical values as the specified Properties.
   * @param properties The Properties to copy.
   * @param propertyHandlerFactory The PropertyHandlerFactory for handling property conversions.
   */
  public EnhancedProperties(final Properties properties, final PropertyHandlerFactory propertyHandlerFactory)
  {
    super(properties);
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties(Properties)");
    this.propertyHandlerFactory = propertyHandlerFactory;
    EnhancedProperties.logger.exiting(EnhancedProperties.class.getCanonicalName(), "EnhancedProperties(Properties)");
  }
  
  /**
   * Store the specified Enum under the specified key. Property will be removed if value is null.
   * @param key
   * @param value May be null, in which case the property is removed.
   * @return The previous value stored under this key. Note that this is not guaranteed to have been a Enum.
   */
  public <T extends Enum<T>> T setProperty(final String key, final T value)
  {
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "setProperty(String,Enum)");
    final String previousValue;
    if (value==null)
    {
      previousValue = (String) super.remove(key);
    }
    else
    {
      previousValue = (String) super.setProperty(key, value.toString());
    }
    final T result = NullUtil.toEnum(previousValue, value.getDeclaringClass());
    EnhancedProperties.logger.exiting(EnhancedProperties.class.getCanonicalName(), "setProperty(String,Enum)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as an Enum instance.
   * @param key
   * @param enumType The Class instance of the Enum type to create.
   * @return The value stored under the specified key as an Enum instance.
   */
  public <T extends Enum<T>> T getProperty(final String key, final Class<T> enumType)
  {
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "getPropertyAsEnum(String,Class)");
    final T result = NullUtil.toEnum(super.getProperty(key), enumType);
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsEnum(String,Class)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as an Enum instance, or the default value if the value was not
   * present.
   * @param key
   * @param defaultValue
   * @return The value stored under the specified key as an Enum instance.
   */
  public <T extends Enum<T>> T getProperty(final String key, final T defaultValue)
  {
    EnhancedProperties.logger.entering(EnhancedProperties.class.getCanonicalName(), "getPropertyAsEnum(String,Enum)");
    final T result = NullUtil.nullValue
      (NullUtil.toEnum(super.getProperty(key), defaultValue.getDeclaringClass()), defaultValue);
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsEnum(String,Enum)", result);
    return result;
  }
  
  /**
   * Store the specified value under the specified key. Property will be removed if value is null.
   * @param key
   * @param value May be null, in which case the property is removed.
   * @param handler An appropriate handler for the specified value.
   * @return The previous value stored under this key. Will be null if no value was previously stored.
   */
  public <T> T setProperty(final String key, final T value, final PropertyHandler<T> handler)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "setProperty(String,Object,PropertyHandler)");
    final Object previousValue;
    if (value==null)
    {
      previousValue = super.remove(key);
    }
    else
    {
      previousValue = super.setProperty(key, handler.toProperty(value));
    }
    final T result = previousValue==null?null:handler.fromProperty(previousValue.toString()); 
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "setProperty(String,Object,PropertyHandler)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as some desired type.
   * @param key
   * @param handler An appropriate handler for the desired value.
   * @return The value stored under the specified key as some desired type.
   */
  public <T> T getProperty(final String key, final PropertyHandler<T> handler)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "getProperty(String,PropertyHandler)");
    final T result = this.getProperty(key, null, handler);
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getProperty(String,PropertyHandler)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as some desired type, or the default value if the value was not
   * present.
   * @param key
   * @param defaultValue
   * @param handler An appropriate handler for the desired value.
   * @return The value stored under the specified key as some desired type.
   */
  public <T> T getProperty(final String key, final T defaultValue, final PropertyHandler<T> handler)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "getProperty(String,Object,PropertyHandler)");
    final String value = super.getProperty(key);
    final T result = value==null?defaultValue:handler.fromProperty(value);
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getProperty(String,Object,PropertyHandler)", result);
    return result;
  }

  /**
   * Store the specified value under the specified key. Property will be removed if value is null.
   * @param key
   * @param value
   * @return The previous value stored under this key. Will be null if no value was previously stored.
   */
  public Boolean setProperty(final String key, final Boolean value)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "setProperty(String,Boolean)");
    final Boolean result = this.setProperty
      (key, value, this.propertyHandlerFactory.getPropertyHandler(Boolean.class));
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "setProperty(String,Boolean)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as a Boolean.
   * @param key
   * @return The value stored under the specified key as a Boolean.
   */
  public Boolean getPropertyAsBoolean(final String key)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsBoolean(String)");
    final Boolean result = this.getProperty(key, this.propertyHandlerFactory.getPropertyHandler(Boolean.class));
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsBoolean(String)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as a Boolean, or the default value if the value was not
   * present.
   * @param key
   * @param defaultValue
   * @return The value stored under the specified key as a Boolean.
   */
  public Boolean getPropertyAsBoolean(final String key, final Boolean defaultValue)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsBoolean(String,Boolean)");
    final Boolean result = this.getProperty
      (key, defaultValue, this.propertyHandlerFactory.getPropertyHandler(Boolean.class));
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsBoolean(String,Boolean)", result);
    return result;
  }

  /**
   * Store the specified value under the specified key.
   * @param key
   * @param value May be null, in which case the property is removed.
   * @return The previous value stored under this key. Will be null if no value was previously stored.
   */
  public Long setProperty(final String key, final Long value)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "setProperty(String,Long)");
    final Long result = this.setProperty
      (key, value, this.propertyHandlerFactory.getPropertyHandler(Long.class));
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "setProperty(String,Long)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as a Long.
   * @param key
   * @return The value stored under the specified key as a Long.
   */
  public Long getPropertyAsLong(final String key)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsLong(String)");
    final Long result = this.getProperty(key, this.propertyHandlerFactory.getPropertyHandler(Long.class));
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsLong(String)", result);
    return result;
  }
  
  /**
   * Get the value stored under the specified key as a Long, or the default value if the value was not
   * present.
   * @param key
   * @param defaultValue
   * @return The value stored under the specified key as a Long.
   */
  public Long getPropertyAsLong(final String key, final Long defaultValue)
  {
    EnhancedProperties.logger.entering
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsLong(String,Long)");
    final Long result = this.getProperty
      (key, defaultValue, this.propertyHandlerFactory.getPropertyHandler(Long.class));
    EnhancedProperties.logger.exiting
      (EnhancedProperties.class.getCanonicalName(), "getPropertyAsLong(String,Long)", result);
    return result;
  }
}