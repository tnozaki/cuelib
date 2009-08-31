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
package jwbroek.id3.v2.r40;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jwbroek.id3.ID3Reader;
import jwbroek.id3.ID3Tag;
import jwbroek.id3.ID3Version;
import jwbroek.id3.v2.MalformedFrameException;
import jwbroek.id3.v2.UnsupportedEncodingException;
import jwbroek.id3.v2.UnsynchedInputStream;

public class ID3v2r40Reader implements ID3Reader
{
  // TODO Handle cases where tag is not at start of file.
  
  public ID3v2r40Reader()
  {
    
  }
  
  public boolean hasTag(final File file) throws IOException
  {
    final FileInputStream input = new FileInputStream(file);
    try
    {
      if  ( input.read() == 'I'
          && input.read() == 'D'
          && input.read() == '3'
          )
      {
        final int majorVersion = input.read();
        final int revision = input.read();
        if (majorVersion==4 && revision==0)
        {
          final int flags = input.read();
          // Don't trip over unsupported flags.
          // TODO Make switch for this behaviour.
          //if ((flags & 15) == 0)  // Only top three bits describe valid flags.
          //{
            for (int index = 0; index < 4; index++)
            {
              final int sizeByte = input.read();
              if (sizeByte >= 128)  // Top bit cannot be used.
              {
                return false;
              }
              return true;
            }
          //}
        }
      }
      return false;
    }
    finally
    {
      input.close();
    }
  }
  
  public ID3Tag read(final File file) throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    ID3Tag tag = new ID3Tag();
    
