package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;

import jwbroek.id3.MusicCDIdentifierFrame;

public class MCIFrameReader implements FrameReader
{
  private final int headerSize;
  
  public MCIFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public MusicCDIdentifierFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final MusicCDIdentifierFrame result = new MusicCDIdentifierFrame();
    result.setTotalFrameSize(size + headerSize);
    final StringBuilder hexBuilder = new StringBuilder();
    for (int index = 0; index < size; index++)
    {
      hexBuilder.append(Integer.toHexString(input.read()));
    }
    result.setHexTOC(hexBuilder.toString());
    
    return result;
  }
}