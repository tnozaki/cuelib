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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link jwbroek.io.FileSelector}.
 * @author jwbroek
 */
public class FileSelectorTest
{
  /**
   * The root of the testing environment.
   */
  private File testRoot;
  /**
   * Maximum number of files to create.
   */
  private final static int maxFiles = 200;
  /**
   * First file will have a name based on this number.
   */
  private final static int firstFile = 1000;
  /**
   * Maximum number of directories to create.
   */
  private final static int maxDirs = 30;
  /**
   * First directories will have a name based on this number.
   */
  private final static int firstDir = 2000;
  /**
   * Prefix for numbered files in the root directory. Numbers range from {@link FileSelectorTest#firstFile} to
   * {@link FileSelectorTest#firstFile} + {@link FileSelectorTest#maxFiles}.
   */
  private final static String rootFilePrefix = "testFile";
  /**
   * Prefix for numbered subdirs in the root directory. Numbers range from {@link FileSelectorTest#firstDir} to
   * {@link FileSelectorTest#firstDir} + {@link FileSelectorTest#maxDirs}.
   */
  private final static String rootSubdirPrefix = "testDir";
  
  /**
   * <p>Create a testing environment containing various files and directories.</p>
   * <p>The layout is as follows: ([] is directory; {} is logical name (not actual name))</p>
   * <ul>
   *   <li>[{testRoot}]</li>
   *   <ul>
   *     <li>{rootFilePrefix}{firstFile}</li>
   *     <li>...</li>
   *     <li>{rootFilePrefix}{firstFile+maxFiles-1}</li>
   *     <li>{testRoot}file</li>
   *     <li>[{rootFilePrefix}{firstDir}]</li>
   *     <li>[...]</li>
   *     <li>[testDir{firstDir+maxDirs-1}]</li>
   *     <li>[{testRoot}]</li>
   *     <li>[a]</li>
   *     <ul>
   *       <li>[a]</li>
   *       <ul>
   *         <li>a</li>
   *         <li>[b1]</li>
   *         <li>[b2]</li>
   *         <li>b3</li>
   *         <li>b4</li>
   *       </ul>
   *       <li>[b1]</li>
   *       <li>[b2]</li>
   *       <li>b3</li>
   *       <li>b4</li>
   *     </ul>
   *   </ul>
   * </ul>
   * @throws SecurityException
   * @throws IOException
   */
  @Before
  public void setUp() throws SecurityException, IOException
  {
    // File bound
    final int fileBound = firstFile + maxFiles;
    // Dir bound
    final int dirBound = firstDir + maxDirs;
    
    // Create a temporary directory in which to create a test environment.
    this.testRoot = TemporaryFileCreator.createTemporaryDirectory();
    
    // Create numbered files.
    for (int fileIndex = firstFile; fileIndex < fileBound; fileIndex++)
    {
      TemporaryFileCreator.createNamedTemporaryFileOrDirectory
        (FileSelectorTest.rootFilePrefix + fileIndex, "", this.testRoot, false, true);
    }
    // Create a file with a name that starts with the name of the root directory and ends in "file".
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory
      (this.testRoot.getName() + "file", "", this.testRoot, false, true);
    
    // Create numbered directories.
    for (int dirIndex = firstDir; dirIndex < dirBound; dirIndex++)
    {
      TemporaryFileCreator.createNamedTemporaryFileOrDirectory
        ("testDir" + dirIndex, "", this.testRoot, true, true);
    }
    // Create a directory with the same name as the root directory.
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory
      (this.testRoot.getName(), "", this.testRoot, true, true);
    
    // Create a directory called "a" in the root.
    File newRootDir = TemporaryFileCreator.createNamedTemporaryFileOrDirectory
      ("a", "", this.testRoot, true, true);
    // Create a subdirectory "a" in directory "a", as well as b1...b4.
    File subDir = TemporaryFileCreator.createNamedTemporaryFileOrDirectory
      ("a", "", newRootDir, true, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b1", "", newRootDir, true, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b2", "", newRootDir, true, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b3", "", newRootDir, false, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b4", "", newRootDir, false, true);
    // Create a file "a" in directory "a/a", as well as b1...b4.
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory
      ("a", "", subDir, false, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b1", "", subDir, true, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b2", "", subDir, true, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b3", "", subDir, false, true);
    TemporaryFileCreator.createNamedTemporaryFileOrDirectory("b4", "", subDir, false, true);
  }
  
  /**
   * Clean up the testing environment.
   */
  @After
  public void cleanUp()
  {
    // Delete the temporary file. First check if creation went OK.
    if (this.testRoot!=null)
    {
      this.testRoot.delete();
    }
  }
  
  /**
   * Test for {@link FileSelector#getDirsFilter()}.
   */
  @Test
  public void testDirsFileFilter() throws IOException
  {
    FileFilter filter = FileSelector.getDirsFilter();
    Set<File> prediction = new HashSet<File>();
    final int bound = FileSelectorTest.firstDir + FileSelectorTest.maxDirs;
    for (int index = FileSelectorTest.firstDir; index < bound; index++)
    {
      prediction.add(new File(this.testRoot, FileSelectorTest.rootSubdirPrefix + index));      
    }
    prediction.add(new File(this.testRoot, this.testRoot.getName()));
    prediction.add(new File(this.testRoot, "a"));      
    testFileFilter(this.testRoot, filter, prediction, "FileSelector.getDirsFilter()");
  }

  /**
   * Test for {@link FileSelector#getFilesFilter()}.
   */
  @Test
  public void testFilesFileFilter() throws IOException
  {
    FileFilter filter = FileSelector.getFilesFilter();
    Set<File> prediction = new HashSet<File>();
    final int bound = FileSelectorTest.firstFile + FileSelectorTest.maxFiles;
    for (int index = FileSelectorTest.firstFile; index < bound; index++)
    {
      prediction.add(new File(this.testRoot, FileSelectorTest.rootFilePrefix + index));      
    }
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    testFileFilter(this.testRoot, filter, prediction, "FileSelector.testFilesFileFilter()");
  }
  
  /**
   * Test for {@link FileSelector#getFileNamePatternFilter(Pattern)}.
   */
  @Test
  public void testFileNamePatternFilter()
  {
    // Create a FileNamePatternFilter that must match any file of which the name begins with "test", then either
    // "File" or "Dir", then 3 digits, and then a "1" or "2". No other files must be matched.
    FileFilter filter = FileSelector.getFileNamePatternFilter(Pattern.compile("test(?:File|Dir)\\d{3}[12].*"));
    Set<File> prediction = new HashSet<File>();
    
    // We can be clever and optimize these loops by using "%", greater increments and some "if" statements. This is
    // much clearer though, and speed isn't much of an issue, so I'll leave it like this.
    
    // Add all qualifying files.
    final int fileBound = FileSelectorTest.firstFile + FileSelectorTest.maxFiles;
    for (int index = FileSelectorTest.firstFile; index < fileBound; index++)
    {
      if (index > 1000 && index < 10000 && (index % 10 == 1 || index % 10 == 2))
      {
        prediction.add(new File(this.testRoot, FileSelectorTest.rootFilePrefix + index));
      }
    }

    // Add all qualifying directories.
    final int dirBound = FileSelectorTest.firstDir + FileSelectorTest.maxDirs;
    for (int index = FileSelectorTest.firstDir; index < dirBound; index++)
    {
      if (index > 1000 && index < 10000 && (index % 10 == 1 || index % 10 == 2))
      {
        prediction.add(new File(this.testRoot, FileSelectorTest.rootSubdirPrefix + index));
      }
    }
    testFileFilter(this.testRoot, filter, prediction, "FileSelector.getFileNamePatternFilter(Pattern)");
  }
  
  /**
   * Create a Pattern that must match any file that starts with the name of its parent directory.
   * @return A Pattern that must match any file that starts with the name of its parent directory.
   */
  public static Pattern getParentDirNameAsFileNamePattern()
  {
    final String patternDirSeparator = Pattern.quote(File.separator);
    final StringBuilder patternHelper = new StringBuilder(".*")
      .append(patternDirSeparator)
      .append("([^")
      .append(patternDirSeparator)
      .append("]*)")
      .append(patternDirSeparator)
      .append("\\1[^")
      .append(patternDirSeparator)
      .append("]*")
      .append(patternDirSeparator)
      .append("?$")
      ;
    return Pattern.compile(patternHelper.toString());
  }

  /**
   * Test for {@link FileSelector#getPathPatternFilter(Pattern)}.
   */
  @Test
  public void testPathPatternFilter()
  {
    // Create a FileNamePatternFilter that must match any file that starts with the name of its parent directory.
    final FileFilter filter = FileSelector.getPathPatternFilter(FileSelectorTest.getParentDirNameAsFileNamePattern());
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    prediction.add(new File(this.testRoot, this.testRoot.getName()));
    testFileFilter(this.testRoot, filter, prediction, "FileSelector.getPathPatternFilter(Pattern)");
  }
  
  /**
   * Test for {@link FileSelector#getCombinedFileFilter(FileFilter[])} and
   * {@link FileSelector#getCombinedFileFilter(Iterable)}.
   */
  @Test
  public void testCombinedFileFilterFromArray()
  {
    // Create a CombinedFileFilter that must match only files (not directories) that start with the name
    // of their parent directory.
    final FileFilter filesFilter = FileSelector.getFilesFilter();
    final FileFilter pathPatternFilter = FileSelector.getPathPatternFilter
      (FileSelectorTest.getParentDirNameAsFileNamePattern());
    final FileFilter combinedFileFilter = FileSelector.getCombinedFileFilter(filesFilter, pathPatternFilter);
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    testFileFilter
      (this.testRoot, combinedFileFilter, prediction, "FileSelector.getCombinedFileFilter(FileFilter[])");
  }
  
  /**
   * Test for {@link FileSelector#getCombinedFileFilter(Iterable)}.
   */
  @Test
  public void testCombinedFileFilterFromIterable()
  {
    // Create a CombinedFileFilter that must match only files (not directories) that start with the name
    // of their parent directory.
    final FileFilter filesFilter = FileSelector.getFilesFilter();
    final FileFilter pathPatternFilter = FileSelector.getPathPatternFilter
      (FileSelectorTest.getParentDirNameAsFileNamePattern());
    final List<FileFilter> fileFilterList = new ArrayList<FileFilter>();
    fileFilterList.add(filesFilter);
    fileFilterList.add(pathPatternFilter);
    final FileFilter combinedFileFilter = FileSelector.getCombinedFileFilter(fileFilterList);
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    testFileFilter
      (this.testRoot, combinedFileFilter, prediction, "FileSelector.getCombinedFileFilter(Iterable)");
  }
  
  /**
   * Test for {@link FileSelector#getIntersectionFileFilter(FileFilter[])} and
   * {@link FileSelector#getIntersectionFileFilter(Iterable)}.
   */
  @Test
  public void testIntersectionFileFilterFromArray()
  {
    // Create an IntersectionFileFilter that must match only files (not directories) that start with the name
    // of their parent directory.
    final FileFilter filesFilter = FileSelector.getFilesFilter();
    final FileFilter pathPatternFilter = FileSelector.getPathPatternFilter
      (FileSelectorTest.getParentDirNameAsFileNamePattern());
    final FileFilter intersectionFileFilter = FileSelector.getIntersectionFileFilter(filesFilter, pathPatternFilter);
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    testFileFilter
      (this.testRoot, intersectionFileFilter, prediction, "FileSelector.getIntersectionFileFilter(FileFilter[])");
  }
  
  /**
   * Test for {@link FileSelector#getIntersectionFileFilter(Iterable)}.
   */
  @Test
  public void testIntersectionFileFilterFromIterable()
  {
    // Create an IntersectionFileFilter that must match only files (not directories) that start with the name
    // of their parent directory.
    final FileFilter filesFilter = FileSelector.getFilesFilter();
    final FileFilter pathPatternFilter = FileSelector.getPathPatternFilter
      (FileSelectorTest.getParentDirNameAsFileNamePattern());
    final List<FileFilter> fileFilterList = new ArrayList<FileFilter>();
    fileFilterList.add(filesFilter);
    fileFilterList.add(pathPatternFilter);
    final FileFilter intersectionFileFilter = FileSelector.getIntersectionFileFilter(fileFilterList);
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    testFileFilter
      (this.testRoot, intersectionFileFilter, prediction, "FileSelector.getIntersectionFileFilter(Iterable)");
  }
  
  /**
   * Test for {@link FileSelector#getUnionFileFilter(FileFilter[])} and
   * {@link FileSelector#getUnionFileFilter(Iterable)}.
   */
  @Test
  public void testUnionFileFilterFromArray()
  {
    // Create an UnionFileFilter that must match only files or directories named "b1" or "b3".
    final FileFilter unionFileFilter =
      FileSelector.getUnionFileFilter
        (FileSelector.getFileNamePatternFilter("b1"), FileSelector.getFileNamePatternFilter("b3"));
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, "a/b1"));
    prediction.add(new File(this.testRoot, "a/b3"));
    testFileFilter
      (new File(this.testRoot, "a"), unionFileFilter, prediction, "FileSelector.getUnionFileFilter(FileFilter[])");
  }
  
  /**
   * Test for {@link FileSelector#getUnionFileFilter(Iterable)}.
   */
  @Test
  public void testUnionFileFilterFromIterable()
  {
    // Create an UnionFileFilter that must match only files or directories named "b1" or "b3".
    final FileFilter nameFilterB1 = FileSelector.getFileNamePatternFilter("b1");
    final FileFilter nameFilterB3 = FileSelector.getFileNamePatternFilter("b3");
    final List<FileFilter> fileFilterList = new ArrayList<FileFilter>();
    fileFilterList.add(nameFilterB1);
    fileFilterList.add(nameFilterB3);
    final FileFilter unionFileFilter = FileSelector.getUnionFileFilter(fileFilterList);
    final Set<File> prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, "a/b1"));
    prediction.add(new File(this.testRoot, "a/b3"));
    testFileFilter
      (new File(this.testRoot, "a"), unionFileFilter, prediction, "FileSelector.getUnionFileFilter(Iterable)");
  }
  
  /**
   * Test for {@link FileSelector#selectFiles(File, Pattern, long, boolean, boolean)} with negative depth.
   */
  @Test
  public void testSelectFilesWithNegativeDepth()
  {
    final List<File> matchedFiles;
    final Set<File> prediction = new HashSet<File>();
    
    // Should match no files due to depth of -1.
    matchedFiles = FileSelector.selectFiles(this.testRoot, Pattern.compile(".*"), -1, true, true);
    testFilesAgainstPrediction
      (matchedFiles, prediction, "FileSelector.selectFiles(File,Pattern,boolean,boolean); negative depth");
  }
  
  /**
   * Test for {@link FileSelector#selectFiles(File, Pattern, long, boolean, boolean)} with depth zero.
   */
  @Test
  public void testSelectFilesWithDepthZero()
  {
    final List<File> matchedFiles;
    final Set<File> prediction = new HashSet<File>();
    
    // Should match only root due to depth of 0.
    matchedFiles = FileSelector.selectFiles(this.testRoot, Pattern.compile(".*"), 0, true, true);
    prediction.add(this.testRoot);
    testFilesAgainstPrediction
      (matchedFiles, prediction, "FileSelector.selectFiles(File,Pattern,boolean,boolean); depth zero");
  }
  
  /**
   * Test for {@link FileSelector#selectFiles(File, Pattern, long, boolean, boolean)} with depth zero and no
   * consideration of base file.
   */
  @Test
  public void testSelectFilesWithDepthZeroNoBaseFile()
  {
    final List<File> matchedFiles;
    final Set<File> prediction = new HashSet<File>();
    
    // Should match nothing due to considerBaseFile=false and depth = 0. 
    matchedFiles = FileSelector.selectFiles(this.testRoot, Pattern.compile(".*"), 0, false, true);
    testFilesAgainstPrediction
      ( matchedFiles
      , prediction
      , "FileSelector.selectFiles(File,Pattern,boolean,boolean); depth zero, no consideration of base file"
      );
  }
  
  /**
   * Test for {@link FileSelector#selectFiles(File, Pattern, long, boolean, boolean)} for all files at depth one
   * of which the name starts with the name of the root.
   */
  @Test
  public void testSelectFilesWithDepthOneAndNameStartingWithRootName()
  {
    final List<File> matchedFiles;
    final Set<File> prediction;
    
    // Match all files and directories in the root directory that have the same name as the root.
    matchedFiles = FileSelector.selectFiles
      (this.testRoot, FileSelectorTest.getParentDirNameAsFileNamePattern(), 1, false, true);
    prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    prediction.add(new File(this.testRoot, this.testRoot.getName()));
    testFilesAgainstPrediction
      ( matchedFiles
      , prediction
      , "FileSelector.selectFiles(File,Pattern,boolean,boolean);"
      + " all files at depth one of which the name starts with the name of the root"
      );
  }
  
  /**
   * Test for {@link FileSelector#selectFiles(File, Pattern, long, boolean, boolean)} for all files of which the
   * name starts with the name of their parent.
   */
  @Test
  public void testSelectFilesWithNameStartingWithParentName()
  {
    final List<File> matchedFiles;
    final Set<File> prediction;
    
    // Match all files and directories in the entire tree minus the root that have a name that starts with
    // the name of their parent.
    matchedFiles = FileSelector.selectFiles
      (this.testRoot, FileSelectorTest.getParentDirNameAsFileNamePattern(), Integer.MAX_VALUE, false, true);
    prediction = new HashSet<File>();
    prediction.add(new File(this.testRoot, "a" + File.separator + "a"));
    prediction.add(new File(this.testRoot, "a" + File.separator + "a" + File.separator + "a"));
    prediction.add(new File(this.testRoot, this.testRoot.getName()));
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    testFilesAgainstPrediction
      ( matchedFiles
      , prediction
      , "FileSelector.selectFiles(File,Pattern,boolean,boolean); "
      + "all files of which the name starts with the name of the root"
      );
  }
  
  /**
   * Test for {@link FileSelector#selectFiles(File, FileFilter, List, long, boolean, boolean)}.
   */
  @Test
  public void testSelectFilesFromFileFilter()
  {
    final FileFilter filter = FileSelector.getFilesFilter();
    final List<File> matchedFiles = new ArrayList<File>();
    final Set<File> prediction = new HashSet<File>();
    final int bound = FileSelectorTest.firstFile + FileSelectorTest.maxFiles;
    for (int index = FileSelectorTest.firstFile; index < bound; index++)
    {
      prediction.add(new File(this.testRoot, FileSelectorTest.rootFilePrefix + index));      
    }
    prediction.add(new File(this.testRoot, this.testRoot.getName() + "file"));
    prediction.add(new File(this.testRoot, "a" + File.separator + "b3"));
    prediction.add(new File(this.testRoot, "a" + File.separator + "b4"));
    prediction.add(new File(this.testRoot, "a" + File.separator + "a" + File.separator + "a"));
    prediction.add(new File(this.testRoot, "a" + File.separator + "a" + File.separator + "b3"));
    prediction.add(new File(this.testRoot, "a" + File.separator + "a" + File.separator + "b4"));
    
    // Match all files.
    FileSelector.selectFiles(this.testRoot, filter, matchedFiles, Integer.MAX_VALUE, false, true);
    testFilesAgainstPrediction
      ( matchedFiles
      , prediction
      , "FileSelector.selectFiles(File, FileFilter, List, long, boolean, boolean); "
      + "all files, but not directories"
      );
  }
  
  /**
   * Test the specified {@link FileFilter}.
   * @param directory The directory where the test must take place.
   * @param filter The filter to test.
   * @param predictedResult The files and directories predicted to pass the filter.
   * @param filterName The name of the filter. Is used for reporting purposes.
   */
  private void testFileFilter
    ( final File directory
    , final FileFilter filter
    , final Set<File> predictedResult
    , final String filterName
    )
  {
    // Get the files that matched the filter.
    final File [] matchedFiles = directory.listFiles(filter);
    
    testFilesAgainstPrediction(Arrays.asList(matchedFiles), predictedResult, filterName);
  }
  
  /**
   * Test the specified set of {@link File} instances against the predicted set of instances.
   * @param filesFound The files and directories that were found.
   * @param predictedResult The files and directories predicted to be found.
   * @param methodDescription The method that was used to find the files. Is used for reporting purposes.
   */
  private void testFilesAgainstPrediction
    ( final List<File> filesFound
    , final Set<File> predictedResult
    , final String methodDescription
    )
  {
    // Set of files that have not (yet) been matched, but have been predicted.
    final Set<File> unmatchedFiles = new HashSet<File>(predictedResult);
    final Set<File> matchedFiles = new HashSet<File>();
    matchedFiles.addAll(filesFound);
    
    // There should be no duplicate files matched. While this is not explicitly guaranteed, it would certainly be
    // highly undesirable.
    Assert.assertEquals
      ( "List of files found by '" + methodDescription + "' contains duplicates."
      , filesFound.size()
      , matchedFiles.size()
      );
    
    // All matched files must be in the prediction.
    for (File acceptedFile : filesFound)
    {
      Assert.assertTrue
        ( methodDescription + " accepted the unpredicted file: '" + acceptedFile.toString() + "'"
        , unmatchedFiles.contains(acceptedFile)
        );
      unmatchedFiles.remove(acceptedFile);
    }
    
    for (File unmatchedFile : unmatchedFiles)
    {
      Assert.assertTrue
        ( methodDescription + " did not match predicted file: '" + unmatchedFile.toString() + "'" 
        , false
        );
    }
  }
}
