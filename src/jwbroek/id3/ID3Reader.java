/*
 * Created on Aug 21, 2009
 */
package jwbroek.id3;

import java.io.File;
import java.io.IOException;

import jwbroek.id3.v2.MalformedFrameException;
import jwbroek.id3.v2.UnsupportedEncodingException;

public interface ID3Reader
{
  public boolean hasTag(final File file) throws IOException;
  public ID3Tag read(final File file) throws IOException, UnsupportedEncodingException, MalformedFrameException;
}
