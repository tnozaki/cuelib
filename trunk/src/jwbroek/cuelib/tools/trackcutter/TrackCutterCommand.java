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
package jwbroek.cuelib.tools.trackcutter;

import java.io.File;
import java.util.Scanner;

import javax.sound.sampled.AudioFileFormat;

import jwbroek.cuelib.Position;
import jwbroek.cuelib.tools.trackcutter.TrackCutterConfiguration.PregapHandling;
import jwbroek.util.SimpleOptionsParser;

/**
 * Command line interface for TrackCutter.
 * @author jwbroek
 */
public class TrackCutterCommand
{
  /**
   * The configuration for the TrackCutter. Will be modified based on the command line arguments.
   */
  private TrackCutterConfiguration configuration= new TrackCutterConfiguration();
  
  /**
   * Create a new TrackCutterCommand instance. 
   */
  private TrackCutterCommand()
  {
    // No need to instantiate from outside this class.
  }
  
  /**
   * Print a help message.
   */
  private static void printHelp()
  {
    System.out.println("Syntax: [options] cuefiles");
    System.out.println("Options:");
    System.out.println(" -f file             Template for file name. Implies no redirect to post-processing.");
    System.out.println(" -t type             Audio type to convert to. Valid types are AIFC, AIFF, AU, SND, WAVE.");
    System.out.println("                     Not all conversions may be supported.");
    System.out.println(" -p file command     Template for post-processing file name and command.");
    System.out.println(" -g type [templates] Pregap handling. Choose from \"prepend\", \"discard\", \"separate\"");
    System.out.println("                     If \"separate\" is chosen, you must also specify templates for file");
    System.out.println("                     name, command, and name for intermediate file. The latter options will");
    System.out.println("                     not be used if redirect is enabled for post-processing. It must still be");
    System.out.println("                     specified.");
    System.out.println(" -pt length          Threshold for pregap processing. Pregaps with length shorter than this");
    System.out.println("                     will not be processed. Length as per the position field in cue sheets.");
    System.out.println(" -s                  Redirect audio to post-processing step.");
    System.out.println(" -ro                 Redirect output of post-processing step to log file.");
    System.out.println(" -re                 Redirect error output of post-processing step to log file.");
    System.out.println("Templates:");
    System.out.println(" <title>             Title of the track.");
    System.out.println(" <artist>            Artist of the track, or artist of the album, if unknown.");
    System.out.println(" <album>             Title of the album");
    System.out.println(" <year>              Year of the track, or year of the album, if unknown.");
    System.out.println(" <comment>           Comment of the album.");
    System.out.println(" <track>             Track number");
    System.out.println(" <genre>             Genre of the album.");
    System.out.println(" <cutFile>           Name of the file after cutting. Can only be used in the post-");
    System.out.println("                     processing command template.");
    System.out.println(" <postProcessFile>   Name of the file after post-processing. Can only be used in");
    System.out.println("                     the post-processing command template.");
    System.out.println("Examples:");
    System.out.println(" Cut the tracks in a cue sheet:");
    System.out.println("  \"c:\\tmp\\Skunk Anansie - Stoosh.cue\"");
    System.out.println(" Cut the tracks in a cue sheet and prepend the pregaps:");
    System.out.println("  -p prepend \"c:\\tmp\\Skunk Anansie - Stoosh.cue\"");
    System.out.println(" Cut the tracks in a cue sheet and prepend pregaps longer than 3 seconds:");
    System.out.println("  -p prepend -pt 00:02:00 \"c:\\tmp\\Skunk Anansie - Stoosh.cue\"");
    System.out.println(" Cut the tracks in a cue sheet and give them names based on the data in the sheet:");
    System.out.println("  -f \"<artist>\\<album>\\<track>_<title>.wav\" \"c:\\tmp\\Skunk Anansie - Stoosh.cue\"");
    System.out.println(" Cut the tracks with separate pregaps, convert to WAV format, and redirect to lame while");
    System.out.println(" setting the appropriate ID3 tags, and creating log files:");
    System.out.println("  -f \"waves\\<artist>\\<album>\\<track>_<title>.wav\"");
    System.out.println("  -p \"mp3\\<artist>\\<album>\\<track>_<title>.mp3\"");
    System.out.println("  \"C:\\lame\\lame.exe --vbr-new -V 0 -t --tt \\\"<title>\\\" --ta \\\"<artist>\\\" --tl \\\"<album>\\\"");
    System.out.println("  --ty \\\"<year>\\\" --tc \\\"<comment>\\\" --tn \\\"<track>\\\" --tg \\\"<genre>\\\" - ");
    System.out.println("  \\\"<postProcessFile>\\\"\"");
    System.out.println("  -g separate \"mp3\\<artist>\\<album>\\<track>_0_<title>.mp3\"");
    System.out.println("  \"C:\\lame\\lame.exe --vbr-new -V 0 -t --tt \\\"00 Pregap of <title>\\\" --ta \\\"<artist>\\\"");
    System.out.println("  --tl \\\"<album>\\\" --ty \\\"<year>\\\" --tc \\\"Pregap of <title>\\\" --tn \\\"<track>\\\" --tg");
    System.out.println("  \\\"<genre>\\\" - \\\"<postProcessFile>\\\"\"");
    System.out.println("  -s -ro -re -t WAVE \"c:\\tmp\\Skunk Anansie - Stoosh.cue\"");
  }
  
