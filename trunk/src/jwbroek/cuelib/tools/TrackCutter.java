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
package jwbroek.cuelib.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import jwbroek.cuelib.CueParser;
import jwbroek.cuelib.CueSheet;
import jwbroek.cuelib.FileData;
import jwbroek.cuelib.Index;
import jwbroek.cuelib.Position;
import jwbroek.cuelib.TrackData;
import jwbroek.io.StreamPiper;
import jwbroek.util.StringReplacer;

public class TrackCutter
{
  public static final StringReplacer templateReplacer =
    new StringReplacer(getHumanReadableToFormatStringReplacements());

  public static final String DEFAULT_FILENAME = "<artist>_<album>_<track>_<title>.mp3";
  public static final String DEFAULT_POSTPROCESSOR_COMMAND =
    "c:\\lame\\lame.exe --vbr-new -V 0 -t --tt \"<title>\" --ta \"<artist>\" --tl \"<album>\" --ty \"<year>\" --tc \"<comment>\" --tn \"<track>\" --tg \"<genre>\" - \"<targetFile>\""; 
  
  private static void printHelp()
  {
    // TODO
  }
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // Make sure we have arguments.
    if (args.length == 0)
    {
      // TODO!
      printHelp();
      System.exit(1);
    }
    
    // Set defaults and control variables.
    String targetFileNameTemplate = DEFAULT_FILENAME;
    String processTemplate = DEFAULT_POSTPROCESSOR_COMMAND;
    String processPregapTemplate = null;
    String pregapFileNameTemplate = null;
    boolean pregapInSameFile = false;
    boolean redirectErr = false;
    boolean redirectStdOut = false;
    
    // Last argument must be a cue file.
    File cueFile = new File(args[args.length - 1]);
    
    // Parse remaining arguments.
    int maxParam = args.length - 1;
    for (int index = 0; index < maxParam; index++)
    {
      if ("-f".equals(args[index]))
      {
        if (++index < maxParam)
        {
          targetFileNameTemplate = args[index];
        }
        else
        {
          printHelp();
        }
      }
      else if ("-p".equals(args[index]))
      {
        if (++index < maxParam)
        {
          processTemplate = args[index];
        }
        else
        {
          printHelp();
        }
      }
      else if ("-pgd".equals(args[index]))
      {
        // Discard pregap
        pregapFileNameTemplate = null;
        pregapInSameFile = false;
      }
      else if ("-pgk".equals(args[index]))
      {
        // Keep pregap
        pregapFileNameTemplate = null;
        pregapInSameFile = true;
      }
      else if ("-pgs".equals(args[index]))
      {
        // Pregap comes in a separate file.
        index += 2;
        if (index < maxParam)
        {
          pregapFileNameTemplate = args[index-1];
          processPregapTemplate = args[index];
          pregapInSameFile = false;
        }
        else
        {
          printHelp();
        }
      }
      else if ("-re".equals(args[index]))
      {
        // Redirect errors.
        redirectErr = true;
      }
      else if ("-ro".equals(args[index]))
      {
        // Redirect standard out.
        redirectStdOut = true;
      }
    }
    
    // If pregap should come in the same file, then pregapFileNameTemplate should be equal to targetFileNameTemplate.
    if (pregapInSameFile)
    {
      pregapFileNameTemplate = targetFileNameTemplate;
    }
    
