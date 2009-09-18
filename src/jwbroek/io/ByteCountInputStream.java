/*
 * Created on Sep 18, 2009
 */
package jwbroek.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

// TODO Add option to not treat skipping as reading.

/**
 * Counts the number of bytes that are read or skipped. (Skipping is treated as reading.) A read that returns -1
 * is not counted.
 * @author jwbroek
 */
public class ByteCountInputStream extends FilterInputStream
{
  private long bytesRead = 0;
  
  public ByteCountInputStream(final InputStream in)
  {
    super(in);
  }
  
  @Override
  public int read() throws IOException
  {
    final int byteRead = in.read(); // Is all that super.read() does.
    if (byteRead >= 0)
    {
      this.bytesRead++;
    }
    return byteRead;
  }
  
  // read(byte[]) is implemented to call read(byte[],int,int), so no need to override.
  
  @Override
  public int read(final byte[] b, final int off, final int len) throws IOException
  {
    final int bytesRead = super.read(b, off, len);
    if (bytesRead > 0)
    {
      this.bytesRead += bytesRead;
    }
    return bytesRead;
  }
  
  @Override
  public long skip(final long n) throws IOException
  {
    final long bytesSkipped = super.skip(n);
    if (bytesSkipped > 0)
    {
      this.bytesRead += bytesSkipped;
    }
    return bytesSkipped;
  }

  /**
   * Get the bytesRead of this ByteCountInputStream.
   * @return The bytesRead of this ByteCountInputStream.
   */
  public long getBytesRead()
  {
    return bytesRead;
  }

  public void resetBytesRead()
  {
    this.bytesRead = 0;
  }
}