  /**
   * Get a configurated parser for the command line arguments.
   * @return A configurated parser for the command line arguments.
   */
  private SimpleOptionsParser getArgumentsParser()
  {
    SimpleOptionsParser argumentsParser = new SimpleOptionsParser();
    argumentsParser.registerOption
      ( "-f"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Create a target file. This implies no streaming to the postprocessor.
            TrackCutterCommand.this.getConfiguration().setRedirectToPostprocessing(false);
            TrackCutterCommand.this.getConfiguration().setCutFileNameTemplate(options[offset+1]);
            return offset+2;
          }
        }
      );
    argumentsParser.registerOption
      ( "-t"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            String type = options[offset+1];
            AudioFileFormat.Type audioType = null;
            if ("AIFC".equalsIgnoreCase(type))
            {
              audioType = AudioFileFormat.Type.AIFC;
            }
            else if ("AIFF".equalsIgnoreCase(type))
            {
              audioType = AudioFileFormat.Type.AIFF;
            }
            else if ("AU".equalsIgnoreCase(type))
            {
              audioType = AudioFileFormat.Type.AU;
            }
            else if ("SND".equalsIgnoreCase(type))
            {
              audioType = AudioFileFormat.Type.SND;
            }
            else if ("WAVE".equalsIgnoreCase(type))
            {
              audioType = AudioFileFormat.Type.WAVE;
            }
            else
            {
              throw new IllegalArgumentException("Unsupported audio type: " + type);
            }
            // Set target audio type.
            TrackCutterCommand.this.getConfiguration().setTargetType(audioType);
            return offset+2;
          }
        }
      );
    argumentsParser.registerOption
      ( "-p"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Create a postprocessing command. This implies that we do postprocessing.
            TrackCutterCommand.this.getConfiguration().setDoPostProcessing(true);
            TrackCutterCommand.this.getConfiguration().setPostProcessFileNameTemplate(options[offset+1]);
            TrackCutterCommand.this.getConfiguration().setPostProcessCommandTemplate(options[offset+2]);
            return offset+3;
          }
        }
      );
    argumentsParser.registerOption
      ( "-g"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Pregap handling
            if ("prepend".equalsIgnoreCase(options[offset + 1]))
            {
              TrackCutterCommand.this.getConfiguration().setPregapHandling(PregapHandling.PREPEND);
              return offset+2;
            }
            else if ("discard".equalsIgnoreCase(options[offset + 1]))
            {
              TrackCutterCommand.this.getConfiguration().setPregapHandling(PregapHandling.DISCARD);
              return offset+2;
            }
            else if ("separate".equalsIgnoreCase(options[offset + 1]))
            {
              TrackCutterCommand.this.getConfiguration().setPregapHandling(PregapHandling.SEPARATE);
              TrackCutterCommand.this.getConfiguration().setPregapPostProcessFileNameTemplate(options[offset + 2]);
              TrackCutterCommand.this.getConfiguration().setPregapPostProcessCommandTemplate(options[offset + 3]);
              TrackCutterCommand.this.getConfiguration().setPregapCutFileNameTemplate(options[offset + 4]);
              return offset+5;
            }
            else
            {
              throw new IllegalArgumentException("Invalid type for pregap handling: " + options[1]);
            }
          }
        }
      );
    argumentsParser.registerOption
      ( "-s"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Stream to postprocessor
            TrackCutterCommand.this.getConfiguration().setRedirectToPostprocessing(true);
            return offset+1;
          }
        }
      );
    argumentsParser.registerOption
      ( "-pt"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Set frame length threshold for pregap handling.
            Scanner scanner = new Scanner(options[offset+1]).useDelimiter(":");
            Position thresholdPosition = new Position(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
            scanner.close();
            TrackCutterCommand.this.getConfiguration().setPregapFrameLengthThreshold(thresholdPosition.getTotalFrames());
            return offset+2;
          }
        }
      );
    argumentsParser.registerOption
      ( "-ro"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Redirect standard out.
            TrackCutterCommand.this.getConfiguration().setRedirectStdOut(true);
            return offset+1;
          }
        }
      );
    argumentsParser.registerOption
      ( "-re"
      , new SimpleOptionsParser.OptionHandler()
        {
          public int handleOption(String [] options, int offset)
          {
            // Redirect err.
            TrackCutterCommand.this.getConfiguration().setRedirectErr(true);
            return offset+1;
          }
        }
      );
    
    return argumentsParser;
  }
  
  /**
   * Process based on the provided command line arguments.
   * @param args The command line arguments.
   */
  public void performProcessing(final String [] args)
  {
    TrackCutter cutter = new TrackCutter(this.getConfiguration());
    SimpleOptionsParser argumentsParser = getArgumentsParser();
    
    int firstFileIndex = argumentsParser.parseOptions(args);
    
    if (firstFileIndex == -1 || firstFileIndex == args.length)
    {
      // Something went wrong, or no files were specified.
      printHelp();
      return;
    }
    
    // Process all files in turn.
    for (int fileIndex = firstFileIndex; fileIndex < args.length; fileIndex++)
    {
      File cueFile = new File(args[fileIndex]);
      
      try
      {
        cutter.cutTracksInCueSheet(cueFile);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Entry-point.
   * @param args Command line arguments.
   */
  public static void main(final String[] args)
  {
    new TrackCutterCommand().performProcessing(args);
  }

  /**
   * Get the configuration for the TrackCutter.
   * @return The configuration for the TrackCutter.
   */
  private TrackCutterConfiguration getConfiguration()
  {
    return this.configuration;
  }
}
