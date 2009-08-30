/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007-2009 Jan-Willem van den Broek
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
package jwbroek.id3;

import java.util.Properties;

public class UniqueFileIdentifierFrame implements ID3Frame
{
  // TODO Change to byte array?
  private String hexIdentifier;
  private String ownerIdentifier;
  private int totalFrameSize;
  private Properties flags = new Properties();
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  public UniqueFileIdentifierFrame()
  {
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Unique File Identifier frame: [").append(this.totalFrameSize).append("]\n")
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("Owner identifier: ").append(this.ownerIdentifier).append('\n')
            .append("Identifier: ").append(this.hexIdentifier)
            ;
    return builder.toString();
  }
  
  /**
   * @return the declaredSize
   */
  public int getTotalFrameSize()
  {
    return totalFrameSize;
  }

  /**
   * @param totalFrameSize the totalFrameSize to set
   */
  public void setTotalFrameSize(final int totalFrameSize)
  {
    this.totalFrameSize = totalFrameSize;
  }

  public String getOwnerIdentifier()
  {
    return this.ownerIdentifier;
  }

  public void setOwnerIdentifier (final String ownerIdentifier)
  {
    this.ownerIdentifier = ownerIdentifier;
  }
  
  public String getHexIdentifier()
  {
    return this.hexIdentifier;
  }

  public void setHexIdentifier (final String hexIdentifier)
  {
    this.hexIdentifier = hexIdentifier;
  }

  public CanonicalFrameType getCanonicalFrameType()
  {
    return CanonicalFrameType.MUSIC_CD_IDENTIFIER;
  }
}
