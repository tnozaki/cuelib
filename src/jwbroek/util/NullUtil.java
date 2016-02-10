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
package jwbroek.util;

import java.io.File;

/**
 * Utility class for helping to deal with some of the more inconvenient aspects of null. Note that this
 * class is often inappropriate. When a null value can cause real trouble, you should definitely allow the
 * VM to throw a NullPointerException rather than using the methods in this class. An Exception is better than
 * unpredictable behaviour.
 * @author jwbroek
 */
final public class NullUtil
{
  /**
   * This class does not need to be instantiated.
   */
  private NullUtil()
  {
  }
  
  /**
   * Get a String representation of the specified Object instance as per {@link Object#toString()}, or null if the
   * Object instance is a null reference.
   * @param o The Object instance to convert to String.
   * @return A String representation of the specified Object instance as per {@link Object#toString()}, or null if the
   * Object instance is a null reference.
   */
  public static String toString(final Object o)
  {
    String result = o==null?null:o.toString();
    return result;
  }
  
  /**
   * Get a String representation of the specified Object instance as per {@link Object#toString()}, or the specified
   * default value if the Object instance is a null reference.
   * @param o The Object instance to convert to String.
   * @param defaultValue The value to return if the Object instance is a null reference.
   * @return A String representation of the specified Object instance as per {@link Object#toString()}, or the
   * specified default value if the Object instance is a null reference.
   */
  public static String toString(final Object o, final String defaultValue)
  {
    String result = o==null?defaultValue:o.toString();
    return result;
  }
  
  /**
   * Get a String representation of the specified Object instance as per {@link Object#toString()}, or "null" if the
   * Object instance is a null reference.
   * @param o The Object instance to convert to String.
   * @return A String representation of the specified Object instance as per {@link Object#toString()}, or "null" if
   * the Object instance is a null reference.
   */
  public static String toGuaranteedString(final Object o)
  {
    String result = o==null?"null":o.toString();
    return result;
  }
  
  /**
   * Create a File instance based on the specified String, or null if the String is null.
   * @param file A String representing the File, or null.
   * @return A File instance based on the specified String, or null if the String is null.
   */
  public static File toFile(final String file)
  {
    final File result = file==null?null:new File(file);
    return result;
  }
  
  /**
   * Create an Enum instance based on the specified String, or null if the String is null.
   * @param value A String representing the Enum, or null.
   * @param enumType The Class instance of the Enum type to create.
   * @return An Enum instance based on the specified String, or null if the String is null.
   */
  public static <T extends Enum<T>> T toEnum(final String value, final Class<T> enumType)
  {
    final T result = value==null?null:Enum.valueOf(enumType, value);
    return result;
  }
  
  /**
   * Create a Long instance based on the specified String, or null if the String is null.
   * @param longValue A String representing the Long, or null.
   * @return A Long instance based on the specified String, or null if the String is null.
   */
  public static Long toLong(final String longValue)
  {
    final Long result = longValue==null?null:Long.getLong(longValue);
    return result;
  }
  
  /**
   * Get the specified value, or the default value if the value was null.
   * @param value
   * @param defaultValue The value to return in case the value was null.
   * @return The specified value, or the default value if the value was null.
   */
  public static <E> E nullValue(final E value, final E defaultValue)
  {
    final E result = value==null?defaultValue:value;
    return result;
  }
  
  /**
   * Get the specified value, or the default value if the value was null. Shorthand for
   * {@link #nullValue(Object, Object)}.
   * @param value
   * @param defaultValue The value to return in case the value was null.
   * @return The specified value, or the default value if the value was null.
   */
  public static <E> E nvl(final E value, final E defaultValue)
  {
    final E result = NullUtil.nvl(value, defaultValue);
    return result;
  }

}
