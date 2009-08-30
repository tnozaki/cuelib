package jwbroek.id3.v2;

public class MalformedFrameException extends Exception
{
  /**
   * 
   */
  private static final long serialVersionUID = 6928538930151585090L;

  public MalformedFrameException()
  {
    super();
  }

  public MalformedFrameException(String message)
  {
    super(message);
  }

  public MalformedFrameException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public MalformedFrameException(Throwable cause)
  {
    super(cause);
  }
}
