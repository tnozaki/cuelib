package jwbroek.id3.v2;

public class UnsupportedFrameTypeException extends RuntimeException
{
  /**
   * 
   */
  private static final long serialVersionUID = -1545143968507887751L;

  public UnsupportedFrameTypeException()
  {
    super();
  }
  
  public UnsupportedFrameTypeException(final Exception e)
  {
    super(e);
  }

  public UnsupportedFrameTypeException(final String message)
  {
    super(message);
  }
  
  public UnsupportedFrameTypeException(final String message, final Exception e)
  {
    super(message, e);
  }
}
