package jwbroek.id3.v2.r00;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jwbroek.id3.ID3Tag;
import jwbroek.id3.ID3Reader;
import jwbroek.id3.ID3Version;
import jwbroek.id3.v2.MalformedFrameException;
import jwbroek.id3.v2.UnsupportedEncodingException;
import jwbroek.id3.v2.UnsynchedInputStream;

public class ID3v2r00Reader implements ID3Reader
{
  public ID3v2r00Reader()
  {
    
  }
  
  public boolean hasTag(final File file) throws IOException
  {
    final FileInputStream input = new FileInputStream(file);
    try
    {
      if  ( input.read() == 'I'
          && input.read() == 'D'
          && input.read() == '3'
          )
      {
        final int majorVersion = input.read();
        final int revision = input.read();
        if (majorVersion==2 && revision==0)
        {
          final int flags = input.read();
          // Don't trip over unsupported flags.
          // TODO Make switch for this behaviour.
          //if ((flags & 63) == 0)  // Only top two bits describe valid flags.
          //{
            for (int index = 0; index < 4; index++)
            {
              final int sizeByte = input.read();
              if (sizeByte >= 128)  // Top bit cannot be used.
              {
                return false;
              }
              return true;
            }
          //}
        }
      }
      return false;
    }
    finally
    {
      input.close();
    }
  }
  
  public ID3Tag read(final File file) throws IOException, UnsupportedEncodingException, MalformedFrameException
  {
    ID3Tag tag = new ID3Tag();
    
    final FileInputStream input = new FileInputStream(file);
    try
    {
      if  ( input.read() == 'I'
          && input.read() == 'D'
          && input.read() == '3'
          )
      {
        final int majorVersion = input.read();
        final int revision = input.read();
        if (majorVersion==2 && revision==0)
        {
          tag.setVersion(ID3Version.ID3v2r2);
          tag.setRevision(0);
          final int flags = input.read();
          final boolean unsyncUsed = (flags & 128) == 128;
          tag.getFlags().setProperty(ID3Tag.UNSYNC_USED, Boolean.toString(unsyncUsed));
          final boolean compressionUsed = (flags & 64) == 64;
          tag.getFlags().setProperty(ID3Tag.COMPRESSION_USED, Boolean.toString(compressionUsed));
          int size = 0;
          for (int index = 0; index < 4; index++)
          {
            final int sizeByte = input.read();
            if (sizeByte >= 128)
            {
              size = -1;
              break;
            }
            size = size * 128 + sizeByte;
          }
          if (size >= 0)
          {
            tag.setDeclaredSize(size);
            
            // Now to read the frames.
            final InputStream frameInputStream;
            if (unsyncUsed)
            {
              frameInputStream = new UnsynchedInputStream(input);
            }
            else
            {
              frameInputStream = input;
            }
            final FramesReader frameReader = new FramesReader();
            frameReader.readFrames(tag, frameInputStream, size);
          }
          else
          {
            // TODO Emit warning.
            // Invalid size byte encountered. Not a valid ID3 tag.
            tag = null;
          }
        }
        else
        {
          // TODO Emit warning.
          // Version and revision combination not supported.
          tag = null;
        }
      }
      else
      {
        // TODO Emit warning?
        // No valid tag found.
        tag = null;
      }
    }
    finally
    {
      input.close();
    }
    
    return tag;
  }
  
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    try
    {
      // For testing purposes...
      final ID3v2r00Reader reader = new ID3v2r00Reader();
      //final File file1 = new File("C:\\tmp\\01 Dead.mp3"); 
      //final File file1 = new File("C:\\tmp\\01 Prowler.mp3");
      final File file1 = new File("C:\\tmp\\06 United.mp3");
      System.out.println(reader.hasTag(file1));
      final ID3Tag tag = reader.read(file1);
      if (tag != null)
      {
        System.out.println(tag.toString());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
