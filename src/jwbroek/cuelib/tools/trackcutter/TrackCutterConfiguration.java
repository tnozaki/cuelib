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
import jwbroek.cuelib.tools.genrenormalizer.GenreNormalizer;
import jwbroek.util.StringReplacer;
import jwbroek.util.properties.AudioFileFormatTypePropertyHandler;
import jwbroek.util.properties.EnhancedProperties;
import jwbroek.util.properties.FilePropertyHandler;

/**
 * This class represents a configuration for a TrackCutter instance. It takes care of much of the bookkeeping,
 * allowing TrackCutter to focus on his core task.
 * @author jwbroek
 */
public class TrackCutterConfiguration
{
  /**
   * Allowed modes for pregap handling.
   */
  public enum PregapHandling
  {
    PREPEND,
    DISCARD,
    SEPARATE
  };
  
  /**
   * Parent directory for relative paths.
   */
  private File parentDirectory = null;
  /**
   * How to handle pregaps.
   */
  private PregapHandling pregapHandling = PregapHandling.DISCARD;
  /**
   * Only process pregaps with a frame length greater than this.
   */
  private long pregapFrameLengthThreshold = 0;
  /**
   * Audio type to convert to.
   */
  private AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
  /**
   * Whether or not error output from post-processing should be redirected.
   */
  private boolean redirectErr = false;
  /**
   * Whether or not standard output from post-processing should be redirected.
   */
  private boolean redirectStdOut = false;
  /**
   * Whether or not we should do post-processing.
   */
  private boolean doPostProcessing = false;
  /**
   * Whether or not we should redirect output directly to post-processing.
   */
  private boolean redirectToPostprocessing = false;
  /**
   * Template for the file name of the cut tracks.
   */
  private String cutFileNameTemplate = "<artist>_<album>_<track>_<title>.wav";
  /**
   * Template for the file name of the post-processed tracks.
   */
  private String postProcessFileNameTemplate = "<artist>/<album>/<track>_<title>.mp3";
  /**
   * Template for the post-processing command.
   */
  private String postProcessCommandTemplate =
    "C:\\lame\\lame.exe --vbr-new -V 0 -t --tt \"<title>\" --ta \"<artist>\" --tl \"<album>\" --ty \"<year>\""
    + " --tc \"<comment>\" --tn \"<track>\" --tg \"<genre>\" \"<targetFile>\" \"<postProcessFile>\"";
  /**
   * Template for the file name of the cut pregaps.
   */
  private String pregapCutFileNameTemplate = "<artist>_<album>_<track>__pre_<title>.wav";
  /**
   * Template for the file name of the post-processed pregaps.
   */
  private String pregapPostProcessFileNameTemplate = "<artist>/<album>/<track>__pre_<title>.mp3";
  /**
   * Template for the post-processing command for the pregaps.
   */
  private String pregapPostProcessCommandTemplate =
    "C:\\lame\\lame.exe --vbr-new -V 0 -t --tt \"Pregap of <title>\" --ta \"<artist>\" --tl \"<album>\" --ty \"<year>\""
    + " --tc \"Pregap of <title>\" --tn \"<track>\" --tg \"<genre>\" \"<targetFile>\" \"<postProcessFile>\"";
  /**
   * Replacer for the template values.
   */
  private static final StringReplacer templateReplacer =
    new StringReplacer(getHumanReadableToFormatStringReplacements());
  
  /**
   * Create a new TrackCutterConfiguration instance, with default values.
   */
  public TrackCutterConfiguration()
  {
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
    replacements.put("<id3genre>", "%8$s");
    replacements.put("<id31genre>", "%9$s");
    replacements.put("<lamegenre>", "%10$s");
    replacements.put("<cutFile>", "%11$s");
    replacements.put("<postProcessFile>", "%12$s");
    return replacements;
  }
  
