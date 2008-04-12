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

public class TrackCutter
{
  public enum PregapHandling
  {
    PREPEND,
    DISCARD,
    SEPARATE
  };
  
  public TrackCutterConfiguration configuration;
  
  public TrackCutter(TrackCutterConfiguration configuration)
  {
    this.configuration = configuration;
  }
  
  public void cutTracksInCueSheet(File cueFile) throws IOException
  {
    CueSheet cueSheet = null;
    
    // If no parent directory specified, then set the parent directory of the cue file.
    if (this.configuration.getParentDirectory()==null)
    {
      this.configuration.setParentDirectory(cueFile.getParentFile());
    }
    
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
  
  private void addProcessActions(TrackData trackData, Position nextPosition, List<TrackCutterProcessingAction> processActions)
  {
    if (trackData.getIndex(0) == null)
    {
      // No pregap to handle. Just process this track.
      processActions.add(new TrackCutterProcessingAction(trackData.getIndex(1).getPosition(), nextPosition, trackData, false, this.configuration));
    }
    else
    {
      switch (configuration.getPregapHandling())
      {
        case DISCARD:
          // Discard the pregap, process the track.
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(1).getPosition(), nextPosition, trackData, false, this.configuration));
          break;
        case PREPEND:
          // Prepend the pregap.
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(0).getPosition(), nextPosition, trackData, false, this.configuration));
          break;
        case SEPARATE:
          // Add pregap and track as separate tracks.
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(0).getPosition(), trackData.getIndex(1).getPosition(), trackData, true, this.configuration));
          processActions.add(new TrackCutterProcessingAction(trackData.getIndex(1).getPosition(), nextPosition, trackData, false, this.configuration));
          break;
      }
    }
  }
  
  private List<TrackCutterProcessingAction> getProcessActionList(FileData fileData)
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
  
  private void cutTracksInFileData(FileData fileData)
    throws IOException, UnsupportedAudioFileException
  {
    AudioInputStream audioInputStream = null;
    
    try
    {
      // Determine the complete path to the audio file.
      File audioFile = this.configuration.getAudioFile(fileData);
      
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
  
  private long skipToPosition ( Position toPosition
                              , AudioInputStream audioInputStream
                              , long currentAudioFramePos
                              ) throws IOException
  {
    long toAudioFramePos = getAudioFormatFrames(toPosition, audioInputStream.getFormat());
    audioInputStream.skip((toAudioFramePos - currentAudioFramePos)  * audioInputStream.getFormat().getFrameSize());
    return toAudioFramePos;
  }
  
  private long performProcessAction ( TrackCutterProcessingAction processAction
                                    , AudioInputStream audioInputStream
                                    , long currentAudioFramePos
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
    
    createFile
      ( processAction
      , new AudioInputStream(audioInputStream, audioInputStream.getFormat(), toAudioFramePos - fromAudioFramePos)
      );
    
    return toAudioFramePos;
  }
  
  private void createFile ( TrackCutterProcessingAction processAction
                          , AudioInputStream audioInputStream
                          ) throws IOException
  {
    if (!this.configuration.getRedirectToPostprocessing())
    {
      // We're going to create target files, so make sure there's a directory for them.
      processAction.getCutFile().getParentFile().mkdirs();
    }
    
    if (configuration.getDoPostProcessing() && configuration.getRedirectToPostprocessing())
    {
      OutputStream audioOutputStream = null;
      
      try
      {
        audioOutputStream = this.createProcess(processAction).getOutputStream();
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
        this.createProcess(processAction);
      }
    }
  }
  
  private Process createProcess(TrackCutterProcessingAction processAction) throws IOException
  {
    processAction.getPostProcessFile().getParentFile().mkdirs();
    Process process = Runtime.getRuntime().exec(processAction.getPostProcessCommand());
    
    pipeStream(process.getInputStream(), processAction.getStdOutRedirectFile());
    pipeStream(process.getErrorStream(), processAction.getErrRedirectFile());
    
    return process;
  }
  
  /**
   * 
   * @param in
   * @param file
   * @throws IOException
   */
  private static void pipeStream(InputStream in, File file) throws IOException
  {
    OutputStream out = null;
    if (file!=null)
    {
      out = new FileOutputStream(file);
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
}
