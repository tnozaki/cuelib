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
 * PropertyHandler for {@link Long}s.
 * @author jwbroek
 */
final public class LongPropertyHandler implements PropertyHandler<Long>
{
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(LongPropertyHandler.class.getCanonicalName());
  /**
   * The singleton instance of this class.
   */
  private static LongPropertyHandler instance = new LongPropertyHandler();
  
  /**
   * This constructor is only meant to be called by LongPropertyHandler itself, as
   * LongPropertyHandler is a singleton class.
   */
  private LongPropertyHandler()
  {
    LongPropertyHandler.logger.entering(LongPropertyHandler.class.getCanonicalName(), "LongPropertyHandler()");
    LongPropertyHandler.logger.exiting(LongPropertyHandler.class.getCanonicalName(), "LongPropertyHandler()");
  }
  
  /**
   * Get an instance of this class.
   * @return An instance of this class.
   */
  public static LongPropertyHandler getInstance()
  {
    LongPropertyHandler.logger.entering(LongPropertyHandler.class.getCanonicalName(), "getInstance()");
    LongPropertyHandler.logger.exiting
      (LongPropertyHandler.class.getCanonicalName(), "getInstance()", LongPropertyHandler.instance);
    return LongPropertyHandler.instance;
  }
  
  /**
   * Convert the value to a String that can be used in a {@link Properties} instance.
   * @param value
   * @return A conversion of the value to a string that can be used in a {@link Properties} instance.
   */
  public String toProperty(final Long value)
  {
    LongPropertyHandler.logger.entering(LongPropertyHandler.class.getCanonicalName(), "toProperty(Long)", value);
    final String result = value.toString();
    LongPropertyHandler.logger.exiting
      (LongPropertyHandler.class.getCanonicalName(), "toProperty(Long)", result);
    return result;
  }

  /**
   * Convert the value from a {@link Properties} instance into a Long instance.
   * @param value
   * @return A conversion of the value from a {@link Properties} instance into a Long instance.
   */
  public Long fromProperty(final String value)
  {
    LongPropertyHandler.logger.entering(LongPropertyHandler.class.getCanonicalName(), "fromProperty(String)", value);
    final Long result = new Long(value);
    LongPropertyHandler.logger.exiting
      (LongPropertyHandler.class.getCanonicalName(), "fromProperty(String)", result);
    return result;
  }
}