    final FileInputStream input = new FileInputStream(file);
    try
    {
      if  ( input.read() == 'I'
          && input.read() == 'D'
          && input.read() == '3'
          )
      {
        final int majorVersion = input.read();
        final int revision = input.read();
        if (majorVersion==4 && revision==0)
        {
          tag.setVersion(ID3Version.ID3v2r4);
          tag.setRevision(0);
          final int flags = input.read();
          final boolean unsyncUsed = (flags & 128) == 128;
          tag.getFlags().setProperty(ID3Tag.UNSYNC_USED, Boolean.toString(unsyncUsed));
          final boolean extendedHeaderUsed = (flags & 64) == 64;
          final boolean experimental = (flags & 32) == 32;
          tag.getFlags().setProperty(ID3Tag.EXPERIMENTAL, Boolean.toString(experimental));
          final boolean footerPresent = (flags & 16) == 16;
          tag.getFlags().setProperty(ID3Tag.FOOTER_PRESENT, Boolean.toString(footerPresent));
          // TODO Check that other flags are not set.
          int size = 0;
          for (int index = 0; index < 4; index++)
          {
            final int sizeByte = input.read();
            if (sizeByte >= 128)
            {
              size = -1;
              break;
            }
            size = size * 128 + sizeByte;
          }
          if (size >= 0)
          {
            tag.setDeclaredSize(size);
            
            // Read the extended header, if it is used.
            if (extendedHeaderUsed)
            {
              long extendedHeaderSize = 0;
              for (int index = 0; index < 4; index++)
              {
                final int sizeByte = input.read();
                if (sizeByte >= 128)
                {
                  extendedHeaderSize = -1;
                  break;
                }
                extendedHeaderSize = extendedHeaderSize * 128 + sizeByte;
              }
              if (extendedHeaderSize >= 6)
              {
                tag.getFlags().put(ID3Tag.EXTENDED_HEADER_SIZE, Long.toString(extendedHeaderSize));
                final int numberOfFlagBytes = input.read();
                if (numberOfFlagBytes == 1)
                {
                  final int extendedFlags = input.read();
                  final boolean tagIsAnUpdate = (extendedFlags & 64) == 64;
                  tag.getFlags().put(ID3Tag.TAG_IS_UPDATE, Boolean.toString(tagIsAnUpdate));
                  final boolean crcPresent = (extendedFlags & 32) == 32;
                  final boolean tagRestrictionsSet = (extendedFlags & 16) == 16;
                  System.out.println("Tag restrictions set: " + tagRestrictionsSet);
                  
                  if (tagIsAnUpdate)
                  {
                    final int updateFlagDataLength = input.read();
                    if (updateFlagDataLength != 0)
                    {
                      System.out.println("Invalid length for \"tag is an update\" flag encountered. Should be 0, but is " + updateFlagDataLength);
                      // TODO Handle or throw exception.
                    }
                  }
                  
                  if (crcPresent)
                  {
                    final int crcLength = input.read();
                    if (crcLength == 5)
                    {
                      // 35 bit value, but according to spec the upper 4 are not
                      // used, so would fit in the positive part of a signed integer.
                      // Seems odd though. I wonder if the spec doesn't mean the
                      // upper 5 bits of the "raw" (non-sync-safe) bytes...
                      // Using a long to be safe.
                      final long crc  = input.read() << 28
                                      | input.read() << 21
                                      | input.read() << 14
                                      | input.read() << 7
                                      | input.read();
                      tag.getFlags().put(ID3Tag.CRC32_HEX, Long.toHexString(crc));
                      // TODO Use this CRC32_HEX information.
                    }
                    else
                    {
                      System.out.println("Invalid length for CRC32_HEX flag encountered. Should be 5, but is " + crcLength);
                      // TODO Handle or throw exception.
                    }
                  }
                  
                  if (tagRestrictionsSet)
                  {
                    final int tagRestrictionsDataSize = input.read();
                    if (tagRestrictionsDataSize == 1)
                    {
                      final int restrictionsByte = input.read();
                      final int tagSizeRestrictions = (restrictionsByte & 192) >> 6;
                      final int textEncodingRestrictions = (restrictionsByte & 32) >> 5;
                      final int textFieldSizeRestrictions = (restrictionsByte & 24) >> 3;
                      final int imageEncodingRestrictions = (restrictionsByte & 4) >> 2;
                      final int imageSizeRestrictions = (restrictionsByte & 3);
                      switch (tagSizeRestrictions)
                      {
                        case 0:
                          System.out.println("Tag size restriction: No more than 128 frames and 1 MB total tag size.");
                          break;
                        case 1:
                          System.out.println("Tag size restriction: No more than 64 frames and 128 KB total tag size.");
                          break;
                        case 2:
                          System.out.println("Tag size restriction: No more than 32 frames and 40 KB total tag size.");
                          break;
                        case 3:
                          System.out.println("Tag size restriction: No more than 32 frames and 4 KB total tag size.");
                          break;
                      }
                      switch (textEncodingRestrictions)
                      {
                        case 0:
                          System.out.println("Text encoding restriction: No restrictions.");
                          break;
                        case 1:
                          System.out.println("Text encoding restriction: Strings are only encoded with ISO-8859-1 [ISO-8859-1] or UTF-8 [UTF-8].");
                          break;
                      }
                      switch (textFieldSizeRestrictions)
                      {
                        case 0:
                          System.out.println("Text field size restriction: No restrictions.");
                          break;
                        case 1:
                          System.out.println("Text field size restriction: No string is longer than 1024 characters.");
                          break;
                        case 2:
                          System.out.println("Text field size restriction: No string is longer than 128 characters.");
                          break;
                        case 3:
                          System.out.println("Text field size restriction: No string is longer than 30 characters.");
                          break;
                      }
                      switch (imageEncodingRestrictions)
                      {
                        case 0:
                          System.out.println("Image encoding restriction: No restrictions.");
                          break;
                        case 1:
                          System.out.println("Image encoding restriction: Images are encoded only with PNG [PNG] or JPEG [JFIF].");
                          break;
                      }
                      switch (imageSizeRestrictions)
                      {
                        case 0:
                          System.out.println("Image size restriction: No restrictions.");
                          break;
                        case 1:
                          System.out.println("Image size restriction: All images are 256x256 pixels or smaller.");
                          break;
                        case 2:
                          System.out.println("Image size restriction: All images are 64x64 pixels or smaller.");
                          break;
                        case 3:
                          System.out.println("Image size restriction: All images are exactly 64x64 pixels, unless required otherwise.");
                          break;
                      }
                      // TODO Check restrictions.
                      // TODO Set as property of tag.
                    }
                    else
                    {
                      System.out.println("Invalid length for tag restrictions flag encountered. Should be 1, but is " + tagRestrictionsDataSize);
                      // TODO Handle or throw exception.
                    }
                  }
                }
                else
                {
                  System.out.println("Number of flag bytes in extended header should be one, but is: " + numberOfFlagBytes);
                  // TODO Throw exception or handle.
                }
              }
              else
              {
                System.out.println("Invalid extended header size: " + extendedHeaderSize);
                // TODO Throw an exception.
              }
            }
            
            // Now to read the frames.
            final InputStream frameInputStream;
            if (unsyncUsed)
            {
              frameInputStream = new UnsynchedInputStream(input);
            }
            else
            {
              frameInputStream = input;
            }
            final FramesReader frameReader = new FramesReader();
            frameReader.readFrames(tag, frameInputStream, size);
          }
          else
          {
            // TODO Emit warning.
            // Invalid size byte encountered. Not a valid ID3 tag.
            tag = null;
          }
        }
        else
        {
          // TODO Emit warning.
          // Version and revision combination not supported.
          tag = null;
        }
      }
      else
      {
        // TODO Emit warning?
        // No valid tag found.
        tag = null;
      }
      // TODO Read footer (if present?). Is copy of header, but at end, and
      // with "3DI" instead of "ID3".
    }
    finally
    {
      input.close();
    }
    
    return tag;
  }
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    try
    {
      // For testing purposes...
      final ID3v2r40Reader reader = new ID3v2r40Reader();
      //final File file1 = new File("C:\\tmp\\mp3\\Anaal Nathrakh\\Rock Tribune CD Sampler Juli 2009\\12_The Lucifer Effect.mp3"); 
      //final File file1 = new File("C:\\tmp\\01 Prowler.mp3");
      //final File file1 = new File("C:\\tmp\\01 Satyriasis.mp3");
      final File file1 = new File("C:\\tmp\\August 21, 2009_ Gen Con Revisited.mp3");
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
