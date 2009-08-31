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

public class COMFrameReader implements FrameReader
{
  private final int headerSize;
  
  public COMFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public CommentFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final CommentFrame result = new CommentFrame();
    
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
    
    final StringBuilder languageBuilder = new StringBuilder();
    languageBuilder .append((char) input.read())
                    .append((char) input.read())
                    .append((char) input.read())
                    ;
    
    // Read entire field, then process.
    // Length -4 because of the encoding byte and 3 language bytes.
    final byte [] b = new byte[size - 4];
    input.read(b);
    final String rawResult = new String(b, charset);
    int nulPosition = rawResult.indexOf(0);
    if (nulPosition < 0)
    {
      throw new MalformedFrameException("Description not terminated in COM frame.");
    }
    final String description = rawResult.substring(0, nulPosition);
    final String rawText = rawResult.substring(nulPosition+1);
    nulPosition = rawText.indexOf(0);
    final String value = rawText.substring(0, (nulPosition==-1)?rawText.length():nulPosition);
    result.setLanguageCode(languageBuilder.toString());
    result.setDescription(description);
    result.setText(value);
    result.setTotalFrameSize(size + headerSize);
    
    return result;
  }
}
