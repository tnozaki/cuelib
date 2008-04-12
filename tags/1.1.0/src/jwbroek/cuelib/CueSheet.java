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
 * Simple representation of a cue sheet.
 * @author jwbroek
 */
public class CueSheet
{
  public enum MetaDataField
  {
    ALBUMPERFORMER,
    ALBUMSONGWRITER,
    ALBUMTITLE,
    CATALOG,
    CDTEXTFILE,
    COMMENT,
    DISCID,
    GENRE,
    ISRCCODE,
    PERFORMER,
    SONGWRITER,
    TITLE,
    TRACKNUMBER,
    TRACKPERFORMER,
    TRACKSONGWRITER,
    TRACKTITLE,
    YEAR
  }
  
  private List<Message> messages = new ArrayList<Message>();
  private List<FileData> fileData = new ArrayList<FileData>();
  private String catalog = null;
  private String cdTextFile = null;
  private String performer = null;
  private String title = null;
  private String songwriter = null;
  private String comment = null;
  private int year = -1;
  private String discid = null;
  private String genre = null;
  
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
      case CATALOG:
        return this.getCatalog()==null?"":this.getCatalog();
      case CDTEXTFILE:
        return this.getCdTextFile()==null?"":this.getCdTextFile();
      case COMMENT:
        return this.getComment()==null?"":this.getComment();
      case DISCID:
        return this.getDiscid()==null?"":this.getDiscid();
      case GENRE:
        return this.getGenre()==null?"":this.getGenre();
      case PERFORMER:
      case ALBUMPERFORMER:
        return this.getPerformer()==null?"":this.getPerformer();
      case SONGWRITER:
      case ALBUMSONGWRITER:
        return this.getSongwriter()==null?"":this.getSongwriter();
      case TITLE:
      case ALBUMTITLE:
        return this.getTitle()==null?"":this.getTitle();
      case YEAR:
        return this.getYear()==-1?"":""+this.getYear();
      default:
        throw new IllegalArgumentException();
    }
  }
  
  /**
   * @return the discid
   */
  public String getDiscid()
  {
    return discid;
  }

  /**
   * @param discid the discid to set
   */
  public void setDiscid(String discid)
  {
    this.discid = discid;
  }

  /**
   * @return the genre
   */
  public String getGenre()
  {
    return genre;
  }

  /**
   * @param genre the genre to set
   */
  public void setGenre(String genre)
  {
    this.genre = genre;
  }

  /**
   * @return the year
   */
  public int getYear()
  {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(int year)
  {
    this.year = year;
  }

  /**
   * @return the comment
   */
  public String getComment()
  {
    return comment;
  }

  /**
   * @param comment the comment to set
   */
  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public CueSheet()
  {
    // Intentionally empty.
  }
  
  /**
   * Add an error message to this cue sheet.
   * @param lineOfInput The line of input that caused the error.
   * @param message A message describing the error.
   */
  public void addError(LineOfInput lineOfInput, String message)
  {
    this.messages.add(new Error(lineOfInput, message));
  }
  
  /**
   * Add a warning message to this cue sheet.
   * @param lineOfInput The line of input that caused the warning.
   * @param message A message describing the warning.
   */
  public void addWarning(LineOfInput lineOfInput, String message)
  {
    this.messages.add(new Warning(lineOfInput, message));
  }
  
  /**
   * @return the catalog
   */
  public String getCatalog()
  {
    return catalog;
  }
  /**
   * @param catalog the catalog to set
   */
  public void setCatalog(String catalog)
  {
    this.catalog = catalog;
  }
  /**
   * @return the cdTextFile
   */
  public String getCdTextFile()
  {
    return cdTextFile;
  }
  /**
   * @param cdTextFile the cdTextFile to set
   */
  public void setCdTextFile(String cdTextFile)
  {
    this.cdTextFile = cdTextFile;
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
   * @return the fileData
   */
  public List<FileData> getFileData()
  {
    return fileData;
  }
  /**
   * @return the messages
   */
  public List<Message> getMessages()
  {
    return messages;
  }
  
  /**
   * Get all track data described in this cue sheet.
   * @return All track data associated described in this cue sheet.
   */
  public List<TrackData> getAllTrackData()
  {
    List<TrackData> allTrackData = new ArrayList<TrackData>();
    
    for (FileData fileData: this.fileData)
    {
      allTrackData.addAll(fileData.getTrackData());
    }
    
    return allTrackData;
  }
}
