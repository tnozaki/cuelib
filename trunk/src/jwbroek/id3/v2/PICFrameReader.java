/*
 * Created on Sep 18, 2009
 */
package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import jwbroek.id3.CommentFrame;
import jwbroek.id3.ID3Frame;
import jwbroek.id3.PictureFrame;
import jwbroek.id3.util.FieldReader;
import jwbroek.io.ByteCountInputStream;

public class PICFrameReader implements FrameReader
{
  private final int headerSize;
  private int imageTypeSize = -1; // -1 Stands for unlimited.
  
  public PICFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }

  public PICFrameReader(final int headerSize, final boolean v2r00Mode)
  {
    this.headerSize = headerSize;
    this.imageTypeSize = 3;
  }
  
  public PictureFrame readFrameBody(final int size, final InputStream input)
      throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final ByteCountInputStream countingInput = new ByteCountInputStream(input);
    
    final PictureFrame result = new PictureFrame();
    result.setTotalFrameSize(size + headerSize);
    
    final int encoding = countingInput.read();
    
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
    
    if (this.imageTypeSize > 0)
    {
      result.setImageType(FieldReader.readField(countingInput, this.imageTypeSize, Charset.forName("ISO-8859-1")));
    }
    else
    {
      result.setImageType(FieldReader.readUntilNul(countingInput, size - 1, Charset.forName("ISO-8859-1")));
    }
    
    result.setPictureNumber(countingInput.read());
    
    // TODO Size is actually a maximum of 64 in 2.2 and 2.3.
    result.setDescription(FieldReader.readUntilNul(countingInput, size, charset));
    
    // Remainder of frame is data.
    final byte[] imageData = new byte[size-(int)countingInput.getBytesRead()];
    countingInput.read(imageData);
    result.setImageData(imageData);
    
    return result;
  }

}