  /**
   * <p>Load configuration data from the specified Properties.</p>
   * <p>The following properties are supported. If a value is not specified in the properties file, the
   * configuration for that field is not changed.</p>
   * <table>
   * <tr><th>Property</th><th>Description</th><th>Values</th></tr>
   * <tr><td>parentDirectory</td><td>Parent directory for relative paths.</td><td>Directory path. When not set, a default is used.</td></tr>
   * <tr><td>pregapHandling</td><td>How to handle pregaps.</td><td>{@link TrackCutterConfiguration.PregapHandling}</td></tr>
   * <tr><td>pregapFrameLengthThreshold</td><td>Only process pregaps with a frame length greater than this.</td><td>{@link Long}.</td></tr>
   * <tr><td><targetType/td><td>Audio type to convert to.</td><td>{@link javax.sound.sampled.AudioFileFormat.Type} name.</td></tr>
   * <tr><td>redirectErr</td><td>Whether or not error output from post-processing should be redirected.</td><td>{@link Boolean}.</td></tr>
   * <tr><td>redirectStdOut</td><td>Whether or not standard output from post-processing should be redirected.</td><td>{@link Boolean}.</td></tr>
   * <tr><td>doPostProcessing</td><td>Whether or not we should do post-processing.</td><td>{@link Boolean}.</td></tr>
   * <tr><td>redirectToPostprocessing</td><td>Whether or not we should redirect output directly to post-processing.</td><td>{@link Boolean}.</td></tr>
   * <tr><td>cutFileNameTemplate</td><td>Template for the file name of the cut tracks.</td><td>{@link String}.</td></tr>
   * <tr><td>postProcessFileNameTemplate</td><td>Template for the file name of the post-processed tracks.</td><td>{@link String}.</td></tr>
   * <tr><td>postProcessCommandTemplate</td><td>Template for the post-processing command.</td><td>{@link String}.</td></tr>
   * <tr><td>pregapCutFileNameTemplate</td><td>Template for the file name of the cut pregaps.</td><td>{@link String}.</td></tr>
   * <tr><td>pregapPostProcessFileNameTemplate</td><td>Template for the file name of the post-processed pregaps.</td><td>{@link String}.</td></tr>
   * <tr><td>pregapPostProcessCommandTemplate</td><td>Template for the post-processing command for the pregaps.</td><td>{@link String}.</td></tr>
   * </table>
   * @param properties The Properties to load configuration from.
   */
  public void loadProperties(final EnhancedProperties properties)
  {
    this.parentDirectory = properties.getProperty
      ("parentDirectory", this.parentDirectory, FilePropertyHandler.getInstance());
    this.pregapHandling = properties.getProperty("pregapHandling", this.pregapHandling);
    this.pregapFrameLengthThreshold = properties.getPropertyAsLong
      ("pregapFrameLengthThreshold", this.pregapFrameLengthThreshold);
    this.targetType = properties.getProperty
      ("targetType", this.targetType, AudioFileFormatTypePropertyHandler.getInstance());
    this.redirectErr = properties.getPropertyAsBoolean("redirectErr", this.redirectErr);
    this.redirectStdOut = properties.getPropertyAsBoolean("redirectStdOut", this.redirectStdOut);
    this.doPostProcessing = properties.getPropertyAsBoolean("doPostProcessing", this.doPostProcessing);
    this.redirectToPostprocessing = properties.getPropertyAsBoolean
      ("redirectToPostprocessing", this.redirectToPostprocessing);
    this.cutFileNameTemplate = properties.getProperty("cutFileNameTemplate", this.cutFileNameTemplate);
    this.postProcessFileNameTemplate = properties.getProperty
      ("postProcessFileNameTemplate", this.postProcessFileNameTemplate);
    this.postProcessCommandTemplate = properties.getProperty
      ("postProcessCommandTemplate", this.postProcessCommandTemplate);
    this.pregapCutFileNameTemplate = properties.getProperty
      ("pregapCutFileNameTemplate", this.pregapCutFileNameTemplate);
    this.pregapPostProcessFileNameTemplate = properties.getProperty
      ("pregapPostProcessFileNameTemplate", this.pregapPostProcessFileNameTemplate);
    this.pregapPostProcessCommandTemplate = properties.getProperty
      ("pregapPostProcessCommandTemplate", this.pregapPostProcessCommandTemplate);
  }
  
  /**
   * Get a snapshot of the configuration data stored as in an EnhancedProperties instance. The properties are
   * stored as per {@link #loadProperties(EnhancedProperties)}.
   * @return A snapshot of the configuration data stored as in an EnhancedProperties instance.
   */
  public EnhancedProperties getPropertiesSnapshot()
  {
    final EnhancedProperties properties = new EnhancedProperties();
    
    properties.setProperty("parentDirectory", this.parentDirectory, FilePropertyHandler.getInstance());
    properties.setProperty("pregapHandling", this.pregapHandling);
    properties.setProperty("pregapFrameLengthThreshold", this.pregapFrameLengthThreshold);
    properties.setProperty("targetType", this.targetType, AudioFileFormatTypePropertyHandler.getInstance());
    properties.setProperty("redirectErr", this.redirectErr);
    properties.setProperty("redirectStdOut", this.redirectStdOut);
    properties.setProperty("doPostProcessing", this.doPostProcessing);
    properties.setProperty("redirectToPostprocessing", this.redirectToPostprocessing);
    properties.setProperty("cutFileNameTemplate", this.cutFileNameTemplate);
    properties.setProperty("postProcessFileNameTemplate", this.postProcessFileNameTemplate);
    properties.setProperty("postProcessCommandTemplate", this.postProcessCommandTemplate);
    properties.setProperty("pregapCutFileNameTemplate", this.pregapCutFileNameTemplate);
    properties.setProperty("pregapPostProcessFileNameTemplate", this.pregapPostProcessFileNameTemplate);
    properties.setProperty("pregapPostProcessCommandTemplate", this.pregapPostProcessCommandTemplate);
    return properties;
  }
  
