/*
 * Created on Aug 22, 2009
 */
package jwbroek.id3;

import java.util.Properties;

public interface ID3Frame
{
  public static final String PRESERVE_FRAME_WHEN_TAG_ALTERED = "preserve_frame_when_tag_altered"; 
  public static final String PRESERVE_FRAME_WHEN_FILE_ALTERED = "preserve_frame_when_file_altered";
  public static final String READ_ONLY = "read_only";
  public static final String COMPRESSION_USED = "compression_used";
  public static final String DATA_LENGTH_INDICATOR = "data_length_indicator";
  public static final String ENCRYPTION_METHOD_USED = "encryption_method_used";
  public static final String GROUP_ID = "group_id";
  public static final String UNSYNC_USED = "unsync_used";
  
  public int getTotalFrameSize();

  public CanonicalFrameType getCanonicalFrameType();
  
  /**
   * @return the flags
   */
  public Properties getFlags();
}
