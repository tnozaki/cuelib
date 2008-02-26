/*
 * Cuelib library for manipulating cue sheets.
 * Copyright (C) 2007 Jan-Willem van den Broek
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
  
  public TrackData()
  {
  }
  
  public TrackData(int number, String dataType)
  {
    this.number = number;
    this.dataType = dataType;
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
}
