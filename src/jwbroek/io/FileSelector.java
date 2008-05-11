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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Convenience class for selecting files based on some criterium.
 * @author jwbroek
 */
public class FileSelector
{
  /**
   * FileFilter that accepts only directories.
   */
  private static FileFilter dirsFileFilter = new FileFilter()
  {
    public boolean accept(final File file)
    {
      return file.isDirectory();
    }
  };
  
  /**
   * FileFilter that accepts only files (no directories).
   */
  private static FileFilter filesFilter = new FileFilter()
  {
    public boolean accept(final File file)
    {
      return file.isFile();
    }
  };
  
  /**
   * Constructor. Should not be used as this class doesn't need to be instantiated.
   */
  private FileSelector()
  {
    // Intentionally empty, as this class doesn't need to be instantiated.
  }
  
  /**
   * Get a FileFilter that accepts only directories.
   * @return A FileFilter that accepts only directories.
   */
  public static FileFilter getDirsFilter()
  {
    return FileSelector.dirsFileFilter;
  }
  
  /**
   * Get a FileFilter that accepts only files (no directories).
   * @return A FileFilter that accepts only files (no directories).
   */
  public static FileFilter getFilesFilter()
  {
    return FileSelector.filesFilter;
  }
  
  /**
   * Get a FileFilter that will accept only files such that their canonical paths match the pattern.
   * @param pattern The pattern to match.
   * @return A FileFilter that will accept only files such that their canonical paths match the pattern.
   */
  public static FileFilter getPathPatternFilter(final Pattern pattern)
  {
    return new FileFilter()
    {
      public boolean accept(final File file)
      {
        try
        {
          return pattern.matcher(file.getCanonicalPath()).matches();
        }
        catch (IOException e)
        {
          return false;
        }
        catch (SecurityException e)
        {
          return false;
        }
      }
    };
  }
  
  /**
   * Get a FileFilter that will accept only files such that their names match the pattern.
   * @param pattern The pattern to match.
   * @return A FileFilter that will accept only files such that their names match the pattern.
   */
  public static FileFilter getFileNamePatternFilter(final Pattern pattern)
  {
    return new FileFilter()
    {
      public boolean accept(final File file)
      {
        try
        {
          return pattern.matcher(file.getName()).matches();
        }
        catch (SecurityException e)
        {
          return false;
        }
      }
    };
  }
  
  /**
   * Get a FileFilter that combines all the specified FileFilters, such that it will only accept a file
   * if it is accepted by all specified FileFilters. The filters will be tested in order, so it is generally
   * preferable to put cheap and highly discriminating filters at low indices.
   * @param fileFilters The FileFilter instances to combine.
   * @return A FileFilter that combines all the specified FileFilters, such that it will only accept a file
   * if it is accepted by all specified FileFilters.
   */
  public static FileFilter getCombinedFileFilter(final FileFilter ... fileFilters)
  {
    // The FileFilter ... parameter has lower (implicit) priority than Iterable<FileFilter>, so there is no
    // (directly) recursive call here.
    return FileSelector.getCombinedFileFilter(fileFilters);
  }
  
  /**
   * Get a FileFilter that combines all the specified FileFilters, such that it will only accept a file
   * if it is accepted by all specified FileFilters. The filters will be tested in order, so it is generally
   * preferable to put cheap and highly discriminating filters at low indices.
   * @param fileFilters The FileFilter instances to combine.
   * @return A FileFilter that combines all the specified FileFilters, such that it will only accept a file
   * if it is accepted by all specified FileFilters.
   */
  public static FileFilter getCombinedFileFilter(final Iterable<FileFilter> fileFilters)
  {
    return new FileFilter()
    {
      public boolean accept(final File file)
      {
        for (FileFilter fileFilter : fileFilters)
        {
          if (! fileFilter.accept(file))
          {
            return false;
          }
        }
        
        return true;
      }
    };
  }
  
  /**
   * Select files such that their canonical paths match the pattern.
   * @param baseFile Base to start looking for files.
   * @param pattern The pattern that the files must match.
   * @param recurseDepth The depth of subdirectories to recurse into. If 0, then only the base directory will
   * be processed (excluding any subfiles and subdirectories). If Long.MAX_VALUE, then recursion depth is
   * indefinite. If < 0, then no files will be processed.
   * @param considerBaseFile If set to false, then the baseFile will not be selected, even if it passes the
   * FileFilter.
   * @param keepGoing Whether or not to keep going when a SecurityException occurs. If true,
   * then such exceptions will be caught and the method will try to continue selecting more files.
   * @return A list of files such that they match the pattern.
   */
  public static List<File> selectFiles
    ( final File baseFile
    , final Pattern pattern
    , final long recurseDepth
    , final boolean considerBaseFile
    , final boolean keepGoing
    )
  {
    List<File> fileList = new ArrayList<File>();
    
    selectFiles(baseFile, getPathPatternFilter(pattern), fileList, recurseDepth, considerBaseFile, keepGoing);
    
    return fileList;
  }
  
  /**
   * Select files based on the specified criteria.
   * @param baseFile Base to start looking for files in. May be a proper file or a directory. If it passes the
   * filter, it will be added, unless the considerBaseFile parameter is set to false.
   * @param fileFilter The filter that the files must be tested against.
   * @param fileList Files that are matched will be added to this list.
   * @param recurseDepth The depth of subdirectories to recurse into. If 0, then only the base directory will
   * be processed (excluding any subfiles and subdirectories). If Long.MAX_VALUE, then recursion depth is
   * indefinite. If < 0, then no files will be processed.
   * @param considerBaseFile If set to false, then the baseFile will not be selected, even if it passes the
   * FileFilter.
   * @param keepGoing Whether or not to keep going when a SecurityException occurs.
   */
  public static void selectFiles
    ( final File baseFile
    , final FileFilter fileFilter
    , final List<File> fileList
    , final long recurseDepth
    , final boolean considerBaseFile
    , final boolean keepGoing
    )
  {
    // We've gone too deep, so stop.
    if (recurseDepth < 0)
    {
      return;
    }
    
    // Add the base file to the list, if it qualifies.
    try
    {
      if (considerBaseFile && fileFilter.accept(baseFile))
      {
        fileList.add(baseFile);
      }
    }
    catch (SecurityException e)
    {
      if (!keepGoing)
      {
        throw e;
      }
    }
    
    // Recurse if the base file is a directory.
    if (baseFile.isDirectory())
    {
      try
      {
        // We have to check for null due to java bug 5086412.
        File [] subFiles = baseFile.listFiles();
        if (subFiles != null)
        {
          long childRecurseDepth = recurseDepth==Long.MAX_VALUE?recurseDepth:recurseDepth-1;
          for (File childFile : subFiles)
          {
            selectFiles(childFile, fileFilter, fileList, childRecurseDepth, true, keepGoing);
          }
        }
      }
      catch (SecurityException e)
      {
        if (!keepGoing)
        {
          throw e;
        }
      }
    }
  }
}
