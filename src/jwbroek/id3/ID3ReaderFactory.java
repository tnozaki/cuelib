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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import jwbroek.id3.v1.ID3v1Reader;
import jwbroek.id3.v2.r00.ID3v2r00Reader;
import jwbroek.id3.v2.r30.ID3v2r30Reader;
import jwbroek.id3.v2.r40.ID3v2r40Reader;

public class ID3ReaderFactory
{
  public ID3ReaderFactory()
  {
    
  }
  
  public ID3Reader getReader(final ID3Version version)
  {
    switch(version)
    {
      case ID3v1:
      case ID3v1r0:
      case ID3v1r1:
        return new ID3v1Reader();
      case ID3v2r0:
      case ID3v2r2:
        return new ID3v2r00Reader();
      case ID3v2r3:
        return new ID3v2r30Reader();
      case ID3v2:
      case ID3v2r4:
        return new ID3v2r40Reader();
      default:
        throw new RuntimeException("Unsupported ID3 version: " + version.toString());
    }
  }

  public ID3Reader getReader(final File file) throws IOException
  {
    return this.getReader(this.getVersion(file));
  }
  
  /**
   * Get the ID3v2 version for this file. Will be null if no supported ID3 tag is found.
   * @param input
   * @return
   * @throws IOException
   */
  private ID3Version getID3v2Version(final RandomAccessFile input) throws IOException
  {
    ID3Version result = null;
    
    if  ( input.read() == 'I'
      && input.read() == 'D'
      && input.read() == '3'
      )
    {
      final int majorVersion = input.read();
      switch (majorVersion)
      {
        case 0:
          result = ID3Version.ID3v2r0;
          break;
        case 3:
          result = ID3Version.ID3v2r3;
          break;
        case 4:
          result = ID3Version.ID3v2r4;
          break;
      }
    }
    return result;
  }
  
  /**
   * Get the ID3v1 version for this file. Will be null if no supported ID3 tag is found.
   * @param input
   * @return
   * @throws IOException
   */
  private ID3Version getID3v1Version(final RandomAccessFile input) throws IOException
  {
    ID3Version result = null;
    
    if (input.length() >= 128)
    {
      input.seek(input.length()-128);
      if  ( input.readUnsignedByte() == 'T'
          && input.readUnsignedByte() == 'A'
          && input.readUnsignedByte() == 'G'
          )
      {
        // Check if there is a track number. If so, it's 1.1.
        input.seek(input.length()-3);
        final int trackNoMarker = input.readUnsignedByte();
        final int rawTrackNo = input.readUnsignedByte();
        if (trackNoMarker == 0)
        {
          if (rawTrackNo == 0)
          {
            // Could be either v1.0 or v1.1, so just report v1.
            result = ID3Version.ID3v1;
          }
          else
          {
            // Definitely v1.1.
            result = ID3Version.ID3v1r1;
          }
        }
      }
    }
    return result;
  }
  
  /**
   * Get the ID3 version for this file. Will be null if no supported ID3 tag is found. Will be the highest supported
   * version if multiple tags are present.
   * @param file
   * @return
   * @throws IOException
   */
  public ID3Version getVersion(final File file) throws IOException
  {
    // TODO Support the difference between no ID3 & unsupported version of ID3. Are currently both mapped by null.
    
    final RandomAccessFile input = new RandomAccessFile(file, "r");
    try
    {
      ID3Version result = null;
      
      // First look for a V2 style tag.
      result = this.getID3v2Version(input);
      
      // If we have no result, check for a V1 style tag.
      if (result == null)
      {
        result = this.getID3v1Version(input);
      }
      
      return result;
    }
    finally
    {
      input.close();
    }
  }
  
  /**
   * Get the ID3 versions for this file. Unsupported versions will not be reported.
   * @param file
   * @return
   * @throws IOException
   */
  public List<ID3Version> getVersions(final File file) throws IOException
  {
    // TODO Support the difference between no ID3 & unsupported version of ID3. Are currently both mapped by null.
    final List<ID3Version> result = new ArrayList<ID3Version>();
    
    final RandomAccessFile input = new RandomAccessFile(file, "r");
    try
    {
      final ID3Version v2result = this.getID3v2Version(input);
      if (v2result != null)
      {
        result.add(v2result);
      }
      
      final ID3Version v1result = this.getID3v1Version(input);
      if (v1result != null)
      {
        result.add(v1result);
      }
      
      return result;
    }
    finally
    {
      input.close();
    }
  }
  
  public static void main(String ... param)
  {
    try
    {
      // For testing purposes...
      final ID3ReaderFactory rf = new ID3ReaderFactory();
      final File file1 = new File("C:\\tmp\\mp3\\Anaal Nathrakh\\Rock Tribune CD Sampler Juli 2009\\12_The Lucifer Effect.mp3");
      
      for (ID3Version id3Version: rf.getVersions(file1))
      {
        System.out.println(id3Version);
        final ID3Reader reader = rf.getReader(id3Version);
        System.out.println(reader.hasTag(file1));
        reader.read(file1);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
