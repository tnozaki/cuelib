package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import jwbroek.id3.UserDefinedURLFrame;
import jwbroek.id3.util.FieldReader;

public class WXXFrameReader implements FrameReader
{
  private final int headerSize;
  
  public WXXFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public UserDefinedURLFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final UserDefinedURLFrame result = new UserDefinedURLFrame();
    result.setTotalFrameSize(size + this.headerSize);
    
    final int encoding = input.read();
    
    final Charset charset;
    final int charLength;
    switch (encoding)
    {
      case 0:
        charset = Charset.forName("ISO-8859-1");
        charLength = 1;
        break;
      case 1:
        charset = Charset.forName("UTF-16");
        charLength = 2;
        break;
      case 2:
        // TODO Not supported until 2.4. Enable via option and throw exception otherwise.
        charset = Charset.forName("UTF-16BE");
        charLength = 2;
        break;
      case 3:
        // TODO Not supported until 2.4. Enable via option and throw exception otherwise.
        charset = Charset.forName("UTF-8");
        charLength = 1; // TODO Actually variable.
        break;
      default:
        throw new UnsupportedEncodingException("Encoding not supported: " + encoding);
    }
    result.setCharset(charset);
    
    // First read variable length field with user-defined encoding. Then
    // read rest of field with ISO-8859-1 encoding.
    // Size -1 because of the encoding byte.
    final String description = FieldReader.readUntilNul(input, size-1, charset, charLength);
    result.setDescription(description);
    // Length is what remains after the description and encoding byte. Length
    // of description is length in characters + 1 (for the nul) times the
    // character length.
    // TODO Fixme. Method doesn't work for UTF-8.
    final String url = FieldReader.readField(input, size - (description.length() + 1) * charLength -1, charset);
    result.setUrl(url);
    
    return result;
  }
}