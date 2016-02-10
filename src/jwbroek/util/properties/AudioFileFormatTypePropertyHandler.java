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

import javax.sound.sampled.AudioFileFormat;

/**
 * PropertyHandler for {@link javax.sound.sampled.AudioFileFormat.Type}s.
 * @author jwbroek
 */
final public class AudioFileFormatTypePropertyHandler implements PropertyHandler<AudioFileFormat.Type>
{
  /**
   * The singleton instance of this class.
   */
  private static AudioFileFormatTypePropertyHandler instance = new AudioFileFormatTypePropertyHandler();
  
  /**
   * This constructor is only meant to be called by AudioFileFormatTypePropertyHandler itself, as
   * AudioFileFormatTypePropertyHandler is a singleton class.
   */
  private AudioFileFormatTypePropertyHandler()
  {
  }
  
  /**
   * Get an instance of this class.
   * @return An instance of this class.
   */
  public static AudioFileFormatTypePropertyHandler getInstance()
  {
    return AudioFileFormatTypePropertyHandler.instance;
  }
  
  /**
   * Convert the value to a String that can be used in a {@link Properties} instance.
   * @param value
   * @return A conversion of the value to a string that can be used in a {@link Properties} instance.
   */
  public String toProperty(final AudioFileFormat.Type value)
  {
    final String result = value.toString();
    return result;
  }

  /**
   * Convert the value from a {@link Properties} instance into a AudioFileFormat.Type instance.
   * @param value
   * @return A conversion of the value from a {@link Properties} instance into a AudioFileFormat.Type instance.
   * @throws CannotConvertPropertyException When the value could not be converted.
   */
  public AudioFileFormat.Type fromProperty(final String value) throws CannotConvertPropertyException
  {
    final AudioFileFormat.Type result;
    
    if ("AIFC".equals(value))
    {
      result = AudioFileFormat.Type.AIFC;
    }
    else if ("AIFF".equals(value))
    {
      result = AudioFileFormat.Type.AIFF;
    }
    else if ("AU".equals(value))
    {
      result = AudioFileFormat.Type.AU;
    }
    else if ("SND".equals(value))
    {
      result = AudioFileFormat.Type.SND;
    }
    else if ("WAVE".equals(value))
    {
      result = AudioFileFormat.Type.WAVE;
    }
    else
    {
      final CannotConvertPropertyException exception = new CannotConvertPropertyException
        ("Cannot convert to AudioFileFormat.Type: '" + value + "'");
      throw exception;
    }
    return result;
  }
}
