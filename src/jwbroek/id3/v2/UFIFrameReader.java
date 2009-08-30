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
import java.util.ArrayList;
import java.util.List;

import jwbroek.id3.UniqueFileIdentifierFrame;

public class UFIFrameReader implements FrameReader
{
  private final int headerSize;
  
  public UFIFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public UniqueFileIdentifierFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException
  {
    final UniqueFileIdentifierFrame result = new UniqueFileIdentifierFrame();
    result.setTotalFrameSize(size + this.headerSize);
    
    final StringBuilder owner = new StringBuilder();
    final List<Integer> identifier = new ArrayList<Integer>();
    boolean haveNul = false;
    
    for (int index = 0; index < size; index++)
    {
      final int i = input.read();
      if (haveNul)
      {
        identifier.add(i);
      }
      else
      {
        if (i == 0)
        {
          haveNul = true;
        }
        else
        {
          owner.append((char) i);
        }
      }
    }
    result.setOwnerIdentifier(owner.toString());

    final StringBuilder hexIdentifier = new StringBuilder();
    for (final Integer i : identifier)
    {
      hexIdentifier.append(Integer.toHexString(i));
    }
    result.setHexIdentifier(hexIdentifier.toString());
    
    return result;
  }
}