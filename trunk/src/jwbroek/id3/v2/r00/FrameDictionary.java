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
package jwbroek.id3.v2.r00;

import java.util.HashMap;
import java.util.Map;

import jwbroek.id3.CanonicalFrameType;

public class FrameDictionary
{
  private static Map<String, CanonicalFrameType> nameToType = new HashMap<String, CanonicalFrameType>();
  private static Map<CanonicalFrameType, String> typeToName = new HashMap<CanonicalFrameType, String>();
  
  static
  {
    FrameDictionary.addToDictionary("UFI", CanonicalFrameType.UNIQUE_FILE_IDENTIFIER);
    FrameDictionary.addToDictionary("TT1", CanonicalFrameType.CONTENT_GROUP_DESCRIPTION);
    FrameDictionary.addToDictionary("TT2", CanonicalFrameType.TITLE);
    FrameDictionary.addToDictionary("TT3", CanonicalFrameType.SUBTITLE);
    FrameDictionary.addToDictionary("TP1", CanonicalFrameType.PERFORMER);
    FrameDictionary.addToDictionary("TP2", CanonicalFrameType.ACCOMPANYING_PERFORMER);
    FrameDictionary.addToDictionary("TP3", CanonicalFrameType.CONDUCTOR);
    FrameDictionary.addToDictionary("TP4", CanonicalFrameType.MODIFIED_BY);
    FrameDictionary.addToDictionary("TCM", CanonicalFrameType.COMPOSERS);
    FrameDictionary.addToDictionary("TXT", CanonicalFrameType.TEXT_WRITERS);
    FrameDictionary.addToDictionary("TLA", CanonicalFrameType.LANGUAGES);
    FrameDictionary.addToDictionary("TCO", CanonicalFrameType.CONTENT_TYPE);
    FrameDictionary.addToDictionary("TAL", CanonicalFrameType.ALBUM);
    FrameDictionary.addToDictionary("TPA", CanonicalFrameType.PART);
    FrameDictionary.addToDictionary("TRK", CanonicalFrameType.TRACK_NO);
    FrameDictionary.addToDictionary("TRC", CanonicalFrameType.ISRC);
    FrameDictionary.addToDictionary("TYE", CanonicalFrameType.YEAR);
    FrameDictionary.addToDictionary("TDA", CanonicalFrameType.DATE);
    FrameDictionary.addToDictionary("TDS", CanonicalFrameType.ITUNES_PODCAST_DESCRIPTION);
    FrameDictionary.addToDictionary("TIM", CanonicalFrameType.TIME);
    FrameDictionary.addToDictionary("TRD", CanonicalFrameType.RECORDING_DATES);
    FrameDictionary.addToDictionary("TMT", CanonicalFrameType.MEDIA_TYPE);
    FrameDictionary.addToDictionary("TFT", CanonicalFrameType.FILE_TYPE);
    FrameDictionary.addToDictionary("TBP", CanonicalFrameType.BEATS_PER_MINUTE);
    FrameDictionary.addToDictionary("TCR", CanonicalFrameType.COPYRIGHT);
    // Unofficial.
    FrameDictionary.addToDictionary("TCT", CanonicalFrameType.ITUNES_PODCAST_CATEGORY);
    FrameDictionary.addToDictionary("TDR", CanonicalFrameType.ITUNES_PODCAST_RELEASE_TIME);
    FrameDictionary.addToDictionary("TPB", CanonicalFrameType.PUBLISHER);
    FrameDictionary.addToDictionary("TEN", CanonicalFrameType.ENCODER);
    FrameDictionary.addToDictionary("TSS", CanonicalFrameType.ENCODER_EQUIPMENT_OR_SETTINGS);
    FrameDictionary.addToDictionary("TOF", CanonicalFrameType.ORIGINAL_FILE_NAME);
    FrameDictionary.addToDictionary("TLE", CanonicalFrameType.TRACK_LENGTH_MS);
    FrameDictionary.addToDictionary("TSI", CanonicalFrameType.TRACK_SIZE_BYTES);
    FrameDictionary.addToDictionary("TDY", CanonicalFrameType.PLAYLIST_DELAY_MS);
    FrameDictionary.addToDictionary("TKE", CanonicalFrameType.INITIAL_KEY);
    FrameDictionary.addToDictionary("TOT", CanonicalFrameType.ORIGINAL_TITLE);
    FrameDictionary.addToDictionary("TOA", CanonicalFrameType.ORIGINAL_ARTISTS);
    FrameDictionary.addToDictionary("TOL", CanonicalFrameType.ORIGINAL_TEXT_WRITERS);
    FrameDictionary.addToDictionary("TOR", CanonicalFrameType.ORIGINAL_YEAR);
    // Unofficial
    FrameDictionary.addToDictionary("TID", CanonicalFrameType.ITUNES_PODCAST_URL);
    // Unofficial
    FrameDictionary.addToDictionary("TKW", CanonicalFrameType.ITUNES_PODCAST_KEYWORDS);
    FrameDictionary.addToDictionary("TXX", CanonicalFrameType.CUSTOM_TEXT);
    FrameDictionary.addToDictionary("WAF", CanonicalFrameType.OFFICIAL_AUDIO_FILE_WEBPAGE);
    FrameDictionary.addToDictionary("WAR", CanonicalFrameType.OFFICIAL_ARTIST_WEBPAGE);
    FrameDictionary.addToDictionary("WAS", CanonicalFrameType.OFFICIAL_AUDIO_SOURCE_WEBPAGE);
    // Unofficial
    FrameDictionary.addToDictionary("WFD", CanonicalFrameType.ITUNES_PODCAST_FEED_URL);
    FrameDictionary.addToDictionary("WCM", CanonicalFrameType.COMMERCIAL_INFORMATION_URL);
    FrameDictionary.addToDictionary("WCP", CanonicalFrameType.COPYRIGHT_OR_LEGAL_INFORMATION_URL);
    FrameDictionary.addToDictionary("WPB", CanonicalFrameType.OFFICIAL_PUBLISHER_WEBPAGE);
    FrameDictionary.addToDictionary("WXX", CanonicalFrameType.CUSTOM_WEBPAGE);
    FrameDictionary.addToDictionary("IPL", CanonicalFrameType.INVOLVED_PEOPLE_LIST);
    FrameDictionary.addToDictionary("MCI", CanonicalFrameType.MUSIC_CD_IDENTIFIER);
    // TODO ETC
    // TODO MLL
    // TODO STC
    // TODO ULT
    // TODO SLT
    FrameDictionary.addToDictionary("COM", CanonicalFrameType.COMMENT);
    // TODO RVA
    // TODO EQU
    // TODO REV
    FrameDictionary.addToDictionary("PIC", CanonicalFrameType.PICTURE);
    // TODO GEO
    // TODO CNT
    // TODO POP
    // TODO BUF
    // TODO CRM
    // TODO CRA
    // TODO LNK
    FrameDictionary.addToDictionary("PCS", CanonicalFrameType.ITUNES_PODCAST);
    FrameDictionary.addToDictionary("XYZ", CanonicalFrameType.UNRECOGNISED_FRAME);
  }
  
  public FrameDictionary()
  {
    
  }
  
  private static void addToDictionary(final String name, final CanonicalFrameType canonicalFrameType)
  {
    FrameDictionary.nameToType.put(name, canonicalFrameType);
    FrameDictionary.typeToName.put(canonicalFrameType, name);
  }
  
  public CanonicalFrameType getCanonicalFrameType(final String name)
  {
    return FrameDictionary.nameToType.get(name);
  }

  public String getName(final CanonicalFrameType canonicalFrameType)
  {
    return FrameDictionary.typeToName.get(canonicalFrameType);
  }
}
