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

import java.nio.charset.Charset;
import java.util.Properties;

public class TextFrame implements ID3Frame
{
  private String additionalTypeInfo = "";
  private String text;
  private int totalFrameSize;
  private CanonicalFrameType canonicalFrameType;
  private Charset charset = Charset.forName("ISO-8859-1");
  private Properties flags = new Properties();
  
  public TextFrame(final CanonicalFrameType canonicalFrameType)
  {
    this(canonicalFrameType, " ");
  }
  
  public TextFrame(final CanonicalFrameType canonicalFrameType, final String text)
  {
    this(canonicalFrameType, text, text.length());
  }
  
  public TextFrame(final CanonicalFrameType canonicalFrameType, final String text, final int totalFrameSize)
  {
    this.canonicalFrameType = canonicalFrameType;
    this.text = text;
    this.totalFrameSize = totalFrameSize;
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Text frame: ").append(this.canonicalFrameType.toString())
            .append(' ').append(this.additionalTypeInfo)
            .append(" [").append(this.totalFrameSize).append("] ")
            .append(this.charset.toString()).append('\n')
            .append("Flags: ").append(this.flags.toString()).append('\n')
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

  public CanonicalFrameType getCanonicalFrameType()
  {
    return this.canonicalFrameType;
  }

  public void setCanonicalFrameType(final CanonicalFrameType canonicalFrameType)
  {
    this.canonicalFrameType = canonicalFrameType;
  }

  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  /**
   * Get the additionalTypeInfo of this TextFrame.
   * @return The additionalTypeInfo of this TextFrame.
   */
  public String getAdditionalTypeInfo()
  {
    return additionalTypeInfo;
  }

  /**
   * Set the additionalTypeInfo of this TextFrame.
   * @param additionalTypeInfo The additionalTypeInfo of this TextFrame.
   */
  public void setAdditionalTypeInfo(String additionalTypeInfo)
  {
    this.additionalTypeInfo = additionalTypeInfo;
  }
}