    try
    {
      processFilesInCueSheet  ( cueFile
                              , targetFileNameTemplate
                              , processTemplate
                              , pregapFileNameTemplate
                              , processPregapTemplate
                              , redirectErr
                              , redirectStdOut
                              );
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public static void processFilesInCueSheet
    ( File cueFile
    , String targetFileNameTemplate
    , String processTemplate
    , String pregapFileNameTemplate
    , String processPregapTemplate
    , boolean redirectErr
    , boolean redirectStdOut
    ) throws IOException
  {
    CueSheet cueSheet = null;
    
    try
    {
      cueSheet = CueParser.parse(cueFile);
    }
    catch (IOException e)
    {
      throw new IOException("Problem parsing cue file.", e);
    }

    // We can process each file in the cuesheet independently.
    for (FileData fileData : cueSheet.getFileData())
    {
      try
      {
        processFileData ( fileData
                        , cueSheet
                        , cueFile
                        , targetFileNameTemplate
                        , processTemplate
                        , pregapFileNameTemplate
                        , processPregapTemplate
                        , redirectErr
                        , redirectStdOut
                        );
      }
      catch (UnsupportedAudioFileException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  private static void processFileData ( FileData fileData
                                      , CueSheet cueSheet
                                      , File cueFile
                                      , String targetFileNameTemplate
                                      , String processTemplate
                                      , String pregapFileNameTemplate
                                      , String processPregapTemplate
                                      , boolean redirectErr
                                      , boolean redirectStdOut
                                      ) throws IOException, UnsupportedAudioFileException
  {
    AudioInputStream audioInputStream = null;
    
    try
    {
      // Determine the complete path to the audio file.
      File audioFile = new File(fileData.getFile());
      if (audioFile.getParent()==null)
      {
        // Use the directory of the cue file as the directory of the audio file.
        audioFile = new File(cueFile.getParent(), fileData.getFile());
      }
      
      // Determine the properties of the audio file.
      // Sadly, we can't do much with the file type information from the cue sheet, as javax.sound.sampled
      // needs more information before it can process a specific type of sound file. Best then to let it
      // determine all aspects of the audio type by itself.
      audioInputStream = AudioSystem.getAudioInputStream(audioFile);
      
      // Process all tracks in turn.
      List<TrackData> trackList = fileData.getTrackData();
      long currentFramePos = 0;
      for (int trackIndex = 0; trackIndex < trackList.size(); trackIndex++)
      {
        TrackData currentTrack = trackList.get(trackIndex);
        
        if (currentTrack.getIndex(0)==null || pregapFileNameTemplate==null)
        {
          // No pregap track, or we're not interested in it.
          Position nextPosition = null;
          if (trackIndex + 1 < trackList.size())
          {
            Index nextIndex = trackList.get(trackIndex + 1).getIndex(0);
            if (nextIndex==null)
            {
              nextIndex = trackList.get(trackIndex + 1).getIndex(1);
            }
            nextPosition = nextIndex.getPosition();
          }
          currentFramePos = createFileFromPartialAudioStream  ( currentTrack
                                                              , audioInputStream
                                                              , currentFramePos
                                                              , currentTrack.getIndex(1).getPosition()
                                                              , nextPosition
                                                              , targetFileNameTemplate
                                                              , cueFile.getParentFile()
                                                              , processTemplate
                                                              , redirectStdOut
                                                              , redirectErr
                                                              );
        }
        else
        {
          // We have a pregap track.
          if (targetFileNameTemplate.equals(pregapFileNameTemplate))
          {
            // Prepend pregap.
            
            Position nextPosition = null;
            if (trackIndex + 1 < trackList.size())
            {
              Index nextIndex = trackList.get(trackIndex + 1).getIndex(0);
              if (nextIndex==null)
              {
                nextIndex = trackList.get(trackIndex + 1).getIndex(1);
              }
              nextPosition = nextIndex.getPosition();
            }
            currentFramePos = createFileFromPartialAudioStream  ( currentTrack
                                                                , audioInputStream
                                                                , currentFramePos
                                                                , currentTrack.getIndex(0).getPosition()
                                                                , nextPosition
                                                                , pregapFileNameTemplate
                                                                , cueFile.getParentFile()
                                                                , processPregapTemplate
                                                                , redirectStdOut
                                                                , redirectErr
                                                                );
          }
          else
          {
            // Pregap in separate file.
            
            // Handle pregap.
            currentFramePos = createFileFromPartialAudioStream  ( currentTrack
                                                                , audioInputStream
                                                                , currentFramePos
                                                                , currentTrack.getIndex(0).getPosition()
                                                                , currentTrack.getIndex(1).getPosition()
                                                                , pregapFileNameTemplate
                                                                , cueFile.getParentFile()
                                                                , processPregapTemplate
                                                                , redirectStdOut
                                                                , redirectErr
                                                                );
            
            // Handle regular file.
            Position nextPosition = null;
            if (trackIndex + 1 < trackList.size())
            {
              Index nextIndex = trackList.get(trackIndex + 1).getIndex(0);
              if (nextIndex==null)
              {
                nextIndex = trackList.get(trackIndex + 1).getIndex(1);
              }
              nextPosition = nextIndex.getPosition();
            }
            currentFramePos = createFileFromPartialAudioStream  ( currentTrack
                                                                , audioInputStream
                                                                , currentFramePos
                                                                , currentTrack.getIndex(1).getPosition()
                                                                , nextPosition
                                                                , targetFileNameTemplate
                                                                , cueFile.getParentFile()
                                                                , processTemplate
                                                                , redirectStdOut
                                                                , redirectErr
                                                                );
          }
        }
      }
    }
    finally
    {
      if (audioInputStream!=null)
      {
        try
        {
          audioInputStream.close();
        }
        catch (IOException e)
        {
          // Nothing we can do about this.
        }
      }
    }
  }
  
  private static long createFileFromPartialAudioStream  ( TrackData trackData
                                                        , AudioInputStream audioInputStream
                                                        , long currentFramePos
                                                        , Position currentPosition
                                                        , Position nextPosition
                                                        , String targetFileNameTemplate
                                                        , File parentDir
                                                        , String processTemplate
                                                        , boolean redirectStdOut
                                                        , boolean redirectErr 
                                                        ) throws IOException
  {
    long fromFramePos = getAudioFormatFrames(currentPosition, audioInputStream.getFormat());
    long toFramePos = audioInputStream.getFrameLength();

    if (nextPosition != null)
    {
      toFramePos = getAudioFormatFrames(nextPosition, audioInputStream.getFormat());
    }
    
    audioInputStream.skip((fromFramePos - currentFramePos) * audioInputStream.getFormat().getFrameSize());
    
    createFile  ( trackData
                , new AudioInputStream(audioInputStream, audioInputStream.getFormat(), toFramePos - fromFramePos)
                , targetFileNameTemplate
                , parentDir
                , processTemplate
                , redirectStdOut
                , redirectErr
                );
    
    return toFramePos;
  }
  
  private static void createFile  ( TrackData trackData
                                  , AudioInputStream audioInputStream
                                  , String targetFileNameTemplate
                                  , File parentDir
                                  , String processTemplate
                                  , boolean redirectStdOut
                                  , boolean redirectErr
                                  ) throws IOException
  {
    // Get metadata.
    String title = trackData.getMetaData(CueSheet.MetaDataField.TITLE);
    String artist = trackData.getMetaData(CueSheet.MetaDataField.PERFORMER);
    String album = trackData.getMetaData(CueSheet.MetaDataField.ALBUMTITLE);
    String year = trackData.getMetaData(CueSheet.MetaDataField.YEAR);
    String comment = trackData.getMetaData(CueSheet.MetaDataField.COMMENT);
    String track = trackData.getMetaData(CueSheet.MetaDataField.TRACKNUMBER);
    String genre = trackData.getMetaData(CueSheet.MetaDataField.GENRE);

    String targetFileName = String.format ( templateReplacer.replace(targetFileNameTemplate)
                                          , normalizeFileName(title)
                                          , normalizeFileName(artist)
                                          , normalizeFileName(album)
                                          , normalizeFileName(year)
                                          , normalizeFileName(comment)
                                          , normalizeFileName(track)
                                          , normalizeFileName(genre)
                                          );
    
    File targetFile = new File(targetFileName);
    if (!targetFile.isAbsolute())
    {
      targetFile = new File(parentDir, targetFileName);
    }
    targetFile.getParentFile().mkdirs();
    
    String processCommand = String.format ( templateReplacer.replace(processTemplate)
                                          , title
                                          , artist
                                          , album
                                          , year
                                          , comment
                                          , track
                                          , genre
                                          , targetFile
                                          );
    
    createFile  ( audioInputStream
                , processCommand
                , redirectStdOut?targetFile + ".out":null
                , redirectErr?targetFile + ".err":null
                );
  }
  
  private static void createFile  ( AudioInputStream audioInputStream
                                  , String processCommand
                                  , String redirectStdOutFileName
                                  , String redirectErrFileName
                                  ) throws IOException
  {
    OutputStream audioOutputStream = null;
    
    try
    {
      Process process = Runtime.getRuntime().exec(processCommand);
      
      pipeStream(process.getInputStream(), redirectStdOutFileName);
      pipeStream(process.getErrorStream(), redirectErrFileName);
      
      audioOutputStream = new BufferedOutputStream(process.getOutputStream());
      
      AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioOutputStream);
      audioOutputStream.flush();
    }
    finally
    {
      try
      {
        if (audioOutputStream!=null)
        {
          audioOutputStream.close();
        }
      }
      catch (IOException e)
      {
        // Nothing we can do.
      }
    }
  }
  
  /**
   * 
   * @param in
   * @param fileName
   * @throws IOException
   */
  private static void pipeStream(InputStream in, String fileName) throws IOException
  {
    OutputStream out = null;
    if (fileName!=null)
    {
      out = new FileOutputStream(fileName);
    }
    new Thread(new StreamPiper(in, out, true)).start();
  }
  
  /**
   * Get the number of AudioFormat frames represented by the specified Position. Note that an AudioFormat frame may
   * represent a longer or shorter time than a cue sheet frame. 
   * @param position
   * @param audioFileFormat
   * @return
   */
  private static long getAudioFormatFrames(Position position, AudioFormat audioFormat)
  {
    // Determine closest frame number.
    return (long) Math.round(((double) audioFormat.getFrameRate())/75 * position.getTotalFrames());
  }

  /**
   * Normalize the specified file name (without path component) so that it will be likely to be valid on modern
   * file systems and operating systems.
   * @param fileName The file name to normalize. Must not contain a path component.
   * @return The input file name, normalized to be likely to be valid on modern file systems and operating systems.
   */
  private static String normalizeFileName(String fileName)
  {
    StringBuilder builder = new StringBuilder(fileName.length());
    int length = fileName.length();
    for (int index = 0; index < length; index++)
    {
      char currentChar = fileName.charAt(index);
      if (currentChar < 32)
      {
        // No control characters in file name.
        builder.append('_');
      }
      else
      {
        switch (currentChar)
        {
          // No slashes or backslashes.
          case '/':
          case '\\':
            builder.append('_');
            break;
          // Everything else should be okay.
          default:
            builder.append(currentChar);
            break;
        }
      }
    }
    return builder.toString();
  }
  
  /**
   * Get a replacements map for human readable fields to formatting string fields.
   * @return A replacements map for human readable fields to formatting string fields.
   */
  private static Map<String, String> getHumanReadableToFormatStringReplacements()
  {
    HashMap<String, String> replacements = new HashMap<String, String>();
    replacements.put("<title>", "%1$s");
    replacements.put("<artist>", "%2$s");
    replacements.put("<album>", "%3$s");
    replacements.put("<year>", "%4$s");
    replacements.put("<comment>", "%5$s");
    replacements.put("<track>", "%6$s");
    replacements.put("<genre>", "%7$s");
    replacements.put("<targetFile>", "%8$s");
    return replacements;
  }
}
