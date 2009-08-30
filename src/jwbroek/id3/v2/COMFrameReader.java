package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import jwbroek.id3.CommentFrame;

public class COMFrameReader implements FrameReader
{
  private final int headerSize;
  
  public COMFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public CommentFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final CommentFrame result = new CommentFrame();
    
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
    
    final StringBuilder languageBuilder = new StringBuilder();
    languageBuilder .append((char) input.read())
                    .append((char) input.read())
                    .append((char) input.read())
                    ;
    
    // Read entire field, then process.
    // Length -4 because of the encoding byte and 3 language bytes.
    final byte [] b = new byte[size - 4];
    input.read(b);
    final String rawResult = new String(b, charset);
    int nulPosition = rawResult.indexOf(0);
    if (nulPosition < 0)
    {
      throw new MalformedFrameException("Description not terminated in COM frame.");
    }
    final String description = rawResult.substring(0, nulPosition);
    final String rawText = rawResult.substring(nulPosition);
    nulPosition = rawText.indexOf(0);
    final String value = rawText.substring(0, (nulPosition==-1)?rawText.length():nulPosition);
    result.setLanguageCode(languageBuilder.toString());
    result.setDescription(description);
    result.setText(value);
    result.setTotalFrameSize(size + headerSize);
    
    return result;
  }
}
