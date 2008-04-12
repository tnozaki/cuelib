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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import jwbroek.cuelib.CueParser;
import jwbroek.cuelib.CueSheet;
import jwbroek.cuelib.FileData;
import jwbroek.cuelib.Position;
import jwbroek.cuelib.TrackData;
import jwbroek.io.StreamPiper;

/**
 * <p>Class that can cut up files into tracks, based on the information provided by a cue sheet.</p>
 * <p>It can do some audio type conversions, file naming based on information in the cue sheet, and
 * offers the option of having the tracks post-processed by a another application based on information
 * in the cue sheet.</p>
 * @author jwbroek
 */
public class TrackCutter
{
  /**
   * Configuation for the TrackCutter.
   */
  private TrackCutterConfiguration configuration;
  
  /**
   * Create a new TrackCutter instance, based on the configuration provided.
   * @param configuration
   */
  public TrackCutter(TrackCutterConfiguration configuration)
  {
    this.configuration = configuration;
  }
  
  /**
   * Cut the the files specified in the cue sheet into tracks.
   * @param cueFile
   * @throws IOException
   */
  public void cutTracksInCueSheet(final File cueFile) throws IOException
  {
    CueSheet cueSheet = null;
    
    // If no parent directory specified, then set the parent directory of the cue file.
    if (getConfiguration().getParentDirectory()==null)
    {
      getConfiguration().setParentDirectory(cueFile.getParentFile());
    }
    
    try
    {
      cueSheet = CueParser.parse(cueFile);
    }
    catch (IOException e)
    {
      throw new IOException("Problem parsing cue file.", e);
    }
    
    cutTracksInCueSheet(cueSheet);
  }
  
