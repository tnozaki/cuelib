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

import jwbroek.cuelib.Position;
import jwbroek.cuelib.TrackData;

public class TrackCutterProcessingAction
{
  private Position startPosition;
  private Position endPosition;
  private TrackData trackData;
  private boolean isPregap;
  private TrackCutterConfiguration configuration;
  private File targetFile = null;
  private File postProcessFile = null;
  private String postProcessingCommand = null;
  
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
  
  public File getTargetFile()
  {
    if (this.targetFile == null)
    {
      this.targetFile = this.configuration.getTargetFile(this);
    }
    
    return this.targetFile;
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
