/*
 * Created on Aug 27, 2009
 */
package jwbroek.id3;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InvolvedPeopleFrame implements ID3Frame
{
  private int totalFrameSize;
  private Charset charset = Charset.forName("ISO-8859-1");
  private final List<InvolvedPeopleFrame.InvolvedPerson>  involvedPeopleList
    = new ArrayList<InvolvedPeopleFrame.InvolvedPerson>();
  private Properties flags = new Properties();
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  /**
   * @return the charset
   */
  public Charset getCharset()
  {
    return charset;
  }

  /**
   * @param charset the charset to set
   */
  public void setCharset(Charset charset)
  {
    this.charset = charset;
  }

  public InvolvedPeopleFrame()
  {
  }
  
  public InvolvedPeopleFrame(int totalFrameSize)
  {
    this.totalFrameSize = totalFrameSize;
  }
  
  public CanonicalFrameType getCanonicalFrameType()
  {
    return CanonicalFrameType.INVOLVED_PEOPLE_LIST;
  }

  public int getTotalFrameSize()
  {
    return this.totalFrameSize;
  }

  public void setTotalFrameSize(final int totalFrameSize)
  {
    this.totalFrameSize = totalFrameSize;
  }

  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Involved People frame: ").append(" [").append(this.totalFrameSize).append("] ")
            .append(this.charset.toString()).append('\n')
            .append("Flags: ").append(this.flags.toString()).append('\n')
            ;
    for (InvolvedPeopleFrame.InvolvedPerson involvedPerson : this.involvedPeopleList)
    {
      builder .append("Involvee: ").append(involvedPerson.getInvolvee())
              .append("Involvement: ").append(involvedPerson.getInvolvement())
              .append('\n')
              ;
    }
    return builder.toString();
  }
  
  public static class InvolvedPerson
  {
    private String involvee;
    private String involvement;
    
    public InvolvedPerson()
    {
    }
    
    /**
     * @return the involvee
     */
    public String getInvolvee()
    {
      return involvee;
    }
    
    /**
     * @param involvee the involvee to set
     */
    public void setInvolvee(String involvee)
    {
      this.involvee = involvee;
    }
    
    /**
     * @return the involvement
     */
    public String getInvolvement()
    {
      return involvement;
    }
    
    /**
     * @param involvement the involvement to set
     */
    public void setInvolvement(String involvement)
    {
      this.involvement = involvement;
    }
  }

  /**
   * @return the involvedPeopleList
   */
  public List<InvolvedPeopleFrame.InvolvedPerson> getInvolvedPeopleList()
  {
    return involvedPeopleList;
  }
}
