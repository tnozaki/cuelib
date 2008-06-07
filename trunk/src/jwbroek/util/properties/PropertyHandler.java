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

/**
 * <p>Interface for helper instances that can convert instances of other classes from and to Strings.
 * PropertyHandlers can be used to conveniently get and set Properties of types other than String.</p>
 * @author jwbroek
 */
public interface PropertyHandler<T>
{
  /**
   * Convert the value to a String that can be used in a {@link Properties} instance.
   * @param value
   * @return A conversion of the value to a String that can be used in a {@link Properties} instance.
   */
  public String toProperty(final T value);
  
  /**
   * Convert the value from a {@link Properties} instance into an instance of the type of this
   * PropertyHandler.
   * @param value
   * @return A conversion of the value from a {@link Properties} instance into an instance of the type
   * of this PropertyHandler.
   */
  public T fromProperty(final String value);
}
