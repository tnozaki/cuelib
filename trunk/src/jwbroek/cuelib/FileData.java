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

/**
 * Simple representation of a FILE block in a cue sheet.
 * @author jwbroek
 */
public class FileData
{
  private List<TrackData> trackData = new ArrayList<TrackData>();
  private String file = null;
  private String fileType = null;
  private CueSheet parent;
  
  public FileData(CueSheet parent)
  {
    this.parent = parent;
  }
  
  public FileData(CueSheet parent, String file, String fileType)
  {
    this.parent = parent;
    this.file = file;
    this.fileType = fileType;
  }

  /**
   * @return the file
   */
  public String getFile()
  {
    return file;
  }
  /**
   * @param file the file to set
   */
  public void setFile(String file)
  {
    this.file = file;
  }
  /**
   * @return the fileType
   */
  public String getFileType()
  {
    return fileType;
  }
  /**
   * @param fileType the fileType to set
   */
  public void setFileType(String fileType)
  {
    this.fileType = fileType;
  }
  /**
   * @return the trackData
   */
  public List<TrackData> getTrackData()
  {
    return trackData;
  }
  
  public List<Index> getAllIndices()
  {
    List<Index> allIndices = new ArrayList<Index>();
    
    for (TrackData trackData: this.trackData)
    {
      allIndices.addAll(trackData.getIndices());
    }
    
    return allIndices;
  }

  /**
   * @return the parent
   */
  public CueSheet getParent()
  {
    return parent;
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(CueSheet parent)
  {
    this.parent = parent;
  }
}
