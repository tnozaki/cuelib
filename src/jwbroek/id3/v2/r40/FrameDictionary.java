/*
 * Created on Aug 27, 2009
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
    // TODO APIC
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
    FrameDictionary.addToDictionary("TCOM", CanonicalFrameType.COMPOSERS);
    FrameDictionary.addToDictionary("TCON", CanonicalFrameType.CONTENT_TYPE);
    FrameDictionary.addToDictionary("TCOP", CanonicalFrameType.COPYRIGHT);
    // TODO TDEN
    // TODO TDLR
    FrameDictionary.addToDictionary("TDLY", CanonicalFrameType.PLAYLIST_DELAY_MS);
    FrameDictionary.addToDictionary("TDOR", CanonicalFrameType.ORIGINAL_RELEASE_TIME);
    FrameDictionary.addToDictionary("TDRC", CanonicalFrameType.RECORDING_TIME);
    // TODO TDTG
    FrameDictionary.addToDictionary("TENC", CanonicalFrameType.ENCODER);
    FrameDictionary.addToDictionary("TEXT", CanonicalFrameType.TEXT_WRITERS);
    FrameDictionary.addToDictionary("TFLT", CanonicalFrameType.FILE_TYPE);
    FrameDictionary.addToDictionary("TIPL", CanonicalFrameType.INVOLVED_PEOPLE_LIST);
    FrameDictionary.addToDictionary("TIT1", CanonicalFrameType.CONTENT_GROUP_DESCRIPTION);
    FrameDictionary.addToDictionary("TIT2", CanonicalFrameType.TITLE);
    FrameDictionary.addToDictionary("TIT3", CanonicalFrameType.SUBTITLE);
    FrameDictionary.addToDictionary("TKEY", CanonicalFrameType.INITIAL_KEY);
    FrameDictionary.addToDictionary("TLAN", CanonicalFrameType.LANGUAGES);
    FrameDictionary.addToDictionary("TLEN", CanonicalFrameType.TRACK_LENGTH_MS);
    FrameDictionary.addToDictionary("TMCL", CanonicalFrameType.MUSICIAN_CREDITS_LIST);
    FrameDictionary.addToDictionary("TMED", CanonicalFrameType.MEDIA_TYPE);
    // TODO TMOOD
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
    // TODO TPRO
    FrameDictionary.addToDictionary("TPUB", CanonicalFrameType.PUBLISHER);
    FrameDictionary.addToDictionary("TRCK", CanonicalFrameType.TRACK_NO);
    FrameDictionary.addToDictionary("TRSN", CanonicalFrameType.RADIO_STATION);
    FrameDictionary.addToDictionary("TRSO", CanonicalFrameType.RADIO_STATION_OWNER);
    // TODO TSOA
    // TODO TSOP
    // TODO TSOT
    FrameDictionary.addToDictionary("TSRC", CanonicalFrameType.ISRC);
    FrameDictionary.addToDictionary("TSSE", CanonicalFrameType.ENCODER_EQUIPMENT_OR_SETTINGS);
    // TODO TSST
    FrameDictionary.addToDictionary("TXXX", CanonicalFrameType.CUSTOM_TEXT);
    // TODO USER
    FrameDictionary.addToDictionary("UFID", CanonicalFrameType.UNIQUE_FILE_IDENTIFIER);
    // TODO USLT
    FrameDictionary.addToDictionary("WCOM", CanonicalFrameType.COMMERCIAL_INFORMATION_URL);
    FrameDictionary.addToDictionary("WCOP", CanonicalFrameType.COPYRIGHT_OR_LEGAL_INFORMATION_URL);
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
