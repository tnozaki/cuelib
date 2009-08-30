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
package jwbroek.id3.v1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import jwbroek.id3.CanonicalFrameType;
import jwbroek.id3.ID3Tag;
import jwbroek.id3.ID3Reader;
import jwbroek.id3.ID3Version;
import jwbroek.id3.TextFrame;

public class ID3v1Reader implements ID3Reader
{
  public ID3v1Reader()
  {
    
  }
  
  public boolean hasTag(final File file) throws IOException
  {
    final RandomAccessFile input = new RandomAccessFile(file, "r");
    try
    {
      if (input.length() >= 128)
      {
        input.seek(input.length()-128);
        if  ( input.readUnsignedByte() == 'T'
            && input.readUnsignedByte() == 'A'
            && input.readUnsignedByte() == 'G'
            )
        {
          return true;
        }
      }
      return false;
    }
    finally
    {
      input.close();
    }
  }
  
  /*
   * (non-Javadoc)
   * @see jwbroek.id3.ID3Reader#read(java.io.File)
   */
  public ID3Tag read(final File file) throws IOException
  {
    ID3Tag tag = new ID3Tag();
    final RandomAccessFile input = new RandomAccessFile(file, "r");
    try
    {
      if (input.length() >= 128)
      {
        input.seek(input.length()-128);
        if  ( input.readUnsignedByte() == 'T'
            && input.readUnsignedByte() == 'A'
            && input.readUnsignedByte() == 'G'
            )
        {
          tag.setVersion(ID3Version.ID3v1r0);
          // TODO Don't create frame if field is empty?
          tag.getFrames().add(new TextFrame(CanonicalFrameType.TITLE, ID3v1Reader.getField(input, 30), 30));
          tag.getFrames().add(new TextFrame(CanonicalFrameType.PERFORMER, ID3v1Reader.getField(input, 30), 30));
          tag.getFrames().add(new TextFrame(CanonicalFrameType.ALBUM, ID3v1Reader.getField(input, 30), 30));
          tag.getFrames().add(new TextFrame(CanonicalFrameType.YEAR, ID3v1Reader.getField(input, 4), 4));
          // Remember as we may extract a track number from it.
          final TextFrame commentFrame = new TextFrame(CanonicalFrameType.COMMENT, ID3v1Reader.getField(input, 30), 30);
          tag.getFrames().add(commentFrame);
          final int rawGenre = input.readUnsignedByte();
          if (rawGenre != 0)
          {
            // TODO Perhaps a message indicating that genre was not set, if this is the case.
            // TODO Genre is in different form than is the case for v2 tags. Normalise somehow.
            tag.getFrames().add(new TextFrame(CanonicalFrameType.CONTENT_TYPE, "" + rawGenre, 1));
          }
          // ID3 1.1 extension.
          input.seek(input.length()-3);
          final int trackNoMarker = input.readUnsignedByte();
          final int rawTrackNo = input.readUnsignedByte();
          if (trackNoMarker == 0)
          {
            if (rawTrackNo != 0)
            {
              // TODO Track no is in different form than is the case for v2 tags. Normalise somehow.
              tag.getFrames().add(new TextFrame(CanonicalFrameType.TRACK_NO, "" + rawTrackNo, 1));
              // Comment actually size 28.
              commentFrame.setTotalFrameSize(28);
              tag.setVersion(ID3Version.ID3v1r1);
            }
          }
        }
        else
        {
          // Not a valid ID3v1 tag.
          tag = null;
        }
      }
      else
      {
        // File too small to contain ID3v1 data.
        tag = null;
      }
    }
    finally
    {
      input.close();
    }
    
    return tag;
  }
  
  public static String getField
    ( final RandomAccessFile input
    , final int length
    ) throws IOException
  {
    final StringBuffer result = new StringBuffer();
    for (int index = 0; index < length; index++)
    {
      int i = input.readUnsignedByte();
      
      if (i==0)
      {
        // End of buffer.
        input.skipBytes(length-index-1);
        break;
      }
      else
      {
        result.append((char) i);
      }
    }
    // TODO remove trailing spaces if desired.
    return result.toString();
  }
  
	/**
	 * @param args
	 */
	public static void main(String[] args)
  {
    try
    {
      // For testing purposes...
      final ID3v1Reader reader = new ID3v1Reader();
      final File file1 = new File("C:\\tmp\\mp3\\Anaal Nathrakh\\Rock Tribune CD Sampler Juli 2009\\12_The Lucifer Effect.mp3");
      System.out.println(reader.hasTag(file1));
      final ID3Tag tag = reader.read(file1);
      if (tag != null)
      {
        System.out.println(tag.toString());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
	}

}
