/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007-2008 Jan-Willem van den Broek
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
package jwbroek.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class for piping data from an InputStream to an OutputStream, or to nowhere. This class is particularly useful
 * for reading the output streams of a java.lang.Process, as such a Process may block if its output is not read.
 * @author jwbroek
 */
public class StreamPiper implements Runnable
{
  private InputStream from;
  private OutputStream to;
  private boolean closeOutput;
  
  /**
   * Pipe all input from the InputStream to the OutputStream. The OutputStream is explicitly allowed to be null.
   * In such a case, all input will be discarded. In any case, the OutputStream will not be closed by StreamPiper,
   * but the InputStream will, once its end is reached.
   * @param from
   * @param to
   */
  public StreamPiper(InputStream from, OutputStream to)
  {
    this(from, to, false);
  }
  
  /**
   * Pipe all input from the InputStream to the OutputStream. The OutputStream is explicitly allowed to be null.
   * In such a case, all input will be discarded. In any case, the OutputStream will only be closed by StreamPiper if
   * this is requested, while the InputStream will always be, once its end is reached.
   * @param from
   * @param to
   * @param closeOutput
   */
  public StreamPiper(InputStream from, OutputStream to, boolean closeOutput)
  {
    this.from = from;
    this.to = to;
    this.closeOutput = closeOutput;
  }
  
  /**
   * Perform the data piping.
   */
  public void run()
  {
    try
    {
      int input = this.from.read();
      while (input != -1)
      {
        if (this.to != null)
        {
          this.to.write(input);
        }
        input = this.from.read();
      }
    }
    catch (IOException e)
    {
      // Nothing we can do.
    }
    finally
    {
      try
      {
        this.from.close();
      }
      catch (IOException e)
      {
        // Nothing we can do.
      }
      if (this.closeOutput && this.to != null)
      {
        try
        {
          this.to.close();
        }
        catch (IOException e)
        {
          // Nothing we can do.
        }
      }
    }
  }
}