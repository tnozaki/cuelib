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
import java.util.logging.Level;
import java.util.logging.Logger;

import jwbroek.util.LogUtil;

/**
 * Utility class for creating temporary files.
 * @author jwbroek
 */
final public class TemporaryFileCreator
{
  /**
   * The logger for this class.
   */
  private final static Logger logger = Logger.getLogger(TemporaryFileCreator.class.getCanonicalName());
  /**
   * Counter for added to the names of temporary files and directories.
   */
  private static int counter = 0;
  /**
   * Lock for counter.
   */
  private static Object counterLock = new Object();
  
  /**
   * Create a temporary directory based on the information provided. The directory will be
   * deleted when the VM ends. An effort is made to avoid naming conflicts, but such a
   * conflict cannot be guaranteed to be avoided. When a conflict occurs, an exception will
   * be thrown.
   * @return A temporary directory, as specified.
   * @throws IOException Thrown when the directory could not be created.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary directory to be created.
   */
  public static File createTemporaryDirectory() throws IOException, SecurityException
  {
    TemporaryFileCreator.logger.entering
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryDirectory()");
    final File result = TemporaryFileCreator.createTemporaryDirectory(null);
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryDirectory()", result);
    return result;
  }
  
  /**
   * Create a temporary directory based on the information provided. The directory will be
   * deleted when the VM ends. An effort is made to avoid naming conflicts, but such a
   * conflict cannot be guaranteed to be avoided. When a conflict occurs, an exception will
   * be thrown.
   * @param baseDir The directory to create the temporary directory in.
   * @return A temporary directory, as specified.
   * @throws IOException Thrown when the directory could not be created.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary directory to be created.
   */
  public static File createTemporaryDirectory(File baseDir) throws IOException, SecurityException
  {
    TemporaryFileCreator.logger.entering
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryDirectory(File)");
    final File result = TemporaryFileCreator.createTemporaryFileOrDirectory
      ("TemporaryFileCreator", null, baseDir, true, false, 5);
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryDirectory(File)", result);
    return result;
  }
  
  /**
   * Create a temporary file based on the information provided. The file will be deleted when
   * the VM ends. An effort is made to avoid naming conflicts, but such a conflict cannot be
   * guaranteed to be avoided. When a conflict occurs, an exception will be thrown. It is not
   * guaranteed that the name conforms exactly to what is specified.
   * @return A temporary file, as specified.
   * @throws IOException Thrown when the file could not be created.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary file to be created.
   */
  public static File createTemporaryFile() throws IOException, SecurityException
  {
    TemporaryFileCreator.logger.entering(TemporaryFileCreator.class.getCanonicalName(), "createTemporaryFile()");
    final File result = TemporaryFileCreator.createTemporaryFile(null);
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryFile()", result);
    return result;
  }
  
