/*
 * Created on Aug 27, 2009
 */
package jwbroek.id3;

import java.nio.charset.Charset;
import java.util.Properties;

public class CommentFrame implements ID3Frame
{
  private String description;
  private String text;
  private String languageCode;
  private int totalFrameSize;
  private Charset charset = Charset.forName("ISO-8859-1");
  private Properties flags = new Properties();
  
  public CommentFrame()
  {
  }
  
  public CommentFrame(final int totalFrameSize)
  {
    this.totalFrameSize = totalFrameSize;
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Comment frame: ").append(this.languageCode).append(" [").append(this.totalFrameSize).append("] ")
            .append(this.charset.toString()).append('\n')
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("Description: ").append(this.description)
            .append("Text: ").append(this.text)
            ;
    return builder.toString();
  }
  
  /**
   * 
   * @param charset
   */
  public void setCharset(final Charset charset)
  {
    this.charset = charset;
  }

  /**
   * 
   * @return
   */
  public Charset getCharset()
  {
    return this.charset;
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

  /**
   * @return the text
   */
  public String getText()
  {
    return text;
  }

  /**
   * @param text the text to set
   */
  public void setText(String text)
  {
    this.text = text;
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
  public void setDescription(String name)
  {
    this.description = name;
  }

  public CanonicalFrameType getCanonicalFrameType()
  {
    return CanonicalFrameType.COMMENT;
  }

  /**
   * @return the languageCode
   */
  public String getLanguageCode()
  {
    return languageCode;
  }

  /**
   * @param languageCode the languageCode to set
   */
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
  }

  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }
}
