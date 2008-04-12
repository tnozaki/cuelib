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
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;

import jwbroek.cuelib.CueSheet;
import jwbroek.cuelib.FileData;
import jwbroek.cuelib.TrackData;
import jwbroek.cuelib.tools.trackcutter.TrackCutter.PregapHandling;
import jwbroek.util.StringReplacer;

/**
 * This class represents a configuration for a TrackCutter instance. It takes care of much of the bookkeeping,
 * allowing TrackCutter to focus on his real tasks.
 * @author jwbroek
 */
public class TrackCutterConfiguration
{
  private File parentDirectory = null;
  private PregapHandling pregapHandling = PregapHandling.DISCARD;
  private AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
  private boolean redirectErr = false;
  private boolean redirectStdOut = false;
  private boolean doPostProcessing = false;
  private boolean redirectToPostprocessing = false;
  private String cutFileNameTemplate = "<artist>_<album>_<track>_<title>.wav";
  private String postProcessFileNameTemplate = "<artist>/<album>/<track>_<title>.mp3";
  private String postProcessCommandTemplate =
    "C:\\lame\\lame.exe --vbr-new -V 0 -t --tt \"<title>\" --ta \"<artist>\" --tl \"<album>\" --ty \"<year>\""
    + " --tc \"<comment>\" --tn \"<track>\" --tg \"<genre>\" \"<targetFile>\" \"<postProcessFile>\"";
  private String pregapCutFileNameTemplate = "<artist>_<album>_<track>__pre_<title>.wav";
  private String pregapPostProcessFileNameTemplate = "<artist>/<album>/<track>__pre_<title>.mp3";
  private String pregapPostProcessCommandTemplate =
    "C:\\lame\\lame.exe --vbr-new -V 0 -t --tt \"Pregap of <title>\" --ta \"<artist>\" --tl \"<album>\" --ty \"<year>\""
    + " --tc \"Pregap of <title>\" --tn \"<track>\" --tg \"<genre>\" \"<targetFile>\" \"<postProcessFile>\"";
  
  public static final StringReplacer templateReplacer =
    new StringReplacer(getHumanReadableToFormatStringReplacements());
  
  public TrackCutterConfiguration()
  {
    // Intentionally empty.
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
    replacements.put("<cutFile>", "%8$s");
    replacements.put("<postProcessFile>", "%9$s");
    return replacements;
  }
  
