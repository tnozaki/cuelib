/*
 * Created on Aug 22, 2009
 */
package jwbroek.id3;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ID3Tag
{
  public static final String COMPRESSION_USED = "compression_used"; 
  public static final String CRC32_HEX = "crc32_hex"; 
  public static final String EXPERIMENTAL = "experimental"; 
  public static final String EXTENDED_HEADER_SIZE = "extended_header_size"; 
  public static final String PADDING_SIZE = "padding_size"; 
  public static final String UNSYNC_USED = "unsync_used"; 
  public static final String FOOTER_PRESENT = "footer_present"; 
  public static final String TAG_IS_UPDATE = "tag_is_update"; 
  
  private ID3Version version = ID3Version.ID3v2r4;
  private Properties flags = new Properties();
  private List<ID3Frame> frames = new ArrayList<ID3Frame>();
  private int revision = 0;
  // TODO Make sure this is consistently with or without header. Decide which makes more sense.
  private int declaredSize = 0;
  
  public ID3Tag()
  {
    
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("ID3 version: ").append(this.version.toString())
            .append(" revision ").append(this.revision).append('\n')
            .append("Flags: ").append(this.flags.toString()).append('\n')
            ;
    for (ID3Frame frame : this.frames)
    {
      builder.append(frame.toString()).append('\n');
    }
    return builder.toString();
  }
  
  /**
   * @return the version
   */
  public ID3Version getVersion()
  {
    return version;
  }
  
  /**
   * @param version the version to set
   */
  public void setVersion(final ID3Version version)
  {
    this.version = version;
  }
  
  /**
   * @return the flags
   */
  public Properties getFlags()
  {
    return flags;
  }
  
  /**
   * @return the frames
   */
  public List<ID3Frame> getFrames()
  {
    return frames;
  }

  /**
   * @return the revision
   */
  public int getRevision()
  {
    return revision;
  }

  /**
   * @param revision the revision to set
   */
  public void setRevision(int revision)
  {
    this.revision = revision;
  }

  /**
   * @return the declaredSize
   */
  public int getDeclaredSize()
  {
    return declaredSize;
  }

  /**
   * @param declaredSize the declaredSize to set
   */
  public void setDeclaredSize(final int declaredSize)
  {
    this.declaredSize = declaredSize;
  }
}
