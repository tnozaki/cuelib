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
package jwbroek.id3.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class FieldReader
{
  private FieldReader()
  {
    // No need to instantiate.
  }
  
  public static String readUntilNul(final InputStream input, final int length, final Charset charset, final int charLength) throws IOException
  {
    if (length % charLength != 0)
    {
      throw new IllegalArgumentException("length is not a multiple of charLength.");
    }
    final int maxIterations = length / charLength;
    final byte [] b = new byte[charLength];
    final StringBuilder result = new StringBuilder();
    for (int index = 0; index < maxIterations; index++)
    {
      input.read(b);
      final String c = new String(b, charset);
      if ("\u0000".equals(c))
      {
        break;
      }
      else
      {
        result.append(c);
      }
    }
    return result.toString();
  }
  
  public static String readField(final InputStream input, final int length, final Charset charset) throws IOException
  {
    // Read entire field, but throw away everything after nul character.
    final byte [] b = new byte[length];
    input.read(b);
    final String rawResult = new String(b, charset);
    final int nulPosition = rawResult.indexOf(0);
    return rawResult.substring(0, (nulPosition==-1)?rawResult.length():nulPosition);
  }
}