  /**
   * 
   * @param fileData
   * @return
   */
  public File getAudioFile(FileData fileData)
  {
    File audioFile = new File(fileData.getFile());
    if (audioFile.getParent()==null)
    {
      audioFile = new File(this.getParentDirectory(), fileData.getFile());
    }
    return audioFile;
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
          // These characters are likely to be troublesome in file names.
          case '/':
          case '\\':
          case ':':
          case '*':
          case '?':
          case '"':
          case '|':
            builder.append('_');
            break;
          // Everything else should be okay on modern file system.
          default:
            builder.append(currentChar);
            break;
        }
      }
    }
    return builder.toString();
  }
  
  private String getExpandedFileName(TrackData trackData, String fileNameTemplate)
  {
    return String.format
      ( this.getTemplateReplacer().replace(fileNameTemplate)
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.TITLE))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.PERFORMER))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.ALBUMTITLE))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.YEAR))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.COMMENT))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.TRACKNUMBER))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.GENRE))
      );
  }

  private String getExpandedProcessCommand
    ( TrackData trackData
    , String processCommandTemplate
    , String intermediateFileName
    , String processFileName
    )
  {
    return String.format
      ( this.getTemplateReplacer().replace(processCommandTemplate)
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.TITLE))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.PERFORMER))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.ALBUMTITLE))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.YEAR))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.COMMENT))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.TRACKNUMBER))
      , normalizeFileName(trackData.getMetaData(CueSheet.MetaDataField.GENRE))
      , intermediateFileName
      , processFileName
      );
  }
  
  public File getFileFromTemplate(TrackData trackData, String fileNameTemplate)
  {
    String targetFileName = getExpandedFileName(trackData, fileNameTemplate);
    
    File targetFile = new File(targetFileName);
    if (!targetFile.isAbsolute())
    {
      targetFile = new File(this.getParentDirectory(), targetFileName);
    }
    
    return targetFile;
  }

  public File getCutFile(TrackCutterProcessingAction processAction)
  {
    TrackData trackData = processAction.getTrackData();
    
    String fileNameTemplate =
      processAction.getIsPregap()?this.getPregapCutFileNameTemplate():this.getCutFileNameTemplate();
    
    return getFileFromTemplate(trackData, fileNameTemplate);
  }
  
  public File getPostProcessFile(TrackCutterProcessingAction processAction)
  {
    TrackData trackData = processAction.getTrackData();
    
    String fileNameTemplate =
      processAction.getIsPregap()?this.getPregapPostProcessFileNameTemplate():this.getPostProcessFileNameTemplate();
    
    return getFileFromTemplate(trackData, fileNameTemplate);
  }

  public String getPostProcessCommand(TrackCutterProcessingAction processAction)
  {
    TrackData trackData = processAction.getTrackData();
    String commandTemplate =
      processAction.getIsPregap()?this.getPregapPostProcessCommandTemplate():this.getPostProcessCommandTemplate();
    String processCommand = getExpandedProcessCommand
      ( trackData
      , commandTemplate
      , processAction.getCutFile().getPath()
      , processAction.getPostProcessFile().getPath()
      );
    
    return processCommand;
  }
  
  /**
   * @return the templateReplacer
   */
  public StringReplacer getTemplateReplacer()
  {
    return TrackCutterConfiguration.templateReplacer;
  }

  /**
   * @return the parentDirectory
   */
  public File getParentDirectory()
  {
    return parentDirectory;
  }

  /**
   * @param parentDirectory the parentDirectory to set
   */
  public void setParentDirectory(File parentDirectory)
  {
    this.parentDirectory = parentDirectory;
  }

  /**
   * @return the postProcessCommandTemplate
   */
  public String getPostProcessCommandTemplate()
  {
    return postProcessCommandTemplate;
  }

  /**
   * @param processCommandTemplate the postProcessCommandTemplate to set
   */
  public void setPostProcessCommandTemplate(String postProcessCommandTemplate)
  {
    this.postProcessCommandTemplate = postProcessCommandTemplate;
  }

  /**
   * @return the postProcessFileNameTemplate
   */
  public String getPostProcessFileNameTemplate()
  {
    return postProcessFileNameTemplate;
  }

  /**
   * @param postProcessFileNameTemplate the postProcessFileNameTemplate to set
   */
  public void setPostProcessFileNameTemplate(String postProcessFileNameTemplate)
  {
    this.postProcessFileNameTemplate = postProcessFileNameTemplate;
  }

  /**
   * @return the pregapHandling
   */
  public PregapHandling getPregapHandling()
  {
    return pregapHandling;
  }

  /**
   * @param pregapHandling the pregapHandling to set
   */
  public void setPregapHandling(PregapHandling pregapHandling)
  {
    this.pregapHandling = pregapHandling;
  }

  /**
   * @return the redirectErr
   */
  public boolean getRedirectErr()
  {
    return redirectErr;
  }

  /**
   * @param redirectErr the redirectErr to set
   */
  public void setRedirectErr(boolean redirectErr)
  {
    this.redirectErr = redirectErr;
  }

  /**
   * @return the redirectStdOut
   */
  public boolean getRedirectStdOut()
  {
    return redirectStdOut;
  }

  /**
   * @param redirectStdOut the redirectStdOut to set
   */
  public void setRedirectStdOut(boolean redirectStdOut)
  {
    this.redirectStdOut = redirectStdOut;
  }

  /**
   * @return the cutFileNameTemplate
   */
  public String getCutFileNameTemplate()
  {
    return cutFileNameTemplate;
  }

  /**
   * @param cutFileNameTemplate the cutFileNameTemplate to set
   */
  public void setCutFileNameTemplate(String targetFileNameTemplate)
  {
    this.cutFileNameTemplate = targetFileNameTemplate;
  }

  /**
   * @return the targetType
   */
  public AudioFileFormat.Type getTargetType()
  {
    return targetType;
  }

  /**
   * @param targetType the targetType to set
   */
  public void setTargetType(AudioFileFormat.Type targetType)
  {
    this.targetType = targetType;
  }

  /**
   * @return the doPostProcessing
   */
  public boolean getDoPostProcessing()
  {
    return doPostProcessing;
  }

  /**
   * @param doPostProcessing the doPostProcessing to set
   */
  public void setDoPostProcessing(boolean doPostProcessing)
  {
    this.doPostProcessing = doPostProcessing;
  }

  /**
   * @return the redirectToPostprocessing
   */
  public boolean getRedirectToPostprocessing()
  {
    return redirectToPostprocessing;
  }

  /**
   * @param redirectToPostprocessing the redirectToPostprocessing to set
   */
  public void setRedirectToPostprocessing(boolean redirectToPostprocessing)
  {
    this.redirectToPostprocessing = redirectToPostprocessing;
  }


  /**
   * @return the pregapPostProcessCommandTemplate
   */
  public String getPregapPostProcessCommandTemplate()
  {
    return pregapPostProcessCommandTemplate;
  }

  /**
   * @param pregapPostProcessCommandTemplate the pregapPostProcessCommandTemplate to set
   */
  public void setPregapPostProcessCommandTemplate(
      String pregapPostProcessCommandTemplate)
  {
    this.pregapPostProcessCommandTemplate = pregapPostProcessCommandTemplate;
  }

  /**
   * @return the pregapPostProcessFileNameTemplate
   */
  public String getPregapPostProcessFileNameTemplate()
  {
    return pregapPostProcessFileNameTemplate;
  }

  /**
   * @param pregapPostProcessFileNameTemplate the pregapPostProcessFileNameTemplate to set
   */
  public void setPregapPostProcessFileNameTemplate(
      String pregapPostProcessFileNameTemplate)
  {
    this.pregapPostProcessFileNameTemplate = pregapPostProcessFileNameTemplate;
  }

  /**
   * @return the pregapCutFileNameTemplate
   */
  public String getPregapCutFileNameTemplate()
  {
    return pregapCutFileNameTemplate;
  }

  /**
   * @param pregapCutFileNameTemplate the pregapCutFileNameTemplate to set
   */
  public void setPregapCutFileNameTemplate(String pregapTargetFileNameTemplate)
  {
    this.pregapCutFileNameTemplate = pregapTargetFileNameTemplate;
  }
}
