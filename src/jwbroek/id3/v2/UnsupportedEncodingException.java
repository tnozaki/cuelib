package jwbroek.id3.v2;

public class UnsupportedEncodingException extends Exception
{
  /**
   * 
   */
  private static final long serialVersionUID = 876206055952819005L;

  public UnsupportedEncodingException()
  {
    super();
  }
  
  public UnsupportedEncodingException(final Exception e)
  {
    super(e);
  }

  public UnsupportedEncodingException(final String message)
  {
    super(message);
  }
  
  public UnsupportedEncodingException(final String message, final Exception e)
  {
    super(message, e);
  }
}
