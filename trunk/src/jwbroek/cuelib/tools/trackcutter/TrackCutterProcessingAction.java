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

import jwbroek.cuelib.Position;
import jwbroek.cuelib.TrackData;

/**
 * Represents a processing action for a TrackCutter instance.
 * 
 * @author jwbroek
 */
public class TrackCutterProcessingAction
{
  /**
   * Starting position for the track.
   */
  private Position startPosition;

  /**
   * Ending position for the track.
   */
  private Position endPosition;

  /**
   * The corresponding TrackData instance.
   */
  private TrackData trackData;

  /**
   * Whether or not this action concerns the pregap of a track. (These may be
   * treated as separate tracks.)
   */
  private boolean isPregap;

  /**
   * The configuration of the TrackCutter. Undefined behavior occurs when the
   * configuration does not match the configuration of the TrackCutter.
   */
  private TrackCutterConfiguration configuration;

  /**
   * The target file for the cutting operation.
   */
  private File cutFile = null;

  /**
   * The target file for the post-processing operation.
   */
  private File postProcessFile = null;

  /**
   * The post-processing command.
   */
  private String postProcessingCommand = null;

  /**
   * Construct a processing action for a TrackCutter.
   * 
   * @param startPosition
   *          Starting position for the track.
   * @param endPosition
   *          Ending position for the track.
   * @param trackData
   *          The TrackData instance corresponding to the processing action.
   * @param isPregap
   *          Whether or not this action concerns the pregap of a track. (These
   *          may be treated as separate tracks.)
   * @param configuration
   *          The TrackCutterConfiguration of the TrackCutter for which this
   *          action is intended.
   */
  public TrackCutterProcessingAction(Position startPosition,
      Position endPosition, TrackData trackData, boolean isPregap,
      TrackCutterConfiguration configuration)
  {
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.trackData = trackData;
    this.isPregap = isPregap;
    this.configuration = configuration;
  }

  /**
   * Get a File instance representing the track after cutting.
   * 
   * @return A File instance representing the track after cutting.
   */
  public File getCutFile()
  {
    if (this.cutFile == null)
    {
      this.cutFile = this.configuration.getCutFile(this);
    }

    return this.cutFile;
  }

  /**
   * Get a File instance representing the track after post-processing.
   * 
   * @return A File instance representing the track after post-processing.
   */
  public File getPostProcessFile()
  {
    if (this.postProcessFile == null)
    {
      this.postProcessFile = this.configuration.getPostProcessFile(this);
    }

    return this.postProcessFile;
  }

  /**
   * Get the command to be used for post-processing.
   * 
   * @return The command to be used for post-processing.
   */
  public String getPostProcessCommand()
  {
    if (this.postProcessingCommand == null)
    {
      this.postProcessingCommand = this.configuration
          .getPostProcessCommand(this);
    }

    return this.postProcessingCommand;
  }

  /**
   * Get a File instance where standard output of the postprocessing step should be redirected to.
   * @return A File instance where standard output of the postprocessing step should be redirected to.
   */
  public File getStdOutRedirectFile()
  {
    if (this.configuration.getRedirectStdOut())
    {
      return new File(this.getPostProcessFile().getPath() + ".out");
    }
    else
    {
      return null;
    }
  }

  /**
   * Get a File instance where error output of the postprocessing step should be redirected to.
   * @return A File instance where error output of the postprocessing step should be redirected to.
   */
  public File getErrRedirectFile()
  {
    if (this.configuration.getRedirectStdOut())
    {
      return new File(this.getPostProcessFile().getPath() + ".err");
    }
    else
    {
      return null;
    }
  }

  /**
   * Get the starting position for the track.
   * @return The starting position for the track.
   */
  public Position getStartPosition()
  {
    return this.startPosition;
  }

  /**
   * Get the ending position for the track.
   * @return The ending position for the track.
   */
  public Position getEndPosition()
  {
    return this.endPosition;
  }

  /**
   * Get the associated TrackData.
   * @return The associated TrackData.
   */
  public TrackData getTrackData()
  {
    return this.trackData;
  }

  /**
   * Get whether or not this actions concerns a pregap track.
   * @return Whether or not this actions concerns a pregap track.
   */
  public boolean getIsPregap()
  {
    return this.isPregap;
  }
}
