package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import jwbroek.id3.CanonicalFrameType;
import jwbroek.id3.URLFrame;
import jwbroek.id3.util.FieldReader;

public class URLFrameReader implements FrameReader
{
  private final CanonicalFrameType canonicalFrameType;
  private final int headerSize;
  
  public URLFrameReader(final CanonicalFrameType canonicalFrameType, final int headerSize)
  {
    this.canonicalFrameType = canonicalFrameType;
    this.headerSize = headerSize;
  }

  public URLFrame readFrameBody(final int size, final InputStream input) throws IOException, UnsupportedEncodingException
  {
    final URLFrame result = new URLFrame(this.canonicalFrameType);
    result.setTotalFrameSize(size + this.headerSize);
    result.setUrl(FieldReader.readField(input, size, Charset.forName("ISO-8859-1")));
    return result;
  }
}