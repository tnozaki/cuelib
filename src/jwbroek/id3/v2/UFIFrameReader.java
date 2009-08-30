package jwbroek.id3.v2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jwbroek.id3.UniqueFileIdentifierFrame;

public class UFIFrameReader implements FrameReader
{
  private final int headerSize;
  
  public UFIFrameReader(final int headerSize)
  {
    this.headerSize = headerSize;
  }
  
  public UniqueFileIdentifierFrame readFrameBody(final int size, final InputStream input)
    throws IOException, UnsupportedEncodingException
  {
    final UniqueFileIdentifierFrame result = new UniqueFileIdentifierFrame();
    result.setTotalFrameSize(size + this.headerSize);
    
    final StringBuilder owner = new StringBuilder();
    final List<Integer> identifier = new ArrayList<Integer>();
    boolean haveNul = false;
    
    for (int index = 0; index < size; index++)
    {
      final int i = input.read();
      if (haveNul)
      {
        identifier.add(i);
      }
      else
      {
        if (i == 0)
        {
          haveNul = true;
        }
        else
        {
          owner.append((char) i);
        }
      }
    }
    result.setOwnerIdentifier(owner.toString());

    final StringBuilder hexIdentifier = new StringBuilder();
    for (final Integer i : identifier)
    {
      hexIdentifier.append(Integer.toHexString(i));
    }
    result.setHexIdentifier(hexIdentifier.toString());
    
    return result;
  }
}