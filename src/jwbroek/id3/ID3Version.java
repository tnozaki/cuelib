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

public enum ID3Version
{
  ID3v1(1,0),
  ID3v1r0(1,0),
  ID3v1r1(1,1),
  ID3v2(2,0),
  ID3v2r0(2,0),
  ID3v2r2(2,2),
  ID3v2r3(2,3),
  ID3v2r4(3,3);
  
  private int majorVersion;
  private int minorVersion;
  
  ID3Version(final int majorVersion, final int minorVersion)
  {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
  }
  
  public int getMajorVersion()
  {
    return this.majorVersion;
  }
  
  public int getMinorVersion()
  {
    return this.minorVersion;
  }
  
  // TODO Provide better ordering.
}
