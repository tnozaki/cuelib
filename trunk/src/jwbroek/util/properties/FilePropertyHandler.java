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
import java.util.Properties;
import java.util.logging.Logger;

/**
 * PropertyHandler for {@link File}s.
 * @author jwbroek
 */
final public class FilePropertyHandler implements PropertyHandler<File>
{
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(FilePropertyHandler.class.getCanonicalName());
  /**
   * The singleton instance of this class.
   */
  private final static FilePropertyHandler instance = new FilePropertyHandler();
  
  /**
   * This constructor is only meant to be called by FilePropertyHandler itself, as
   * FilePropertyHandler is a singleton class.
   */
  private FilePropertyHandler()
  {
    super();
    FilePropertyHandler.logger.entering(FilePropertyHandler.class.getCanonicalName(), "FilePropertyHandler()");
    FilePropertyHandler.logger.exiting(FilePropertyHandler.class.getCanonicalName(), "FilePropertyHandler()");
  }
  
  /**
   * Get an instance of this class.
   * @return An instance of this class.
   */
  public static FilePropertyHandler getInstance()
  {
    FilePropertyHandler.logger.entering
      (FilePropertyHandler.class.getCanonicalName(), "FilePropertyHandler.getInstance()");
    FilePropertyHandler.logger.exiting
      (FilePropertyHandler.class.getCanonicalName(), "FilePropertyHandler.getInstance()", FilePropertyHandler.instance);
    return FilePropertyHandler.instance;
  }
  
  /**
   * Convert the value to a String that can be used in a {@link Properties} instance.
   * @param value
   * @return A conversion of the value to a string that can be used in a {@link Properties} instance.
   */
  public String toProperty(final File value)
  {
    FilePropertyHandler.logger.entering(FilePropertyHandler.class.getCanonicalName(), "toProperty(File)", value);
    final String result = value.getPath();
    FilePropertyHandler.logger.exiting
      (FilePropertyHandler.class.getCanonicalName(), "toProperty(File)", result);
    return result;
  }

  /**
   * Convert the value from a {@link Properties} instance into a File instance.
   * @param value
   * @return A conversion of the value from a {@link Properties} instance into a File instance.
   */
  public File fromProperty(final String value)
  {
    FilePropertyHandler.logger.entering(FilePropertyHandler.class.getCanonicalName(), "fromProperty(String)", value);
    final File result = new File(value);
    FilePropertyHandler.logger.exiting
      (FilePropertyHandler.class.getCanonicalName(), "fromProperty(String)", result);
    return result;
  }
}
