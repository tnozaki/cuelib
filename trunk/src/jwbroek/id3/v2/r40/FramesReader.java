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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jwbroek.id3.CanonicalFrameType;
import jwbroek.id3.ID3Frame;
import jwbroek.id3.ID3Tag;
import jwbroek.id3.v2.COMFrameReader;
import jwbroek.id3.v2.FrameReader;
import jwbroek.id3.v2.ITunesPodcastFrameReader;
import jwbroek.id3.v2.MCIFrameReader;
import jwbroek.id3.v2.MalformedFrameException;
import jwbroek.id3.v2.PICFrameReader;
import jwbroek.id3.v2.TXXFrameReader;
import jwbroek.id3.v2.TextFrameReader;
import jwbroek.id3.v2.UFIFrameReader;
import jwbroek.id3.v2.URLFrameReader;
import jwbroek.id3.v2.UnsupportedEncodingException;
import jwbroek.id3.v2.WXXFrameReader;

public class FramesReader
{
  // TODO Make sure we can handle unexpected EOFs.
  
  private static FrameDictionary frameDictionary = new FrameDictionary();
  private static Map<String, FrameReader> frameReaders = new HashMap<String, FrameReader>();
  final private static int FRAME_HEADER_LENGTH = 10;
  
  private static void putTextFrameReader(final String frameName)
  {
    frameReaders.put(frameName, new TextFrameReader(frameDictionary.getCanonicalFrameType(frameName), FramesReader.FRAME_HEADER_LENGTH));
  }
  
  private static void putURLFrameReader(final String frameName)
  {
    frameReaders.put(frameName, new URLFrameReader(frameDictionary.getCanonicalFrameType(frameName), FramesReader.FRAME_HEADER_LENGTH));
  }
  
  private final static Set<String> discardWhenFileAltered
  = new TreeSet<String>(Arrays.asList(new String []
    { "ASPI", "AENC", "ETCO"
    , "EQU2", "MLLT", "POSS"
    , "SEEK", "SYLT", "SYTC"
    , "RVA2", "TENC", "TLEN"
    }));
  
