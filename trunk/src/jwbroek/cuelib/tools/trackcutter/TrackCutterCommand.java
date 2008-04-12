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

import javax.sound.sampled.AudioFileFormat;

import jwbroek.cuelib.tools.trackcutter.TrackCutter.PregapHandling;
import jwbroek.util.SimpleOptionsParser;

public class TrackCutterCommand
{
  private TrackCutterConfiguration configuration= new TrackCutterConfiguration();
  
  private TrackCutterCommand()
  {
    // No need to instantiate from outside this class.
  }
  
  private static void printHelp()
  {
    System.out.println("Syntax: [options] cuefiles");
    System.out.println("Options:");
    System.out.println(" -f file             Template for file name. Implies no redirect to post-processing.");
    System.out.println(" -t type             Audio type.");
    System.out.println(" -p file command     Template for post-processing file name and command.");
    System.out.println(" -g type [templates] Pregap handling. Choose from \"prepend\", \"discard\", \"separate\"");
    System.out.println("                     If \"separate\" is chosen, you must also specify templates for file");
    System.out.println("                     name, command, and name for intermediate file. The latter options will");
    System.out.println("                     not be used if redirect is enabled for post-processing. It must still be");
    System.out.println("                     specified.");
    System.out.println(" -s                  Redirect audio to post-processing step.");
    System.out.println(" -ro                 Redirect output of post-processing step to log file.");
    System.out.println(" -re                 Redirect error output of post-processing step to log file.");
    System.out.println("Templates: ");
  }
  
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
            else if ("WAV".equalsIgnoreCase(type))
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
  
  public void processCommand(String [] args)
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
   * @param args
   */
  public static void main(String[] args)
  {
    new TrackCutterCommand().processCommand(args);
  }

  /**
   * @return the configuration
   */
  private TrackCutterConfiguration getConfiguration()
  {
    return this.configuration;
  }
}