  /**
   * Get a file instance representing the audio file specified in the FileData.
   * @param fileData
   * @return A file instance representing the audio file specified in the FileData.
   */
  public File getAudioFile(final FileData fileData)
  {
    File audioFile = new File(fileData.getFile());
    if (audioFile.getParent()==null)
    {
      audioFile = new File(this.getParentDirectory(), fileData.getFile());
    }
    return audioFile;
  }
  
  /**
   * Normalize the specified file name (without path component) so that it will likely be valid on modern
   * file and operating systems.
   * @param fileName The file name to normalize. Must not contain a path component.
   * @return The input file name, normalized to be likely to be valid on modern file and operating systems.
   */
  private static String normalizeFileName(final String fileName)
  {
    final StringBuilder builder = new StringBuilder(fileName.length());
    final int length = fileName.length();
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
    final String result = builder.toString();
    return result;
  }
  
  /**
   * Get the expanded file from a template and track data.
   * @param trackData TrackData to use for expanding the file name template.
   * @param fileNameTemplate The template for the file name.
   * @return The expanded file.
   */
  public File getFileFromTemplate(final TrackData trackData, final String fileNameTemplate)
  {
    String targetFileName = getExpandedFileName(trackData, fileNameTemplate);
    
    File targetFile = new File(targetFileName);
    if (!targetFile.isAbsolute())
    {
      targetFile = new File(this.getParentDirectory(), targetFileName);
    }
    return targetFile;
  }
  
  /**
   * Get the expanded file name from a template and track data.
   * @param trackData TrackData to use for expanding the file name template.
   * @param fileNameTemplate The template for the file name.
   * @return The expanded file name.
   */
  private String getExpandedFileName(final TrackData trackData, final String fileNameTemplate)
  {
    final String genre = trackData.getMetaData(CueSheet.MetaDataField.GENRE);
    final String result = String.format
      ( this.getTemplateReplacer().replace(fileNameTemplate)
      , normalizeFileName(""+trackData.getMetaData(CueSheet.MetaDataField.TITLE))
      , normalizeFileName(""+trackData.getMetaData(CueSheet.MetaDataField.PERFORMER))
      , normalizeFileName(""+trackData.getMetaData(CueSheet.MetaDataField.ALBUMTITLE))
      , normalizeFileName(""+trackData.getMetaData(CueSheet.MetaDataField.YEAR))
      , normalizeFileName(""+trackData.getMetaData(CueSheet.MetaDataField.COMMENT))
      , normalizeFileName(""+trackData.getMetaData(CueSheet.MetaDataField.TRACKNUMBER))
      , normalizeFileName(""+genre)
      , normalizeFileName(""+GenreNormalizer.normalizeGenreDescription(genre, false, false))
      , normalizeFileName(""+GenreNormalizer.normalizeGenreDescription(genre, true, false))
      , normalizeFileName(""+GenreNormalizer.normalizeGenreDescription(genre, true, true))
      );
    return result;
  }

  /**
   * Get the expanded post-processing command.
   * @param trackData TrackData to use in expanding the template.
   * @param processCommandTemplate Template for the post-processing command
   * @param cutFileName The name of the file of the track that was cut.
   * @param processFileName The file name for after the post-processing step.
   * @return The expanded post-processing command.
   */
  private String getExpandedProcessCommand
    ( final TrackData trackData
    , final String processCommandTemplate
    , final String cutFileName
    , final String processFileName
    )
  {
    final String genre = trackData.getMetaData(CueSheet.MetaDataField.GENRE);
    final String result = String.format
      ( this.getTemplateReplacer().replace(processCommandTemplate)
      , trackData.getMetaData(CueSheet.MetaDataField.TITLE)
      , trackData.getMetaData(CueSheet.MetaDataField.PERFORMER)
      , trackData.getMetaData(CueSheet.MetaDataField.ALBUMTITLE)
      , trackData.getMetaData(CueSheet.MetaDataField.YEAR)
      , trackData.getMetaData(CueSheet.MetaDataField.COMMENT)
      , trackData.getMetaData(CueSheet.MetaDataField.TRACKNUMBER)
      , genre
      , GenreNormalizer.normalizeGenreDescription(genre, false, false)
      , GenreNormalizer.normalizeGenreDescription(genre, true, false)
      , GenreNormalizer.normalizeGenreDescription(genre, true, true)
      , cutFileName
      , processFileName
      );
    return result;
  }
  
