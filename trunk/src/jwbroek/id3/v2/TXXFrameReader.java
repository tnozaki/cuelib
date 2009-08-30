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

import jwbroek.id3.UserDefinedTextFrame;

public class TXXFrameReader implements FrameReader
{
  private final int headerSize;
  
  public TXXFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public UserDefinedTextFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final UserDefinedTextFrame result = new UserDefinedTextFrame();
    result.setTotalFrameSize(size + this.headerSize);
    
    final int encoding = input.read();
    
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
    
    // Read entire field, then process.
    // Length -1 because of the encoding byte.
    final byte [] b = new byte[size - 1];
    input.read(b);
    final String rawResult = new String(b, charset);
    int nulPosition = rawResult.indexOf(0);
    if (nulPosition < 0)
    {
      throw new MalformedFrameException("Description not terminated in TXX frame.");
    }
    final String description = rawResult.substring(0, nulPosition);
    final String rawValue = rawResult.substring(nulPosition + 1);
    nulPosition = rawValue.indexOf(0);
    final String value = rawValue.substring(0, (nulPosition==-1)?rawValue.length():nulPosition);
    result.setDescription(description);
    result.setText(value);
    
    return result;
  }
}