  static
  {
    // TODO AENC
    // Must be only one per decription pair. Also only one per icon type allowed.
    frameReaders.put("APIC", new PICFrameReader(FramesReader.FRAME_HEADER_LENGTH, false));
    // TODO ASPI
    // Must be only one per language and content decription pair.
    frameReaders.put("COMM", new COMFrameReader(FramesReader.FRAME_HEADER_LENGTH));
    // TODO COMR
    // TODO ENCR
    // TODO EQU2
    // Can be only one.
    // TODO ETCO
    // TODO GEOB
    // TODO GRID
    //frameReaders.put("IPLS", new IPLFrameReader("IPLS"));
    // TODO LINK
    // Can only be one, and requires present and valid TRK frame.
    frameReaders.put("MCDI", new MCIFrameReader(FramesReader.FRAME_HEADER_LENGTH));
    // TODO MLLT
    // TODO OWNE
    // TODO PCNT
    // TODO Make switch for this.
    // Unofficial.
    frameReaders.put("PCST", new ITunesPodcastFrameReader(FramesReader.FRAME_HEADER_LENGTH));
    // TODO POPM
    // TODO POSS
    // TODO PRIV
    // TODO RBUF
    // TODO RVA2
    // TODO RVRB
    // TODO SEEK
    // TODO SIGN
    // TODO SYLT
    // TODO SYTC
    FramesReader.putTextFrameReader("TALB");
    // Integer.
    FramesReader.putTextFrameReader("TBPM");
    // Unofficial
    // TODO Make switch for this.
    FramesReader.putTextFrameReader("TCAT");
    // Composers separated by "/".
    FramesReader.putTextFrameReader("TCOM");
    // Effectively genre. Free text, but you can also reference an ID3v1
    // genre by encapsulating it in parenthesis, such as (31). You can
    // refine this adding data, such as (4)Eurodisco. If you want a
    // parenthesis in your refinement, use a double opening parenthesis,
    // such as (31)((I think).
    FramesReader.putTextFrameReader("TCON");
    // For the original, not this audio file. Must begin with a year and
    // a space.
    FramesReader.putTextFrameReader("TCOP");
    // Numeric. Delay in ms between tracks in playlist.
    FramesReader.putTextFrameReader("TDEN");
    // Unofficial
    // TODO Make switch for this.
    FramesReader.putTextFrameReader("TDES");
    FramesReader.putTextFrameReader("TDLR");
    FramesReader.putTextFrameReader("TDLY");
    FramesReader.putTextFrameReader("TDOR");
    FramesReader.putTextFrameReader("TDRC");
    // Unofficial
    // TODO Make switch for this.
    FramesReader.putTextFrameReader("TDRL");
    FramesReader.putTextFrameReader("TDTG");
    FramesReader.putTextFrameReader("TENC");
    // Textwriters separated by "/".
    FramesReader.putTextFrameReader("TEXT");
    // Default is "MPG". Are a bunch of predefined values. They are not
    // in parentheses.
    FramesReader.putTextFrameReader("TFLT");
    // TODO Make switch for this.
    // Unofficial
    FramesReader.putTextFrameReader("TGID");
    FramesReader.putTextFrameReader("TIPL");
    FramesReader.putTextFrameReader("TIT1");
    FramesReader.putTextFrameReader("TIT2");
    FramesReader.putTextFrameReader("TIT3");
    // The ground keys are represented with "A","B","C","D","E",
    // "F" and "G" and halfkeys represented with "b" and "#". Minor is
    // represented as "m". Example "Cbm". Off key is represented with an "o"
    // only.
    FramesReader.putTextFrameReader("TKEY");
    // TODO Make switch for this.
    // Unofficial
    FramesReader.putTextFrameReader("TKWD");
    FramesReader.putTextFrameReader("TLAN");
    // Numeric
    FramesReader.putTextFrameReader("TLEN");
    FramesReader.putTextFrameReader("TMCL");
    // Are a bunch of predefined values. Those are in parentheses.
    FramesReader.putTextFrameReader("TMED");
    FramesReader.putTextFrameReader("TMOO");
    FramesReader.putTextFrameReader("TOAL");
    FramesReader.putTextFrameReader("TOFN");
    // Textwriters separated by "/".
    FramesReader.putTextFrameReader("TOLY");
    // Artists separated by "/".
    FramesReader.putTextFrameReader("TOPE");
    FramesReader.putTextFrameReader("TOWN");
    FramesReader.putTextFrameReader("TPE1");
    FramesReader.putTextFrameReader("TPE2");
    FramesReader.putTextFrameReader("TPE3");
    FramesReader.putTextFrameReader("TPE4");
    // Effectively disc number. Can use "/" to include total. I.e. 1/3.
    FramesReader.putTextFrameReader("TPOS");
    // First five characters must be year followed by space.
    FramesReader.putTextFrameReader("TPRO");
    FramesReader.putTextFrameReader("TPUB");
    // Can use "/" to include total. I.e. 3/12.
    FramesReader.putTextFrameReader("TRCK");
    FramesReader.putTextFrameReader("TRSN");
    FramesReader.putTextFrameReader("TRSO");
    FramesReader.putTextFrameReader("TSOA");
    FramesReader.putTextFrameReader("TSOP");
    FramesReader.putTextFrameReader("TSOT");
    FramesReader.putTextFrameReader("TSRC");
    FramesReader.putTextFrameReader("TSSE");
    FramesReader.putTextFrameReader("TSST");
    // User defined text. Must be only one of these per description.
    frameReaders.put("TXXX", new TXXFrameReader(FramesReader.FRAME_HEADER_LENGTH));
    frameReaders.put("UFID", new UFIFrameReader(FramesReader.FRAME_HEADER_LENGTH));
    // TODO USER
    // TODO USLT
    FramesReader.putURLFrameReader("WCOM");
    FramesReader.putURLFrameReader("WCOP");
    // TODO Make switch for this.
    // Unofficial. Name and purpose suggest that its a URL frame, but it's actually a text frame
    // as it contains an encoding byte.
    FramesReader.putTextFrameReader("WFED");
    FramesReader.putURLFrameReader("WOAF");
    // May be more than one if there is more than one artist.
    FramesReader.putURLFrameReader("WOAR");
    FramesReader.putURLFrameReader("WOAS");
    FramesReader.putURLFrameReader("WORS");
    FramesReader.putURLFrameReader("WPAY");
    FramesReader.putURLFrameReader("WPUB");
    // User defined URL. Must be only one of these per description.
    frameReaders.put("WXXX", new WXXFrameReader(FramesReader.FRAME_HEADER_LENGTH));
  }
  
  public FramesReader()
  {
  }
  
  public int readNextFrame
    ( final ID3Tag tag
    , final InputStream input
    ) throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final ID3Frame frame;
    final StringBuilder frameNameBuilder = new StringBuilder(4);
    frameNameBuilder.append((char) input.read());
    frameNameBuilder.append((char) input.read());
    frameNameBuilder.append((char) input.read());
    frameNameBuilder.append((char) input.read());
    final String frameName = frameNameBuilder.toString();
    int frameSize = 0;
    for (int index = 0; index < 4; index++)
    {
      final int sizeByte = input.read();
      if (sizeByte >= 128)
      {
        frameSize = -1;
        break;
      }
      frameSize = frameSize * 128 + sizeByte;
    }
    