  /**
   * Create a temporary file based on the information provided. The file will be deleted when
   * the VM ends. An effort is made to avoid naming conflicts, but such a conflict cannot be
   * guaranteed to be avoided. When a conflict occurs, an exception will be thrown. It is not
   * guaranteed that the name conforms exactly to what is specified.
   * @param baseDir The directory to create the temporary directory in.
   * @return A temporary file, as specified.
   * @throws IOException Thrown when the file could not be created.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary file to be created.
   */
  public static File createTemporaryFile(File baseDir) throws IOException, SecurityException
  {
    TemporaryFileCreator.logger.entering(TemporaryFileCreator.class.getCanonicalName(), "createTemporaryFile(File)");
    final File result = TemporaryFileCreator.createTemporaryFileOrDirectory
      ("TemporaryFileCreator", null, baseDir, false, false, 5);
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), "createTemporaryFile(File)", result);
    return result;
  }
  
  /**
   * Create a temporary file or directory based on the information provided. The file or
   * directory will be deleted when the VM ends. An effort is made to avoid naming conflicts,
   * but such a conflict cannot be guaranteed to be avoided. When a conflict occurs, an exception
   * will be thrown.
   * @param prefix The prefix for the temporary file.
   * @param suffix The suffix for the temporary file. May be null, in which case it will default
   * to ".tmp".
   * @param directory The directory to create the file in. May be null, in which case the default
   * directory for temporary files will be used.
   * @param maxAttempts The maximum number of attempts to create a temporary file. Whenever the
   * temporary file cannot be created due to an IOException (most likely caused by a naming
   * conflict), another attempt with a new name is made, up to the maximum number of attempts.
   * @param createDirectory If true, then a directory will be created, otherwise a file will be
   * created.
   * @param exactName Whether or not to guarantee that the exact name as specified is used for the
   * temporary file. When false, this method is more likely to succeed.
   * @return A temporary file, as specified.
   * @throws IOException Thrown when the file or directory could not be created, even after the
   * specified number of attempts.
   * @throws IllegalArgumentException Thrown when maxAttempts is smaller than 1.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary file or directory to be created.
   */
  public static File createTemporaryFileOrDirectory
    ( final String prefix
    , final String suffix
    , final File directory
    , final boolean createDirectory
    , final boolean exactName
    , final int maxAttempts
    ) throws IOException, IllegalArgumentException, SecurityException
  {
    final String methodName = "createTemporaryFileOrDirectory(String,String,File,boolean,int)";
    TemporaryFileCreator.logger.entering
      ( TemporaryFileCreator.class.getCanonicalName()
      , methodName
      , new Object [] {prefix, suffix, directory, maxAttempts, createDirectory}
      );
    
    IOException ioException = null;
    File result = null;
    
    if (maxAttempts < 1)
    {
      // TODO This error message should come from a ResourceBundle.
      IllegalArgumentException tooFewAttemptsException =
        new IllegalArgumentException("maxAttempts must be at least 1.");
      TemporaryFileCreator.logger.throwing
        ( TemporaryFileCreator.class.getCanonicalName()
        , methodName
        , tooFewAttemptsException
        );
      throw tooFewAttemptsException;
    }
    
    // The filename consists of the prefix, a random number in hex, and a number from the counter.
    // This is probably a little overkill, but you never know...
    int counterNumber;
    synchronized(TemporaryFileCreator.counterLock)
    {
      counterNumber = counter++;
    }
    StringBuilder nameBuilder = new StringBuilder(prefix)
      .append(Double.toHexString(Math.random()))
      .append(counterNumber)
      ;
    
    // Make the specified number of attempt to create a temporary file.
    for (int attempt = 0; result == null && attempt < maxAttempts; attempt++)
    {
      try
      {
        result = TemporaryFileCreator.createNamedTemporaryFileOrDirectory
          ( nameBuilder.toString()
          , suffix
          , directory
          , createDirectory
          , exactName
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
        (TemporaryFileCreator.class.getCanonicalName(), methodName, ioException);
      throw ioException;
    }
    
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), methodName, result);
    return result;
  }

  /**
   * Create a temporary file or directory based on the information provided. The file or
   * directory will be deleted when the VM ends.
   * @param name The name for the temporary file or directory.
   * @param suffix The suffix for the temporary file. May be null, in which case it will default
   * to ".tmp" for files and empty string for directories.
   * @param directory The directory to create the file in. May be null, in which case the default
   * directory for temporary files will be used.
   * @param createDirectory If true, then a directory will be created, otherwise a file will be
   * created.
   * @param exactName Whether or not to guarantee that the exact name as specified is used for the
   * temporary file. When false, this method is more likely to succeed.
   * @return A temporary file, as specified.
   * @throws IOException Thrown when the file or directory could not be created.
   * @throws SecurityException Thrown when a {@link java.lang.SecurityManager} does not allow
   * the temporary file or directory to be created.
   */
  public static File createNamedTemporaryFileOrDirectory
    ( final String name
    , final String suffix
    , final File directory
    , final boolean createDirectory
    , final boolean exactName
    ) throws IOException, IllegalArgumentException, SecurityException
  {
    final String methodName = "createNamedTemporaryFileOrDirectory(String,String,File,boolean,boolean)";
    TemporaryFileCreator.logger.entering
      ( TemporaryFileCreator.class.getCanonicalName()
      , methodName
      , new Object [] {name, suffix, directory, createDirectory}
      );
    
    File result = null;
    
    // Determine directory to create the file or directory in.
    final File parentDirectory;
    if (directory==null)
    {
      parentDirectory = new File(System.getProperty("java.io.tmpdir"));
    }
    else
    {
      parentDirectory = directory;
    }
    
    if (createDirectory)
    {
      // We need to create a temporary directory.
      result = new File(parentDirectory, name + (suffix==null?"":suffix));
      if (!result.mkdir())
      {
        // There was a problem creating the file.
        // TODO This error message should come from a ResourceBundle.
        IOException couldNotCreateDirException = new IOException
          ("Could not create directory: '" + result.toString() + "'");
        TemporaryFileCreator.logger.throwing
          ( TemporaryFileCreator.class.getCanonicalName()
          , methodName
          , couldNotCreateDirException
          );
        throw couldNotCreateDirException;
      }
    }
    else
    {
      // We need to create a temporary file.
      if (exactName)
      {
        result = new File(parentDirectory, name + (suffix==null?".tmp":suffix));
        result.createNewFile();
      }
      else
      {
        result = File.createTempFile(name + (suffix==null?".tmp":suffix), suffix, parentDirectory);
      }
    }
    
    // Request that the file is deleted after the VM ends.
    result.deleteOnExit();
    
    TemporaryFileCreator.logger.exiting
      (TemporaryFileCreator.class.getCanonicalName(), methodName, result);
    return result;
  }
}
