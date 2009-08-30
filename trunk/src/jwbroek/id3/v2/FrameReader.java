package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;

import jwbroek.id3.ID3Frame;

public interface FrameReader
{
  public ID3Frame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException, MalformedFrameException;
}
