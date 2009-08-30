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

import jwbroek.id3.InvolvedPeopleFrame;

public class IPLFrameReader implements FrameReader
{
  private final int headerSize;
  
  public IPLFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public InvolvedPeopleFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final InvolvedPeopleFrame result = new InvolvedPeopleFrame(this.headerSize + size);
    final int encoding = input.read();
    
    System.out.println("Encoding: " + encoding);
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
    final String rawValue = new String(b, charset);
    
    int startPosition = 0;
    boolean atInvolvement = true;
    InvolvedPeopleFrame.InvolvedPerson involvedPerson = null; 
    while (startPosition < rawValue.length())
    {
      final int nulPosition = rawValue.indexOf(0);
      final int endPosition = (nulPosition==-1)?rawValue.length():nulPosition;
      final String value = rawValue.substring(startPosition, endPosition);
      if (atInvolvement)
      {
        involvedPerson = new InvolvedPeopleFrame.InvolvedPerson();
        involvedPerson.setInvolvement(value);
      }
      else
      {
        involvedPerson.setInvolvee(value);
        result.getInvolvedPeopleList().add(involvedPerson);
        involvedPerson = null;
      }
      // +1 because we don't want the nul character.
      startPosition = endPosition + 1;
    }
    
    if (involvedPerson != null)
    {
      // Involvement without involvee found.
      // TODO Throw exception?
      involvedPerson.setInvolvee("");
      result.getInvolvedPeopleList().add(involvedPerson);
    }
    
    return result;
  }
}
