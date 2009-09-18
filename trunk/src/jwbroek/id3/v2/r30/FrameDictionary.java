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
package jwbroek.id3.v2.r30;

import java.util.HashMap;
import java.util.Map;

import jwbroek.id3.CanonicalFrameType;

public class FrameDictionary
{
  private static Map<String, CanonicalFrameType> nameToType = new HashMap<String, CanonicalFrameType>();
  private static Map<CanonicalFrameType, String> typeToName = new HashMap<CanonicalFrameType, String>();
  
  static
  {
    FrameDictionary.addToDictionary("UFID", CanonicalFrameType.UNIQUE_FILE_IDENTIFIER);
    FrameDictionary.addToDictionary("TIT1", CanonicalFrameType.CONTENT_GROUP_DESCRIPTION);
    FrameDictionary.addToDictionary("TIT2", CanonicalFrameType.TITLE);
    FrameDictionary.addToDictionary("TIT3", CanonicalFrameType.SUBTITLE);
    FrameDictionary.addToDictionary("TPE1", CanonicalFrameType.PERFORMER);
    FrameDictionary.addToDictionary("TPE2", CanonicalFrameType.ACCOMPANYING_PERFORMER);
    FrameDictionary.addToDictionary("TPE3", CanonicalFrameType.CONDUCTOR);
    FrameDictionary.addToDictionary("TPE4", CanonicalFrameType.MODIFIED_BY);
    FrameDictionary.addToDictionary("TCOM", CanonicalFrameType.COMPOSERS);
    FrameDictionary.addToDictionary("TEXT", CanonicalFrameType.TEXT_WRITERS);
    FrameDictionary.addToDictionary("TLAN", CanonicalFrameType.LANGUAGES);
    FrameDictionary.addToDictionary("TCON", CanonicalFrameType.CONTENT_TYPE);
    FrameDictionary.addToDictionary("TALB", CanonicalFrameType.ALBUM);
    FrameDictionary.addToDictionary("TPOS", CanonicalFrameType.PART);
    FrameDictionary.addToDictionary("TRCK", CanonicalFrameType.TRACK_NO);
    FrameDictionary.addToDictionary("TSRC", CanonicalFrameType.ISRC);
    FrameDictionary.addToDictionary("TYER", CanonicalFrameType.YEAR);
    FrameDictionary.addToDictionary("TDAT", CanonicalFrameType.DATE);
    FrameDictionary.addToDictionary("TGID", CanonicalFrameType.ITUNES_PODCAST_URL);
    FrameDictionary.addToDictionary("TIME", CanonicalFrameType.TIME);
    FrameDictionary.addToDictionary("TRDA", CanonicalFrameType.RECORDING_DATES);
    FrameDictionary.addToDictionary("TMED", CanonicalFrameType.MEDIA_TYPE);
    FrameDictionary.addToDictionary("TFLT", CanonicalFrameType.FILE_TYPE);
    FrameDictionary.addToDictionary("TBPM", CanonicalFrameType.BEATS_PER_MINUTE);
    FrameDictionary.addToDictionary("TCOP", CanonicalFrameType.COPYRIGHT);
    FrameDictionary.addToDictionary("TPUB", CanonicalFrameType.PUBLISHER);
    FrameDictionary.addToDictionary("TENC", CanonicalFrameType.ENCODER);
    FrameDictionary.addToDictionary("TSSE", CanonicalFrameType.ENCODER_EQUIPMENT_OR_SETTINGS);
    FrameDictionary.addToDictionary("TOFN", CanonicalFrameType.ORIGINAL_FILE_NAME);
    FrameDictionary.addToDictionary("TLEN", CanonicalFrameType.TRACK_LENGTH_MS);
    FrameDictionary.addToDictionary("TSIZ", CanonicalFrameType.TRACK_SIZE_BYTES);
    FrameDictionary.addToDictionary("TDES", CanonicalFrameType.ITUNES_PODCAST_DESCRIPTION);
    FrameDictionary.addToDictionary("TDLY", CanonicalFrameType.PLAYLIST_DELAY_MS);
    FrameDictionary.addToDictionary("TKEY", CanonicalFrameType.INITIAL_KEY);
    FrameDictionary.addToDictionary("TOAL", CanonicalFrameType.ORIGINAL_TITLE);
    FrameDictionary.addToDictionary("TOPE", CanonicalFrameType.ORIGINAL_ARTISTS);
    FrameDictionary.addToDictionary("TOLY", CanonicalFrameType.ORIGINAL_TEXT_WRITERS);
    FrameDictionary.addToDictionary("TORY", CanonicalFrameType.ORIGINAL_YEAR);
    FrameDictionary.addToDictionary("TOWN", CanonicalFrameType.OWNER);
    FrameDictionary.addToDictionary("TRSN", CanonicalFrameType.RADIO_STATION);
    FrameDictionary.addToDictionary("TRSO", CanonicalFrameType.RADIO_STATION_OWNER);
    // Unofficial
    FrameDictionary.addToDictionary("TCAT", CanonicalFrameType.ITUNES_PODCAST_CATEGORY);
    // Unofficial
    FrameDictionary.addToDictionary("TDRL", CanonicalFrameType.ITUNES_PODCAST_RELEASE_TIME);
    // Unofficial
    FrameDictionary.addToDictionary("TKWD", CanonicalFrameType.ITUNES_PODCAST_KEYWORDS);
    FrameDictionary.addToDictionary("TXXX", CanonicalFrameType.CUSTOM_TEXT);
    FrameDictionary.addToDictionary("WOAF", CanonicalFrameType.OFFICIAL_AUDIO_FILE_WEBPAGE);
    FrameDictionary.addToDictionary("WOAR", CanonicalFrameType.OFFICIAL_ARTIST_WEBPAGE);
    FrameDictionary.addToDictionary("WOAS", CanonicalFrameType.OFFICIAL_AUDIO_SOURCE_WEBPAGE);
    FrameDictionary.addToDictionary("WCOM", CanonicalFrameType.COMMERCIAL_INFORMATION_URL);
    FrameDictionary.addToDictionary("WCOP", CanonicalFrameType.COPYRIGHT_OR_LEGAL_INFORMATION_URL);
    // Unofficial
    FrameDictionary.addToDictionary("WFED", CanonicalFrameType.ITUNES_PODCAST_FEED_URL);
    FrameDictionary.addToDictionary("WPUB", CanonicalFrameType.OFFICIAL_PUBLISHER_WEBPAGE);
    FrameDictionary.addToDictionary("WORS", CanonicalFrameType.OFFICIAL_RADIO_STATION_WEBPAGE);
    FrameDictionary.addToDictionary("WPAY", CanonicalFrameType.OFFICIAL_PAYMENT_WEBPAGE);
    FrameDictionary.addToDictionary("WXXX", CanonicalFrameType.CUSTOM_WEBPAGE);
    FrameDictionary.addToDictionary("IPLS", CanonicalFrameType.INVOLVED_PEOPLE_LIST);
    FrameDictionary.addToDictionary("MCDI", CanonicalFrameType.MUSIC_CD_IDENTIFIER);
    // TODO ETCO
    // TODO MLLT
    // TODO SYTC
    // TODO USLT
    // TODO SYLT
    FrameDictionary.addToDictionary("COMM", CanonicalFrameType.COMMENT);
    // TODO RVAD
    // TODO EQUA
    // TODO RVRB
    FrameDictionary.addToDictionary("APIC", CanonicalFrameType.PICTURE);
    // TODO GEOB
    // TODO PCNT
    // TODO POPM
    // TODO RBUF
    // TODO AENC
    // TODO LINK
    // TODO POSS
    // TODO USER
    // TODO OWNE
    // TODO COMR
    // TODO ENCR
    // TODO GRID
    // TODO PRIV
    FrameDictionary.addToDictionary("PCST", CanonicalFrameType.ITUNES_PODCAST);
    FrameDictionary.addToDictionary("XYZX", CanonicalFrameType.UNRECOGNISED_FRAME);
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
