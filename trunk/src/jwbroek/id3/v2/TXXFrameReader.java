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