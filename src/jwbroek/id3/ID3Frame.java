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
