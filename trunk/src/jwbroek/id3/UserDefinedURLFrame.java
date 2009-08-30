/*
 * Created on Aug 29, 2009
 */
package jwbroek.id3;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

public class UserDefinedURLFrame implements ID3Frame
{
  // TODO Use proper URL.
  private String url;
  private int totalFrameSize;
  private String description;
  private Charset charset = Charset.forName("ISO-8859-1");
  private Properties flags = new Properties();
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  public UserDefinedURLFrame()
  {
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("User defined URL frame: ").append(" [").append(this.totalFrameSize).append("]\n")
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("Description: ").append(this.description).append('\n')
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
    return CanonicalFrameType.USER_DEFINED_URL;
  }

  /**
   * @return the description
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description)
  {
    this.description = description;
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
}
