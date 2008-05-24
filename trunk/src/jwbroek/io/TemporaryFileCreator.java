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
package jwbroek.io;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import jwbroek.util.LogUtil;

/**
 * Utility class for creating temporary files.
 * @author jwbroek
 */
public class TemporaryFileCreator
{
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(TemporaryFileCreator.class.getCanonicalName());
  
  /**
   * Create a temporary file based on the information provided. The file will be deleted when
   * the VM ends. An effort is made to avoid naming conflicts, but such a conflict cannot be
   * guaranteed to be avoided. When a conflict occurs, an exception will be thrown.
   * @return A temporary file, as specified.
   * @throws IOException Thrown when the file could not be created.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary file to be created.
   */
  public static File createTemporaryFile() throws IOException, SecurityException
  {
    TemporaryFileCreator.logger.entering(TemporaryFileCreator.class.getCanonicalName(), "createTemporaryFile()");
    final File result = TemporaryFileCreator.createTemporaryFile("TemporaryFileCreator", null, null, 5);
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryFile()", result);
    return result;
  }
  
  /**
   * Create a temporary file based on the information provided. The file will be deleted when
   * the VM ends. An effort is made to avoid naming conflicts, but such a conflict cannot be
   * guaranteed to be avoided. When a conflict occurs, an exception will be thrown.
   * @param prefix The prefix for the temporary file.
   * @param suffix The suffix for the temporary file. May be null, in which case it will default
   * to ".tmp".
   * @param directory The directory to create the file in. May be null, in which case the default
   * directory for temporary files will be used.
   * @param maxAttempts The maximum number of attempts to create a temporary file. Whenever the
   * temporary file cannot be created due to an IOException (most likely caused by a naming
   * conflict), another attempt with a new name is made, up to the maximum number of attempts.
   * @return A temporary file, as specified.
   * @throws IOException Thrown when the file could not be created, even after the specified
   * number of attempts.
   * @throws IllegalArgumentException Thrown when maxAttempts is smaller than 1.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary file to be created.
   */
  public static File createTemporaryFile
    ( final String prefix
    , final String suffix
    , final File directory
    , final int maxAttempts
    ) throws IOException, IllegalArgumentException, SecurityException
  {
    TemporaryFileCreator.logger.entering
      ( TemporaryFileCreator.class.getCanonicalName()
      , "createTemporaryFile(String,String,File,int)"
      , new Object [] {prefix, suffix, directory, maxAttempts}
      );
    
    File result = null;
    IOException ioException = null;
    
    if (maxAttempts < 1)
    {
      // TODO This error message should come from a ResourceBundle.
      IllegalArgumentException tooFewAttemptsException =
        new IllegalArgumentException("maxAttempts must be at least 1.");
      TemporaryFileCreator.logger.throwing
        ( TemporaryFileCreator.class.getCanonicalName()
        , "createTemporaryFile(String,String,File,int)"
        , tooFewAttemptsException
        );
      throw tooFewAttemptsException;
    }
    
    // Make the specified number of attempt to create a temporary file.
    for (int attempt = 0; result == null && attempt < maxAttempts; attempt++)
    {
      try
      {
        // The filename consists of the prefix, the current time in milliseconds, and a
        // random number in hex, separated by underscores, and followed by the suffix.
        StringBuilder nameBuilder = new StringBuilder(prefix)
          .append('_')
          .append(Calendar.getInstance()
          .getTimeInMillis())
          .append('_')
          .append(Double.toHexString(Math.random()))
          ;
        
        result = File.createTempFile
          ( nameBuilder.toString() 
          , suffix
          , directory
          );
      }
      catch (IOException e)
      {
        // Save the exception, in case this is the last allowed attempt.
        ioException = e;
        LogUtil.logStacktrace(TemporaryFileCreator.logger, Level.FINE, ioException);
      }
    }
    
    // If we have no result, then that must be because an exception was thrown. We'll rethrow it.
    if (result == null)
    {
      TemporaryFileCreator.logger.throwing
        ( TemporaryFileCreator.class.getCanonicalName()
        , "createTemporaryFile(String,String,File,int)"
        , ioException
        );
      throw ioException;
    }
    
    // Make sure the file is deleted after the VM ends.
    result.deleteOnExit();
    
    TemporaryFileCreator.logger.exiting
      ( TemporaryFileCreator.class.getCanonicalName()
      , "createTemporaryFile(String,String,File,int)"
      , result
      );
    return result;
  }
}
