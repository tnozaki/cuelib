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

/**
 * PropertyHandler for {@link Boolean}s.
 * @author jwbroek
 */
final public class BooleanPropertyHandler implements PropertyHandler<Boolean>
{
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(BooleanPropertyHandler.class.getCanonicalName());
  /**
   * The singleton instance of this class.
   */
  private static BooleanPropertyHandler instance = new BooleanPropertyHandler();
  
  /**
   * This constructor is only meant to be called by BooleanPropertyHandler itself, as
   * BooleanPropertyHandler is a singleton class.
   */
  private BooleanPropertyHandler()
  {
    BooleanPropertyHandler.logger.entering(BooleanPropertyHandler.class.getCanonicalName(), "BooleanPropertyHandler()");
    BooleanPropertyHandler.logger.exiting(BooleanPropertyHandler.class.getCanonicalName(), "BooleanPropertyHandler()");
  }
  
  /**
   * Get an instance of this class.
   * @return An instance of this class.
   */
  public static BooleanPropertyHandler getInstance()
  {
    BooleanPropertyHandler.logger.entering(BooleanPropertyHandler.class.getCanonicalName(), "getInstance()");
    BooleanPropertyHandler.logger.exiting
      (BooleanPropertyHandler.class.getCanonicalName(), "getInstance()", BooleanPropertyHandler.instance);
    return BooleanPropertyHandler.instance;
  }
  
  /**
   * Convert the value to a String that can be used in a {@link Properties} instance.
   * @param value
   * @return A conversion of the value to a string that can be used in a {@link Properties} instance.
   */
  public String toProperty(final Boolean value)
  {
    BooleanPropertyHandler.logger.entering
      (BooleanPropertyHandler.class.getCanonicalName(), "toProperty(Boolean)", value);
    final String result = value.toString();
    BooleanPropertyHandler.logger.exiting
      (BooleanPropertyHandler.class.getCanonicalName(), "toProperty(Boolean)", result);
    return result;
  }

  /**
   * Convert the value from a {@link Properties} instance into a Boolean instance.
   * @param value
   * @return A conversion of the value from a {@link Properties} instance into a Boolean instance.
   */
  public Boolean fromProperty(final String value)
  {
    BooleanPropertyHandler.logger.entering
      (BooleanPropertyHandler.class.getCanonicalName(), "fromProperty(String)", value);
    final Boolean result = Boolean.valueOf(value);
    BooleanPropertyHandler.logger.exiting
      (BooleanPropertyHandler.class.getCanonicalName(), "fromProperty(String)", result);
    return result;
  }
}