  /**
   * Get a File instance representing the file after cutting the track.
   * @param processAction The associated processing action.
   * @return A File instance representing the file after cutting the track.
   */
  public File getCutFile(final TrackCutterProcessingAction processAction)
  {
    TrackData trackData = processAction.getTrackData();
    
    String fileNameTemplate =
      processAction.getIsPregap()?this.getPregapCutFileNameTemplate():this.getCutFileNameTemplate();
    
    File result = getFileFromTemplate(trackData, fileNameTemplate);
    return result;
  }
  
  /**
   * Get a File instance representing the file after post-processing.
   * @param processAction The associated processing action.
   * @return A File instance representing the file after post-processing.
   */
  public File getPostProcessFile(final TrackCutterProcessingAction processAction)
  {
    TrackData trackData = processAction.getTrackData();
    
    String fileNameTemplate =
      processAction.getIsPregap()?this.getPregapPostProcessFileNameTemplate():this.getPostProcessFileNameTemplate();
    
    File result = getFileFromTemplate(trackData, fileNameTemplate);
    return result;
  }

  /**
   * Get the command to use for post-processing.
   * @param processAction The associated processing action.
   * @return The command to use for post-processing.
   */
  public String getPostProcessCommand(final TrackCutterProcessingAction processAction)
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
   * Get the replacer for the template values.
   * @return The replacer for the template values.
   */
  private StringReplacer getTemplateReplacer()
  {
    return TrackCutterConfiguration.templateReplacer;
  }

  /**
   * Get the parent directory for relative paths.
   * @return The parent directory for relative paths.
   */
  public File getParentDirectory()
  {
    return this.parentDirectory;
  }

  /**
   * Set the parent directory for relative paths.
   * @param parentDirectory The parent directory for relative paths.
   */
  public void setParentDirectory(final File parentDirectory)
  {
    this.parentDirectory = parentDirectory;
  }

  /**
   * Get the template for the file name of the tracks after cutting.
   * @return The template for the file name of the tracks after cutting.
   */
  public String getCutFileNameTemplate()
  {
    return this.cutFileNameTemplate;
  }

  /**
   * Set the template for the file name of the tracks after cutting.
   * @param targetFileNameTemplate The template for the file name of the tracks after cutting.
   */
  public void setCutFileNameTemplate(final String targetFileNameTemplate)
  {
    this.cutFileNameTemplate = targetFileNameTemplate;
  }

  /**
   * Get the template for the file name of the post-processed tracks.
   * @return The template for the file name of the post-processed tracks.
   */
  public String getPostProcessFileNameTemplate()
  {
    return this.postProcessFileNameTemplate;
  }

  /**
   * Set the template for the file name of the post-processed tracks.
   * @param postProcessFileNameTemplate The template for the file name of the post-processed tracks.
   */
  public void setPostProcessFileNameTemplate(String postProcessFileNameTemplate)
  {
    this.postProcessFileNameTemplate = postProcessFileNameTemplate;
  }
  
  /**
   * Get the template for the command for post-processing tracks.
   * @return The template for the command for post-processing tracks.
   */
  public String getPostProcessCommandTemplate()
  {
    return this.postProcessCommandTemplate;
  }

  /**
   * Set the template for the command for post-processing tracks.
   * @param postProcessCommandTemplate The template for the command for post-processing tracks.
   */
  public void setPostProcessCommandTemplate(final String postProcessCommandTemplate)
  {
    this.postProcessCommandTemplate = postProcessCommandTemplate;
  }
  
  /**
   * Get the mode for pregap handling.
   * @return The mode for pregap handling.
   */
  public PregapHandling getPregapHandling()
  {
    return this.pregapHandling;
  }

  /**
   * Set the mode for pregap handling.
   * @param pregapHandling The mode for pregap handling.
   */
  public void setPregapHandling(final PregapHandling pregapHandling)
  {
    this.pregapHandling = pregapHandling;
  }

  /**
   * Get whether or not error output from post-processing should be redirected. 
   * @return the redirectErr Whether or not error output from post-processing should be redirected.
   */
  public boolean getRedirectErr()
  {
    return this.redirectErr;
  }

