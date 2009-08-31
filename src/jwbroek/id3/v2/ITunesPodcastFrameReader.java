/*
 * Created on Aug 31, 2009
 */
package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;

import jwbroek.id3.ITunesPodcastFrame;

public class ITunesPodcastFrameReader implements FrameReader
{
  private final int headerSize;
  
  public ITunesPodcastFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public ITunesPodcastFrame readFrameBody(final int size, final InputStream input)
      throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    final ITunesPodcastFrame result = new ITunesPodcastFrame(this.headerSize + size);
    final StringBuilder payloadBuilder = new StringBuilder();
    for (int index = 0; index < 4; index++)
    {
      payloadBuilder.append(Integer.toHexString(input.read()));
    }
    result.setPayload(payloadBuilder.toString());
    return result;
  }

}
