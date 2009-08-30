/*
 * Created on Aug 29, 2009
 */
package jwbroek.id3;

import java.util.Properties;

public class UniqueFileIdentifierFrame implements ID3Frame
{
  // TODO Change to byte array?
  private String hexIdentifier;
  private String ownerIdentifier;
  private int totalFrameSize;
  private Properties flags = new Properties();
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }

  public UniqueFileIdentifierFrame()
  {
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Unique File Identifier frame: [").append(this.totalFrameSize).append("]\n")
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("Owner identifier: ").append(this.ownerIdentifier).append('\n')
            .append("Identifier: ").append(this.hexIdentifier)
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

  public String getOwnerIdentifier()
  {
    return this.ownerIdentifier;
  }

  public void setOwnerIdentifier (final String ownerIdentifier)
  {
    this.ownerIdentifier = ownerIdentifier;
  }
  
  public String getHexIdentifier()
  {
    return this.hexIdentifier;
  }

  public void setHexIdentifier (final String hexIdentifier)
  {
    this.hexIdentifier = hexIdentifier;
  }

  public CanonicalFrameType getCanonicalFrameType()
  {
    return CanonicalFrameType.MUSIC_CD_IDENTIFIER;
  }
}