  /**
   * Set whether or not error output from post-processing should be redirected. 
   * @param redirectErr Whether or not error output from post-processing should be redirected.
   */
  public void setRedirectErr(final boolean redirectErr)
  {
    this.redirectErr = redirectErr;
  }

  /**
   * Get whether or not standard output from post-processing should be redirected. 
   * @return the redirectErr Whether or not standard output from post-processing should be redirected.
   */
  public boolean getRedirectStdOut()
  {
    return this.redirectStdOut;
  }

  /**
   * Set whether or not standard output from post-processing should be redirected. 
   * @param redirectStdOut Whether or not standard output from post-processing should be redirected.
   */
  public void setRedirectStdOut(final boolean redirectStdOut)
  {
    this.redirectStdOut = redirectStdOut;
  }

  /**
   * Get the audio type to convert to.
   * @return The audio type to convert to.
   */
  public AudioFileFormat.Type getTargetType()
  {
    return this.targetType;
  }

  /**
   * Set the audio type to convert to.
   * @param targetType The audio type to convert to.
   */
  public void setTargetType(final AudioFileFormat.Type targetType)
  {
    this.targetType = targetType;
  }

  /**
   * Get whether or not to do post-processing.
   * @return Whether or not to do post-processing.
   */
  public boolean getDoPostProcessing()
  {
    return this.doPostProcessing;
  }

  /**
   * Set whether or not to do post-processing.
   * @param doPostProcessing Whether or not to do post-processing.
   */
  public void setDoPostProcessing(final boolean doPostProcessing)
  {
    this.doPostProcessing = doPostProcessing;
  }

  /**
   * Get whether or not to redirect the cut track directly to post-processing.
   * @return Whether or not to redirect the cut track directly to post-processing.
   */
  public boolean getRedirectToPostprocessing()
  {
    return this.redirectToPostprocessing;
  }

  /**
   * Set whether or not to redirect the cut track directly to post-processing.
   * @param redirectToPostprocessing Whether or not to redirect the cut track directly to post-processing.
   */
  public void setRedirectToPostprocessing(final boolean redirectToPostprocessing)
  {
    this.redirectToPostprocessing = redirectToPostprocessing;
  }

  /**
   * Get the template for the file name of the pregap tracks after cutting.
   * @return The template for the file name of the tracks after cutting.
   */
  public String getPregapCutFileNameTemplate()
  {
    return this.pregapCutFileNameTemplate;
  }

  /**
   * Set the template for the file name of the pregap tracks after cutting.
   * @param pregapTargetFileNameTemplate The template for the file name of the pregap tracks after cutting.
   */
  public void setPregapCutFileNameTemplate(final String pregapTargetFileNameTemplate)
  {
    this.pregapCutFileNameTemplate = pregapTargetFileNameTemplate;
  }

  /**
   * Get the template for the file name of the post-processed tracks.
   * @return The template for the file name of the post-processed tracks.
   */
  public String getPregapPostProcessFileNameTemplate()
  {
    return this.pregapPostProcessFileNameTemplate;
  }

  /**
   * Set the template for the file name of the post-processed tracks.
   * @param pregapPostProcessFileNameTemplate The template for the file name of the post-processed tracks.
   */
  public void setPregapPostProcessFileNameTemplate(final String pregapPostProcessFileNameTemplate)
  {
    this.pregapPostProcessFileNameTemplate = pregapPostProcessFileNameTemplate;
  }
  
  /**
   * Get the template for the command for post-processing tracks.
   * @return The template for the command for post-processing tracks.
   */
  public String getPregapPostProcessCommandTemplate()
  {
    return this.pregapPostProcessCommandTemplate;
  }

  /**
   * Set the template for the command for post-processing tracks.
   * @param pregapPostProcessCommandTemplate The template for the command for post-processing tracks.
   */
  public void setPregapPostProcessCommandTemplate(final String pregapPostProcessCommandTemplate)
  {
    this.pregapPostProcessCommandTemplate = pregapPostProcessCommandTemplate;
  }

  /**
   * Get the threshold on pregaps in frame length. Pregaps shorter than this will not be processed.
   * @return The threshold on pregaps in frame length.
   */
  public long getPregapFrameLengthThreshold()
  {
    return this.pregapFrameLengthThreshold;
  }

  /**
   * Set the threshold on pregaps in frame length. Pregaps shorter than this will not be processed.
   * @param pregapFrameLengthThreshold The threshold on pregaps in frame length.
   */
  public void setPregapFrameLengthThreshold(final long pregapFrameLengthThreshold)
  {
    this.pregapFrameLengthThreshold = pregapFrameLengthThreshold;
  }
}
