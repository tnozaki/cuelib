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
