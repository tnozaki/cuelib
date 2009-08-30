package jwbroek.id3.v2.r40;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class FrameHeader
{
  private String name = null;
  private int size = -1;
  private boolean preserveFrameWhenTagAltered = true;
  private boolean preserveFrameWhenFileAltered = true;
  private boolean readOnly = false;
  private boolean compressionUsed = false;
  private long decompressedSize = -1;
  private boolean encryptionUsed = false;
  private int encryptionMethodUsed = -1;
  private boolean containsGroupInformation = false;
  private int groupId = -1;
  
  private final static Set<String> discardWhenFileAltered
    = new TreeSet<String>(Arrays.asList(new String []
      { "ASPI", "AENC", "ETCO"
      , "EQU2", "MLLT", "POSS"
      , "SEEK", "SYLT", "SYTC"
      , "RVA2", "TENC", "TLEN"
      }));
  
  public FrameHeader(final String name, final int size, final int flags)
  {
    this.name = name;
    this.size = size;
    this.preserveFrameWhenTagAltered = (flags & 65536) == 65536;
    this.preserveFrameWhenFileAltered
      = FrameHeader.discardWhenFileAltered.contains(name)
      || (flags & 32768) == 32768;
    this.readOnly = (flags & 16384) == 16384;
    this.compressionUsed = (flags & 256) == 256;
    this.encryptionUsed = (flags & 128) == 128;
    this.containsGroupInformation = (flags & 64) == 64;
  }

  /**
   * @return Returns the compressionUsed.
   */
  public boolean getCompressionUsed()
  {
    return compressionUsed;
  }

  /**
   * @param compressionUsed The compressionUsed to set.
   */
  public void setCompressionUsed(final boolean compressionUsed)
  {
    this.compressionUsed = compressionUsed;
  }

  /**
   * @return Returns the containsGroupInformation.
   */
  public boolean getContainsGroupInformation()
  {
    return containsGroupInformation;
  }

  /**
   * @param containsGroupInformation The containsGroupInformation to set.
   */
  public void setContainsGroupInformation(final boolean containsGroupInformation)
  {
    this.containsGroupInformation = containsGroupInformation;
  }

  /**
   * @return Returns the decompressedSize.
   */
  public long getDecompressedSize()
  {
    return decompressedSize;
  }

  /**
   * @param decompressedSize The decompressedSize to set.
   */
  public void setDecompressedSize(final long decompressedSize)
  {
    this.decompressedSize = decompressedSize;
  }

  /**
   * @return Returns the encryptionMethodUsed.
   */
  public int getEncryptionMethodUsed()
  {
    return encryptionMethodUsed;
  }

  /**
   * @param encryptionMethodUsed The encryptionMethodUsed to set.
   */
  public void setEncryptionMethodUsed(final int encryptionMethodUsed)
  {
    this.encryptionMethodUsed = encryptionMethodUsed;
  }

  /**
   * @return Returns the encryptionUsed.
   */
  public boolean getEncryptionUsed()
  {
    return encryptionUsed;
  }

  /**
   * @param encryptionUsed The encryptionUsed to set.
   */
  public void setEncryptionUsed(final boolean encryptionUsed)
  {
    this.encryptionUsed = encryptionUsed;
  }

  /**
   * @return Returns the groupId.
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * @param groupId The groupId to set.
   */
  public void setGroupId(final int groupId)
  {
    this.groupId = groupId;
  }

  /**
   * @return Returns the preserveFrameWhenFileAltered.
   */
  public boolean getPreserveFrameWhenFileAltered()
  {
    return preserveFrameWhenFileAltered;
  }

  /**
   * @param preserveFrameWhenFileAltered The preserveFrameWhenFileAltered to set.
   */
  public void setPreserveFrameWhenFileAltered(final boolean preserveFrameWhenFileAltered)
  {
    this.preserveFrameWhenFileAltered = preserveFrameWhenFileAltered;
  }

  /**
   * @return Returns the preserveFrameWhenTagAltered.
   */
  public boolean getPreserveFrameWhenTagAltered()
  {
    return preserveFrameWhenTagAltered;
  }

  /**
   * @param preserveFrameWhenTagAltered The preserveFrameWhenTagAltered to set.
   */
  public void setPreserveFrameWhenTagAltered(final boolean preserveFrameWhenTagAltered)
  {
    this.preserveFrameWhenTagAltered = preserveFrameWhenTagAltered;
  }

  /**
   * @return Returns the readOnly.
   */
  public boolean getReadOnly()
  {
    return readOnly;
  }

  /**
   * @param readOnly The readOnly to set.
   */
  public void setReadOnly(final boolean readOnly)
  {
    this.readOnly = readOnly;
  }

  /**
   * @return Returns the name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name The name to set.
   */
  public void setName(final String name)
  {
    this.name = name;
  }

  /**
   * @return Returns the size.
   */
  public int getSize()
  {
    return size;
  }

  /**
   * @param size The size to set.
   */
  public void setSize(final int size)
  {
    this.size = size;
  }
}