    if (frameSize < 0)
    {
      // TODO Throw exception.
      // TODO Strictly speaking, 0 is also illegal. Make this into an option.
      System.out.println("Illegal frame size!");
      return 8;
    }
    
    final int flagsBytes = (input.read() << 8) | input.read();
    final Map<String, String> flags = new HashMap<String, String>();
    flags.put(ID3Frame.PRESERVE_FRAME_WHEN_TAG_ALTERED, Boolean.toString((flagsBytes & 16384) == 16384));
    flags.put ( ID3Frame.PRESERVE_FRAME_WHEN_FILE_ALTERED
              , Boolean.toString( FramesReader.discardWhenFileAltered.contains(frameName)
                                || (flagsBytes & 8192) == 8192
                                )
              );
    flags.put(ID3Frame.READ_ONLY, Boolean.toString((flagsBytes & 4096) == 4096));
    final boolean containsGroupInformation = (flagsBytes & 64) == 64;
    final boolean compressionUsed = (flagsBytes & 8) == 8;
    flags.put(ID3Frame.COMPRESSION_USED, Boolean.toString(compressionUsed));
    final boolean encryptionUsed = (flagsBytes & 4) == 4;
    final boolean unsyncUsed = (flagsBytes & 2) == 2;
    // TODO Handle unsync in frames. Take care not to do double unsync when tag flag is also set.
    flags.put(ID3Frame.UNSYNC_USED, Boolean.toString(unsyncUsed));
    final boolean dataLengthIndicatorPresent = (flagsBytes & 1) == 1;

    if (containsGroupInformation)
    {
      final int groupId = input.read();
      flags.put(ID3Frame.GROUP_ID, Integer.toString(groupId));
    }
    if (encryptionUsed)
    {
      final int encryptionMethodUsed = input.read();
      flags.put(ID3Frame.ENCRYPTION_METHOD_USED, Integer.toString(encryptionMethodUsed));
    }
    if (dataLengthIndicatorPresent)
    {
      final int dataLength = input.read() * (1 << 20) + input.read() * (1 << 13) + input.read() * (1 << 6) + input.read();
      flags.put(ID3Frame.DATA_LENGTH_INDICATOR, Integer.toString(dataLength));
    }
    
    final FrameReader reader = FramesReader.frameReaders.get(frameName.toString());
    if (reader == null)
    {
      if ("\u0000\u0000\u0000\u0000".equals(frameName.toString()))
      {
        // End of frames.
        return FramesReader.FRAME_HEADER_LENGTH;
      }
      else if (frameName.charAt(0)=='T')
      {
        // TODO: Add option to enable/disable this behaviour.
        System.out.println("Encountered unknown text frame: " + frameName);
        frame = new TextFrameReader(CanonicalFrameType.USER_DEFINED_TEXT, FramesReader.FRAME_HEADER_LENGTH).readFrameBody(frameName, frameSize, input);
      }
      else if (frameName.charAt(0)=='W')
      {
        // TODO: Add option to enable/disable this behaviour.
        System.out.println("Encountered unknown URL frame: " + frameName);
        frame = new URLFrameReader(CanonicalFrameType.USER_DEFINED_URL, FramesReader.FRAME_HEADER_LENGTH).readFrameBody(frameName, frameSize, input);
      }
      else
      {
        System.out.println("Encountered unsupported frame type: " + frameName + " of length " + frameSize);
        input.skip(frameSize);
        frame = null;
        // TODO Handle
      }
    }
    else
    {
      frame = reader.readFrameBody(frameSize, input);
    }

    if (frame != null)
    {
      frame.getFlags().putAll(flags);
      tag.getFrames().add(frame);
    }
    
    return frameSize + FramesReader.FRAME_HEADER_LENGTH; // Size + header size.
  }
  
  public void readFrames(final ID3Tag tag, final InputStream input, final long length) throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    long bytesLeft = length;
    while (bytesLeft >= FramesReader.FRAME_HEADER_LENGTH)
    {
      final long bytesRead = readNextFrame(tag, input);
      bytesLeft -= bytesRead;
      if (bytesRead == FramesReader.FRAME_HEADER_LENGTH)
      {
        input.skip(bytesLeft);
        bytesLeft = 0;
      }
    }
    input.skip(bytesLeft);
  }
}
