/*
 * Created on Aug 29, 2009
 */
package jwbroek.id3;

import java.util.Properties;


public class MusicCDIdentifierFrame implements ID3Frame
{
  // TODO Change to byte array?
  private String hexTOC;
  private int totalFrameSize;
  private Properties flags = new Properties();
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  public MusicCDIdentifierFrame()
  {
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Music CD Identifier frame: [").append(this.totalFrameSize).append("]\n")
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("Identifier: ").append(this.hexTOC)
            ;
    return builder.toString();
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

  public String getHexTOC()
  {
    return this.hexTOC;
  }

  public void setHexTOC (final String hexTOC)
  {
    this.hexTOC = hexTOC;
  }

  public CanonicalFrameType getCanonicalFrameType()
  {
    return CanonicalFrameType.MUSIC_CD_IDENTIFIER;
  }
}
