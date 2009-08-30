/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007-2009 Jan-Willem van den Broek
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
