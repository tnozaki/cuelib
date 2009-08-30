/*
 * Created on Aug 29, 2009
 */
package jwbroek.id3;

import java.net.URL;
import java.util.Properties;


public class URLFrame implements ID3Frame
{
  // TODO Use proper URL.
  private String url;
  private int totalFrameSize;
  private CanonicalFrameType canonicalFrameType;
  private Properties flags = new Properties();
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  public URLFrame(final CanonicalFrameType canonicalFrameType)
  {
    this.canonicalFrameType = canonicalFrameType;
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("URL frame: ").append(this.canonicalFrameType.toString())
            .append(" [").append(this.totalFrameSize).append("]\n")
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("URL: ").append(this.url)
            ;
    return builder.toString();
  }
  
  /**
   * 
   * @param url
   */
  public void setUrl(final URL url)
  {
    this.url = url.toString();
  }
  
  /**
   * 
   * @param url
   */
  public void setUrl(final String url)
  {
    this.url = url;
  }

  /**
   * 
   * @return
   */
  public String getUrl()
  {
    return this.url;
  }
  
  /**
   * @return the declaredSize
   */
  public int getTotalFrameSize()
  {
    return totalFrameSize;
  }

  /**
   * @param totalFrameSize the totalFrameSize to set
   */
  public void setTotalFrameSize(final int totalFrameSize)
  {
    this.totalFrameSize = totalFrameSize;
  }
  
  public CanonicalFrameType getCanonicalFrameType()
  {
    return this.canonicalFrameType;
  }

  public void setCanonicalFrameType(final CanonicalFrameType canonicalFrameType)
  {
    this.canonicalFrameType = canonicalFrameType;
  }
}
