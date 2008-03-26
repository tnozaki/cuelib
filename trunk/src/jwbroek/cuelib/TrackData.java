/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007-2008 Jan-Willem van den Broek
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
package jwbroek.cuelib;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jwbroek.cuelib.CueSheet.MetaDataField;

/**
 * Simple representation of a TRACK block of a cue sheet.
 * @author jwbroek
 */
public class TrackData
{
  private List<Index> indices = new ArrayList<Index>();
  private Set<String> flags = new TreeSet<String>();
  private int number = -1;
  private String dataType = null;
  private String isrcCode = null;
  private String performer = null;
  private String title = null;
  private Position pregap = null;
  private Position postgap = null;
  private String songwriter = null;
  private FileData parent = null;
  
  public TrackData(FileData parent)
  {
    this.parent = parent;
  }
  
  public TrackData(FileData parent, int number, String dataType)
  {
    this.parent = parent;
    this.number = number;
    this.dataType = dataType;
  }
  
  /**
   * Convenience method for getting metadata from the cue sheet. If a certain metadata field is not set, the method
   * will return the empty string. When a field is ambiguous (such as the track number on a cue sheet instead of on a
   * specific track), an IllegalArgumentException will be thrown. Otherwise, this method will attempt to give a sensible
   * answer, possibly by searching through the cue sheet.
   * @param metaDataField
   * @return The specified metadata.
   */
  public String getMetaData(MetaDataField metaDataField) throws IllegalArgumentException
  {
    switch (metaDataField)
    {
      case ISRCCODE:
        return this.getIsrcCode()==null?"":this.getIsrcCode();
      case PERFORMER:
        return this.getPerformer()==null?this.getParent().getParent().getPerformer():this.getPerformer();
      case TRACKPERFORMER:
        return this.getPerformer()==null?"":this.getPerformer();
      case SONGWRITER:
        return this.getSongwriter()==null?this.getParent().getParent().getSongwriter():this.getSongwriter();
      case TRACKSONGWRITER:
        return this.getSongwriter();
      case TITLE:
        return this.getTitle()==null?this.getParent().getParent().getTitle():this.getTitle();
      case TRACKTITLE:
        return this.getTitle();
      case TRACKNUMBER:
        return "" + this.getNumber();
      default:
        return this.getParent().getParent().getMetaData(metaDataField);
    }
  }

  /**
   * @return the dataType
   */
  public String getDataType()
  {
    return dataType;
  }
  /**
   * @param dataType the dataType to set
   */
  public void setDataType(String dataType)
  {
    this.dataType = dataType;
  }
  /**
   * @return the isrcCode
   */
  public String getIsrcCode()
  {
    return isrcCode;
  }
  /**
   * @param isrcCode the isrcCode to set
   */
  public void setIsrcCode(String isrcCode)
  {
    this.isrcCode = isrcCode;
  }
  /**
   * @return the number
   */
  public int getNumber()
  {
    return number;
  }
  /**
   * @param number the number to set
   */
  public void setNumber(int number)
  {
    this.number = number;
  }
  /**
   * @return the performer
   */
  public String getPerformer()
  {
    return performer;
  }
  /**
   * @param performer the performer to set
   */
  public void setPerformer(String performer)
  {
    this.performer = performer;
  }
  /**
   * @return the postgap
   */
  public Position getPostgap()
  {
    return postgap;
  }
  /**
   * @param postgap the postgap to set
   */
  public void setPostgap(Position postgap)
  {
    this.postgap = postgap;
  }
  /**
   * @return the pregap
   */
  public Position getPregap()
  {
    return pregap;
  }
  /**
   * @param pregap the pregap to set
   */
  public void setPregap(Position pregap)
  {
    this.pregap = pregap;
  }
  /**
   * @return the songwriter
   */
  public String getSongwriter()
  {
    return songwriter;
  }
  /**
   * @param songwriter the songwriter to set
   */
  public void setSongwriter(String songwriter)
  {
    this.songwriter = songwriter;
  }
  /**
   * @return the title
   */
  public String getTitle()
  {
    return title;
  }
  /**
   * @param title the title to set
   */
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  /**
   * Get the index with the specified number, or null if there is no such index. The current implementation is
   * unnecessarily inefficient if the indices are ordered. Currently, this is not enforced.
   * @param number
   * @return The index with the specified number, or null if there is no such index.
   */
  public Index getIndex(int number)
  {
    for (Index index : this.indices)
    {
      if (index.getNumber()==number)
      {
        return index;
      }
    }
    return null;
  }
  
  /**
   * @return the indices
   */
  public List<Index> getIndices()
  {
    return indices;
  }
  /**
   * @return the flags
   */
  public Set<String> getFlags()
  {
    return flags;
  }

  /**
   * @return the parent
   */
  public FileData getParent()
  {
    return parent;
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(FileData parent)
  {
    this.parent = parent;
  }
}
