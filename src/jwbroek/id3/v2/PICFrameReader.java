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
package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import jwbroek.id3.CommentFrame;
import jwbroek.id3.ID3Frame;
import jwbroek.id3.PictureFrame;
import jwbroek.id3.util.FieldReader;
import jwbroek.io.ByteCountInputStream;

public class PICFrameReader implements FrameReader
{
  private final int headerSize;
  private int imageTypeSize = -1; // -1 Stands for unlimited.
  
  public PICFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }

  public PICFrameReader(final int headerSize, final boolean v2r00Mode)
  {
    this.headerSize = headerSize;
    this.imageTypeSize = 3;
  }
  
  public PictureFrame readFrameBody(final int size, final InputStream input)
      throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final ByteCountInputStream countingInput = new ByteCountInputStream(input);
    
    final PictureFrame result = new PictureFrame();
    result.setTotalFrameSize(size + headerSize);
    
    final int encoding = countingInput.read();
    
    final Charset charset;
    switch (encoding)
    {
      case 0:
        charset = Charset.forName("ISO-8859-1");
        break;
      case 1:
        charset = Charset.forName("UTF-16");
        break;
      case 2:
        // TODO Not supported until 2.4. Enable via option and throw exception otherwise.
        charset = Charset.forName("UTF-16BE");
        break;
      case 3:
        // TODO Not supported until 2.4. Enable via option and throw exception otherwise.
        charset = Charset.forName("UTF-8");
        break;
      default:
        throw new UnsupportedEncodingException("Encoding not supported: " + encoding);
    }
    
    result.setCharset(charset);
    
    if (this.imageTypeSize > 0)
    {
      result.setImageType(FieldReader.readField(countingInput, this.imageTypeSize, Charset.forName("ISO-8859-1")));
    }
    else
    {
      result.setImageType(FieldReader.readUntilNul(countingInput, size - 1, Charset.forName("ISO-8859-1")));
    }
    
    result.setPictureNumber(countingInput.read());
    
    // TODO Size is actually a maximum of 64 in 2.2 and 2.3.
    result.setDescription(FieldReader.readUntilNul(countingInput, size, charset));
    
    // Remainder of frame is data.
    final byte[] imageData = new byte[size-(int)countingInput.getBytesRead()];
    countingInput.read(imageData);
    result.setImageData(imageData);
    
    return result;
  }

}
