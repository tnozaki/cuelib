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
   * Whether or not this action concerns the pregap of a track. (These may be treated as separate tracks.
   */
  private boolean isPregap;
  /**
   * The configuration of the TrackCutter. Undefined behavior occurs when the configuration does not
   * match the configuration of the TrackCutter.
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
   * 
   * @param startPosition
   * @param endPosition
   * @param trackData
   * @param isPregap
   * @param configuration
   */
  public TrackCutterProcessingAction
    ( Position startPosition
    , Position endPosition
    , TrackData trackData
    , boolean isPregap
    , TrackCutterConfiguration configuration
    )
  {
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.trackData = trackData;
    this.isPregap = isPregap;
    this.configuration = configuration;
  }
  
  public File getCutFile()
  {
    if (this.cutFile == null)
    {
      this.cutFile = this.configuration.getCutFile(this);
    }
    
    return this.cutFile;
  }
  
  public File getPostProcessFile()
  {
    if (this.postProcessFile == null)
    {
      this.postProcessFile = this.configuration.getPostProcessFile(this);
    }
    
    return this.postProcessFile;
  }
  
  public String getPostProcessCommand()
  {
    if (this.postProcessingCommand == null)
    {
      this.postProcessingCommand = this.configuration.getPostProcessCommand(this);
    }
    
    return this.postProcessingCommand;
  }
  
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

  public Position getStartPosition()
  {
    return this.startPosition;
  }

  public Position getEndPosition()
  {
    return this.endPosition;
  }

  public TrackData getTrackData()
  {
    return this.trackData;
  }
  
  public boolean getIsPregap()
  {
    return this.isPregap;
  }
}
