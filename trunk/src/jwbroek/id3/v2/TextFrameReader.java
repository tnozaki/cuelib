package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import jwbroek.id3.CanonicalFrameType;
import jwbroek.id3.TextFrame;
import jwbroek.id3.util.FieldReader;

public class TextFrameReader implements FrameReader
{
  private final CanonicalFrameType canonicalFrameType;
  private final int headerSize;
  
  public TextFrameReader(final CanonicalFrameType canonicalFrameType, final int headerSize)
  {
    this.canonicalFrameType = canonicalFrameType;
    this.headerSize = headerSize;
  }
  
  public TextFrame readFrameBody(final int size, final InputStream input) throws IOException, UnsupportedEncodingException
  {
    final TextFrame result = new TextFrame(this.canonicalFrameType);
    
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
    
    // Size -1 because we have to count the byte used for encoding.
    result.setText(FieldReader.readField(input, size-1, charset));
    result.setTotalFrameSize(size + headerSize);
    return result;
  }
}
