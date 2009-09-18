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

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PictureFrame implements ID3Frame
{
  private int totalFrameSize;
  private Charset charset = Charset.forName("ISO-8859-1");
  private Properties flags = new Properties();
  private PictureType pictureType = PictureType.OTHER;
  private int pictureNumber = pictureType.getNumber();
  private String description;
  //"PNG", "JPG" for 2.0, MIME for later version. Can also be "-->", in which case the payload is a hyperlink
  // to the image.
  // TODO Tidy up for usecase when hyperlink is used.
  private String imageType; 
  private byte[] imageData;
  
  public PictureFrame()
  {
  }
  
  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder .append("Picture frame: [").append(this.totalFrameSize).append("] ")
            .append(this.charset.toString()).append('\n')
            .append("Flags: ").append(this.flags.toString()).append('\n')
            .append("Type: ").append(this.pictureType.toString()).append(" (").append(this.pictureNumber).append(")\n")
            .append("Format: ").append(this.imageType).append(")\n")
            .append("Description: ").append(this.description)
            ;
    return builder.toString();
  }
  
  public void setPictureType(final PictureType pictureType)
  {
    this.pictureType = pictureType;
    this.pictureNumber = pictureType.getNumber();
  }
  
  public int getPictureNumber()
  {
    return this.pictureNumber;
  }
  
  public void setPictureNumber(final int number)
  {
    this.pictureNumber = number;
    this.pictureType = PictureType.getPictureType(number);
  }

  public CanonicalFrameType getCanonicalFrameType()
  {
    return CanonicalFrameType.PICTURE;
  }

  public Properties getFlags()
  {
    return this.flags;
  }

  public int getTotalFrameSize()
  {
    return this.totalFrameSize;
  }

  public void setTotalFrameSize(final int totalFrameSize)
  {
    this.totalFrameSize = totalFrameSize;
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
  
  /**
   * Get the description of this PictureFrame.
   * @return The description of this PictureFrame.
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Set the description of this PictureFrame.
   * @param description The description of this PictureFrame.
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Get the imageType of this PictureFrame.
   * @return The imageType of this PictureFrame.
   */
  public String getImageType()
  {
    return imageType;
  }

  /**
   * Set the imageType of this PictureFrame.
   * @param imageType The imageType of this PictureFrame.
   */
  public void setImageType(String imageType)
  {
    this.imageType = imageType;
  }

  /**
   * Get the imageData of this PictureFrame.
   * @return The imageData of this PictureFrame.
   */
  public byte[] getImageData()
  {
    return imageData;
  }

  /**
   * Set the imageData of this PictureFrame.
   * @param imageData The imageData of this PictureFrame.
   */
  public void setImageData(byte[] imageData)
  {
    this.imageData = imageData;
  }
  
  public enum PictureType
  {
    OTHER(0),
    FILE_ICON_32X32(1),
    OTHER_FILE_ICON(2),
    FRONT_COVER(3),
    BACK_COVER(4),
    LEAFLET_PAGE(5),
    MEDIA(6),
    LEAD_PERFORMER(7),
    PERFORMER(8),
    CONDUCTOR(9),
    BAND_OR_ORCHESTRA(10),
    COMPOSER(11),
    LYRICIST(12),
    RECORDING_LOCATION(13),
    DURING_RECORDING(14),
    DURING_PERFORMANCE(15),
    MOVIE_CAPTURE(16),
    A_BRIGHT_COLOURED_FISH(17),  // Not a joke. Actually in spec.
    ILLUSTRATION(18),
    BAND_OR_ARTIST_LOGOTYPE(19),
    PUBLISHER_OR_STUDIO_LOGOTYPE(20),
    UNOFFICIAL(21);  // Needs additional info.
    
    private static Map<Integer,PictureType> numberToType = new HashMap<Integer,PictureType>();
    
    static
    {
      for (int index = 0; index < PictureType.values().length; index++)
      {
        PictureType.numberToType.put(PictureType.values()[index].getNumber(), PictureType.values()[index]);
      }
    }
    
    private int number;
    
    PictureType(final int number)
    {
      this.number = number;
    }
    
    public int getNumber()
    {
      return this.number;
    }
    
    public static PictureType getPictureType(final int number)
    {
      PictureType result = PictureType.numberToType.get(number);
      if (result == null)
      {
        return PictureType.UNOFFICIAL;
      }
      else
      {
        return result;
      }
    }
  }
}
