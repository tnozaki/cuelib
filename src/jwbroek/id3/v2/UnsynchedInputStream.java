package jwbroek.id3.v2;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class UnsynchedInputStream extends FilterInputStream
{
  public UnsynchedInputStream(final InputStream in)
  {
    super(new PushbackInputStream(in, 1));
  }
  
  @Override
  public int read() throws IOException
  {
    final int c1 = this.in.read();
    if (c1 == 0xFF)
    {
      final int c2 = this.in.read();
      if (c2 != 0x00)
      {
        ((PushbackInputStream) this.in).unread(c2);
      }
    }
    return c1;
  }
  
  @Override
  public int read(byte [] b, int off, int len) throws IOException
  {
    // Will have to use read() to perform unsync.
    for (int index = 0; index < len; index++)
    {
      final int i = this.read();
      if (i == -1)
      {
        if (index > 0)
        {
          return index;
        }
        else
        {
          return -1;
        }
      }
      b[off + index] = (byte) i;
    }
    return len;
  }
  
  @Override
  public int available() throws IOException
  {
    // Cannot guarantee more than half due to unsync.
    return this.in.available() / 2;
  }
  
  @Override
  public long skip(long n) throws IOException
  {
    // Have to override, as javadoc is inconsistent on default behaviour,
    // and is imperative that read() is used. Otherwise it is possible
    // to skip to the second byte of an unsync sequence resulting in
    // data corruption.
    for (long index = 0; index < n; index++)
    {
      final int i = this.read();
      if (i == -1)
      {
        return index;
      }
    }
    return n;
  }
}
