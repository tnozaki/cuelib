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
package jwbroek.id3.v2.r40;

import java.util.HashMap;
import java.util.Map;

import jwbroek.id3.CanonicalFrameType;

public class FrameDictionary
{
  private static Map<String, CanonicalFrameType> nameToType = new HashMap<String, CanonicalFrameType>();
  private static Map<CanonicalFrameType, String> typeToName = new HashMap<CanonicalFrameType, String>();
  
  static
  {
    // TODO AENC
    FrameDictionary.addToDictionary("APIC", CanonicalFrameType.PICTURE);
    // TODO ASPI
    FrameDictionary.addToDictionary("COMM", CanonicalFrameType.COMMENT);
    // TODO COMR
    // TODO ENCR
    // TODO EQU2
    // TODO ETCO
    // TODO GEOB
    // TODO GRID
    // TODO LINK
    FrameDictionary.addToDictionary("MCDI", CanonicalFrameType.MUSIC_CD_IDENTIFIER);
    // TODO MLLT
    // TODO OWNE
    // TODO PCNT
    FrameDictionary.addToDictionary("PCST", CanonicalFrameType.ITUNES_PODCAST);
    // TODO POPM
    // TODO POSS
    // TODO PRIV
    // TODO RBUF
    // TODO RVA2
    // TODO RVRB
    // TODO SEEK
    // TODO SIGN
    // TODO SYLT
    // TODO SYTC
    FrameDictionary.addToDictionary("TALB", CanonicalFrameType.ALBUM);
    FrameDictionary.addToDictionary("TBPM", CanonicalFrameType.BEATS_PER_MINUTE);
    // Unofficial
    FrameDictionary.addToDictionary("TCAT", CanonicalFrameType.ITUNES_PODCAST_CATEGORY);
    FrameDictionary.addToDictionary("TCOM", CanonicalFrameType.COMPOSERS);
    FrameDictionary.addToDictionary("TCON", CanonicalFrameType.CONTENT_TYPE);
    FrameDictionary.addToDictionary("TCOP", CanonicalFrameType.COPYRIGHT);
    FrameDictionary.addToDictionary("TDEN", CanonicalFrameType.ENCODING_TIMESTAMP);
    // Unofficial
    FrameDictionary.addToDictionary("TDES", CanonicalFrameType.ITUNES_PODCAST_DESCRIPTION);
    FrameDictionary.addToDictionary("TDLY", CanonicalFrameType.PLAYLIST_DELAY_MS);
    FrameDictionary.addToDictionary("TDOR", CanonicalFrameType.ORIGINAL_RELEASE_TIME);
    FrameDictionary.addToDictionary("TDRC", CanonicalFrameType.RECORDING_TIME);
    // Unofficial
    FrameDictionary.addToDictionary("TDRL", CanonicalFrameType.ITUNES_PODCAST_RELEASE_TIME);
    FrameDictionary.addToDictionary("TDTG", CanonicalFrameType.TAGGING_TIMESTAMP);
    FrameDictionary.addToDictionary("TENC", CanonicalFrameType.ENCODER);
    FrameDictionary.addToDictionary("TEXT", CanonicalFrameType.TEXT_WRITERS);
    FrameDictionary.addToDictionary("TFLT", CanonicalFrameType.FILE_TYPE);
    // Unofficial
    FrameDictionary.addToDictionary("TGID", CanonicalFrameType.ITUNES_PODCAST_URL);
    FrameDictionary.addToDictionary("TIPL", CanonicalFrameType.INVOLVED_PEOPLE_LIST);
    FrameDictionary.addToDictionary("TIT1", CanonicalFrameType.CONTENT_GROUP_DESCRIPTION);
    FrameDictionary.addToDictionary("TIT2", CanonicalFrameType.TITLE);
    FrameDictionary.addToDictionary("TIT3", CanonicalFrameType.SUBTITLE);
    FrameDictionary.addToDictionary("TKEY", CanonicalFrameType.INITIAL_KEY);
    // Unofficial
    FrameDictionary.addToDictionary("TKWD", CanonicalFrameType.ITUNES_PODCAST_KEYWORDS);
    FrameDictionary.addToDictionary("TLAN", CanonicalFrameType.LANGUAGES);
    FrameDictionary.addToDictionary("TLEN", CanonicalFrameType.TRACK_LENGTH_MS);
    FrameDictionary.addToDictionary("TMCL", CanonicalFrameType.MUSICIAN_CREDITS_LIST);
    FrameDictionary.addToDictionary("TMED", CanonicalFrameType.MEDIA_TYPE);
    FrameDictionary.addToDictionary("TMOO", CanonicalFrameType.MOOD);
    FrameDictionary.addToDictionary("TOAL", CanonicalFrameType.ORIGINAL_TITLE);
    FrameDictionary.addToDictionary("TOFN", CanonicalFrameType.ORIGINAL_FILE_NAME);
    FrameDictionary.addToDictionary("TOLY", CanonicalFrameType.ORIGINAL_TEXT_WRITERS);
    FrameDictionary.addToDictionary("TOPE", CanonicalFrameType.ORIGINAL_ARTISTS);
    FrameDictionary.addToDictionary("TOWN", CanonicalFrameType.OWNER);
    FrameDictionary.addToDictionary("TPE1", CanonicalFrameType.PERFORMER);
    FrameDictionary.addToDictionary("TPE2", CanonicalFrameType.ACCOMPANYING_PERFORMER);
    FrameDictionary.addToDictionary("TPE3", CanonicalFrameType.CONDUCTOR);
    FrameDictionary.addToDictionary("TPE4", CanonicalFrameType.MODIFIED_BY);
    FrameDictionary.addToDictionary("TPOS", CanonicalFrameType.PART);
    FrameDictionary.addToDictionary("TPRO", CanonicalFrameType.PRODUCED_NOTE);
    FrameDictionary.addToDictionary("TPUB", CanonicalFrameType.PUBLISHER);
    FrameDictionary.addToDictionary("TRCK", CanonicalFrameType.TRACK_NO);
    FrameDictionary.addToDictionary("TRSN", CanonicalFrameType.RADIO_STATION);
    FrameDictionary.addToDictionary("TRSO", CanonicalFrameType.RADIO_STATION_OWNER);
    FrameDictionary.addToDictionary("TSOA", CanonicalFrameType.ALBUM_SORT_ORDER);
    FrameDictionary.addToDictionary("TSOP", CanonicalFrameType.PERFORMER_SORT_ORDER);
    FrameDictionary.addToDictionary("TSOT", CanonicalFrameType.TITLE_SORT_ORDER);
    FrameDictionary.addToDictionary("TSRC", CanonicalFrameType.ISRC);
    FrameDictionary.addToDictionary("TSSE", CanonicalFrameType.ENCODER_EQUIPMENT_OR_SETTINGS);
    FrameDictionary.addToDictionary("TSSE", CanonicalFrameType.SET_SUBTITLE);
    FrameDictionary.addToDictionary("TXXX", CanonicalFrameType.CUSTOM_TEXT);
    // TODO USER
    FrameDictionary.addToDictionary("UFID", CanonicalFrameType.UNIQUE_FILE_IDENTIFIER);
    // TODO USLT
    FrameDictionary.addToDictionary("WCOM", CanonicalFrameType.COMMERCIAL_INFORMATION_URL);
    FrameDictionary.addToDictionary("WCOP", CanonicalFrameType.COPYRIGHT_OR_LEGAL_INFORMATION_URL);
    // Unofficial
    FrameDictionary.addToDictionary("WFED", CanonicalFrameType.ITUNES_PODCAST_FEED_URL);
    FrameDictionary.addToDictionary("WOAF", CanonicalFrameType.OFFICIAL_AUDIO_FILE_WEBPAGE);
    FrameDictionary.addToDictionary("WOAR", CanonicalFrameType.OFFICIAL_ARTIST_WEBPAGE);
    FrameDictionary.addToDictionary("WOAS", CanonicalFrameType.OFFICIAL_AUDIO_SOURCE_WEBPAGE);
    FrameDictionary.addToDictionary("WORS", CanonicalFrameType.OFFICIAL_RADIO_STATION_WEBPAGE);
    FrameDictionary.addToDictionary("WPAY", CanonicalFrameType.OFFICIAL_PAYMENT_WEBPAGE);
    FrameDictionary.addToDictionary("WPUB", CanonicalFrameType.OFFICIAL_PUBLISHER_WEBPAGE);
    FrameDictionary.addToDictionary("WXXX", CanonicalFrameType.CUSTOM_WEBPAGE);
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
