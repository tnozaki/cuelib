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
package jwbroek.cuelib;

/**
 * Simple representation for a position field in a cue sheet.
 * @author jwbroek
 */
public class Position
{
  private int minutes = 0;
  private int seconds = 0;
  private int frames = 0;
  
  public Position()
  {
  }
  
  public Position(int minutes, int seconds, int frames)
  {
    this.minutes = minutes;
    this.seconds = seconds;
    this.frames = frames;
  }
  
  /**
   * Get the total number of frames represented by this position. This is equal to
   * frames + (75 * (seconds + 60 * minutes)).
   * @return The total number of frames represented by this position.
   */
  public int getTotalFrames()
  {
    return frames + (75 * (seconds + 60 * minutes));
  }
  
  /**
   * @return the frames
   */
  public int getFrames()
  {
    return frames;
  }

  /**
   * @param frames the frames to set
   */
  public void setFrames(int frames)
  {
    this.frames = frames;
  }

  /**
   * @return the minutes
   */
  public int getMinutes()
  {
    return minutes;
  }

  /**
   * @param minutes the minutes to set
   */
  public void setMinutes(int minutes)
  {
    this.minutes = minutes;
  }

  /**
   * @return the seconds
   */
  public int getSeconds()
  {
    return seconds;
  }

  /**
   * @param seconds the seconds to set
   */
  public void setSeconds(int seconds)
  {
    this.seconds = seconds;
  }
}