  /**
   * Cut the the files specified in the cue sheet into tracks.
   * @param cueSheet
   * @throws IOException
   */
  public void cutTracksInCueSheet(final CueSheet cueSheet) throws IOException
  {
    // We can process each file in the cue sheet independently.
    for (FileData fileData : cueSheet.getFileData())
    {
      try
      {
        cutTracksInFileData(fileData);
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
  
  /**
   * Cut the the files specified in the FileData into tracks.
   * @param fileData
   * @throws IOException
   * @throws UnsupportedAudioFileException
   */
  private void cutTracksInFileData(final FileData fileData)
    throws IOException, UnsupportedAudioFileException
  {
    AudioInputStream audioInputStream = null;
    
    try
    {
      // Determine the complete path to the audio file.
      File audioFile = getConfiguration().getAudioFile(fileData);
      
      // Open the audio file.
      // Sadly, we can't do much with the file type information from the cue sheet, as javax.sound.sampled
      // needs more information before it can process a specific type of sound file. Best then to let it
      // determine all aspects of the audio type by itself.
      audioInputStream = AudioSystem.getAudioInputStream(audioFile);
      
      // Current position in terms of the frames as per audioInputStream.getFrameLength().
      // Note that these frames need not be equal to cue sheet frames.
      long currentAudioFramePos = 0;
      
      // Process tracks.
      for (TrackCutterProcessingAction processAction : getProcessActionList(fileData))
      {
        currentAudioFramePos = performProcessAction
          ( processAction
          , audioInputStream
          , currentAudioFramePos
          );
      }
    }
    finally
    {
      if (audioInputStream!=null)
      {
        // Don't handle exceptions, as there's really nothing we can do about them.
        audioInputStream.close();
      }
    }
  }
  
  /**
   * Get a list of ProcessActions based on the specified FileData.
   * @param fileData
   * @return A list of ProcessActions based on the specified FileData.
   */
  private List<TrackCutterProcessingAction> getProcessActionList(final FileData fileData)
  {
    List<TrackCutterProcessingAction> result = new ArrayList<TrackCutterProcessingAction>();
    TrackData previousTrackData = null;
    
    // Process all tracks in turn.
    for (TrackData currentTrackData : fileData.getTrackData())
    {
      if (previousTrackData != null)
      {
        if (currentTrackData.getIndex(0) != null)
        {
          addProcessActions(previousTrackData, currentTrackData.getIndex(0).getPosition(), result);
        }
        else
        {
          addProcessActions(previousTrackData, currentTrackData.getIndex(1).getPosition(), result);
        }
      }
      previousTrackData = currentTrackData;
    }
    
    // Handle last track, if any.
    if (previousTrackData != null)
    {
      addProcessActions(previousTrackData, null, result);
    }
    
    return result;
  }
  
  /**
   * Add ProcesAction instances for the specified TrackData.
   * @param trackData
   * @param nextPosition The first position after the current track, or null if there is no next position.
   * (Track continues until the end of data.) 
   * @param processActions A list of ProcessAction instances to which the actions for this TrackData
   * will be added.
   */
  private void addProcessActions
    ( final TrackData trackData
    , final Position nextPosition
    , final List<TrackCutterProcessingAction> processActions
    )
  {
    if (trackData.getIndex(0) == null)
    {
      // No pregap to handle. Just process this track.
      processActions.add(new TrackCutterProcessingAction(trackData.getIndex(1).getPosition(), nextPosition, trackData, false, getConfiguration()));
    }
    else
    {
      switch (configuration.getPregapHandling())
      {
        case DISCARD:
          // Discard the pregap, process the track.
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(1).getPosition(), nextPosition, trackData, false, getConfiguration()));
          break;
        case PREPEND:
          // Prepend the pregap.
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(0).getPosition(), nextPosition, trackData, false, getConfiguration()));
          break;
        case SEPARATE:
          // Add pregap and track as separate tracks.
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(0).getPosition(), trackData.getIndex(1).getPosition(), trackData, true, getConfiguration()));
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(1).getPosition(), nextPosition, trackData, false, getConfiguration()));
          break;
      }
    }
  }
  
  /**
   * Perform the specified ProcessAction.
   * @param processAction
   * @param audioInputStream The audio stream from which to read.
   * @param currentAudioFramePos The current frame position in the audio stream.
   * @return The current frame position after processing.
   * @throws IOException
   */
  private long performProcessAction ( final TrackCutterProcessingAction processAction
                                    , final AudioInputStream audioInputStream
                                    , final long currentAudioFramePos
                                    ) throws IOException
  {
    // Skip positions in the audioInputStream until we are at out starting position.
    long fromAudioFramePos = skipToPosition (processAction.getStartPosition(), audioInputStream, currentAudioFramePos);
    
    // Determine the position to which we should read from the input.
    long toAudioFramePos = audioInputStream.getFrameLength();
    if (processAction.getEndPosition() != null)
    {
      toAudioFramePos = getAudioFormatFrames(processAction.getEndPosition(), audioInputStream.getFormat());
    }
    
    performProcessAction
      ( processAction
      , new AudioInputStream(audioInputStream, audioInputStream.getFormat(), toAudioFramePos - fromAudioFramePos)
      );
    
    return toAudioFramePos;
  }
  
  /**
   * Perform the specified ProcessAction.
   * @param processAction
   * @param audioInputStream The audio stream from which to read. This stream will be closed afterward.
   * @throws IOException
   */
  private void performProcessAction ( final TrackCutterProcessingAction processAction
                                    , final AudioInputStream audioInputStream
                                    ) throws IOException
  {
    if (!getConfiguration().getRedirectToPostprocessing())
    {
      // We're going to create target files, so make sure there's a directory for them.
      processAction.getCutFile().getParentFile().mkdirs();
    }
    
    if (configuration.getDoPostProcessing() && configuration.getRedirectToPostprocessing())
    {
      OutputStream audioOutputStream = null;
      
      try
      {
        audioOutputStream = this.createPostProcessingProcess(processAction).getOutputStream();
        AudioSystem.write(audioInputStream, configuration.getTargetType(), audioOutputStream);
      }
      finally
      {
        if (audioOutputStream!=null)
        {
          // We can't do anything about any exceptions here, so we don't catch them.
          audioOutputStream.close();
        }
      }
    }
    else
    {
      AudioSystem.write(audioInputStream, configuration.getTargetType(), processAction.getCutFile());
      
      if (configuration.getDoPostProcessing())
      {
        this.createPostProcessingProcess(processAction);
      }
    }
  }
  
  /**
   * Create the specified post-processing process.
   * @param processAction
   * @return The specified post-processing process.
   * @throws IOException
   */
  private Process createPostProcessingProcess
    ( final TrackCutterProcessingAction processAction
    ) throws IOException
  {
    processAction.getPostProcessFile().getParentFile().mkdirs();
    Process process = Runtime.getRuntime().exec(processAction.getPostProcessCommand());
    
    pipeStream(process.getInputStream(), processAction.getStdOutRedirectFile());
    pipeStream(process.getErrorStream(), processAction.getErrRedirectFile());
    
    return process;
  }
  
  /**
   * Pipe the contents of the specified input stream to the specified file, or throw it away if the file is
   * null.
   * @param in
   * @param file The file to pipe input to, or null if the input should be thrown away.
   * @throws IOException
   */
  private static void pipeStream(final InputStream in, final File file) throws IOException
  {
    OutputStream out = null;
    if (file!=null)
    {
      out = new FileOutputStream(file);
    }
    new Thread(new StreamPiper(in, out, true)).start();
  }
  
  /**
   * Get the number of AudioFormat frames represented by the specified Position. Note that an AudioFormat
   * frame may represent a longer or shorter time than a cue sheet frame. 
   * @param position
   * @param audioFileFormat
   * @return The number of AudioFormat frames represented by the specified Position. Note that an AudioFormat
   * frame may represent a longer or shorter time than a cue sheet frame.
   */
  private static long getAudioFormatFrames(final Position position, final AudioFormat audioFormat)
  {
    // Determine closest frame number.
    return (long) Math.round(((double) audioFormat.getFrameRate())/75 * position.getTotalFrames());
  }
  
  private long skipToPosition
    ( Position toPosition
    , AudioInputStream audioInputStream
    , long currentAudioFramePos
    ) throws IOException
  {
    long toAudioFramePos = getAudioFormatFrames(toPosition, audioInputStream.getFormat());
    audioInputStream.skip((toAudioFramePos - currentAudioFramePos)  * audioInputStream.getFormat().getFrameSize());
    return toAudioFramePos;
  }
  
  /**
   * Get the configuration for this TrackCutter.
   * @return The configuration for this TrackCutter.
   */
  private TrackCutterConfiguration getConfiguration()
  {
    return this.configuration;
  }
}
