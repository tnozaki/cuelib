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

import java.io.File;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;

/**
 * Implementation of PropertyHandlerFactory that supports the various PropertyHandlers in
 * jwbroek.util.properties.
 * @author jwbroek
 */
public class PropertyHandlerFactoryImpl implements PropertyHandlerFactory
{
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(PropertyHandlerFactoryImpl.class.getCanonicalName());
  /**
   * The singleton instance of this class.
   */
  private final static PropertyHandlerFactoryImpl instance = new PropertyHandlerFactoryImpl();
  
  /**
   * This constructor is only meant to be called by PropertyHandlerFactoryImpl itself, as
   * PropertyHandlerFactoryImpl is a singleton class.
   */
  private PropertyHandlerFactoryImpl()
  {
    super();
    PropertyHandlerFactoryImpl.logger.entering
      (PropertyHandlerFactoryImpl.class.getCanonicalName(), "PropertyHandlerFactoryImpl()");
    PropertyHandlerFactoryImpl.logger.exiting
      (PropertyHandlerFactoryImpl.class.getCanonicalName(), "PropertyHandlerFactoryImpl()");
  }
  
  /**
   * Get an instance of PropertyHandlerFactoryImpl.
   * @return An instance of PropertyHandlerFactoryImpl.
   */
  public static PropertyHandlerFactoryImpl getInstance()
  {
    PropertyHandlerFactoryImpl.logger.entering
      (PropertyHandlerFactoryImpl.class.getCanonicalName(), "PropertyHandlerFactoryImpl.getInstance()");
    PropertyHandlerFactoryImpl.logger.exiting
      ( PropertyHandlerFactoryImpl.class.getCanonicalName()
      , "PropertyHandlerFactoryImpl.getInstance()"
      , PropertyHandlerFactoryImpl.instance
      );
    return PropertyHandlerFactoryImpl.instance;
  }
  
  /**
   * Get a PropertyHandler for the specified type.
   * @param propertyType
   * @return A PropertyHandler for the specified type.
   * @throws UnsupportedOperationException When the specified type is not supported by this factory.
   */
  public <T> PropertyHandler<T> getPropertyHandler(Class<T> propertyType)
    throws UnsupportedOperationException
  {
    PropertyHandlerFactoryImpl.logger.entering
      (PropertyHandlerFactoryImpl.class.getCanonicalName(), "getPropertyHandler(Class<T>)", propertyType);
    
    final PropertyHandler result;
    
    if (propertyType.equals(AudioFileFormat.Type.class))
    {
      result = AudioFileFormatTypePropertyHandler.getInstance();
    }
    else if (propertyType.equals(Boolean.class))
    {
      result = BooleanPropertyHandler.getInstance();
    }
    else if (propertyType.equals(File.class))
    {
      result = FilePropertyHandler.getInstance();
    }
    else if (propertyType.equals(Long.class))
    {
      result = LongPropertyHandler.getInstance();
    }
    else
    {
      final UnsupportedOperationException exception = new UnsupportedOperationException
        ("Unsupported type: '" + propertyType.toString() + "'");
      PropertyHandlerFactoryImpl.logger.throwing
        (PropertyHandlerFactoryImpl.class.getCanonicalName(), "getPropertyHandler(Class<T>)", exception);
      throw exception;
    }
    
    PropertyHandlerFactoryImpl.logger.exiting
      ( PropertyHandlerFactoryImpl.class.getCanonicalName(), "getPropertyHandler(Class<T>)", result);
    // Unsafe operation, but there is no way around this (apart from doing lots of safe casts in the "if" blocks).
    return result;
  }

}
