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

/**
 * Implementation of the Message interface.
 * @author jwbroek
 */
public abstract class MessageImplementation implements Message
{
  private String input;
  private int lineNumber;
  private String message;
  private String type;
  
  public MessageImplementation(String type)
  {
    this.input = "";
    this.lineNumber = -1;
    this.message = "";
    this.type = type;
  }
  
  public MessageImplementation(String type, LineOfInput lineOfInput, String message)
  {
    this.input = lineOfInput.getInput();
    this.lineNumber = lineOfInput.getLineNumber();
    this.message = message;
    this.type = type;
  }

  public MessageImplementation(String type, String input, int lineNumber, String message)
  {
    this.input = input;
    this.lineNumber = lineNumber;
    this.message = message;
    this.type = type;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder(input).append('\n');
    builder.append(type).append(" [Line ").append(lineNumber).append("] ").append(message).append('\n');
    return builder.toString();
  }

  /**
   * @return the input
   */
  public String getInput()
  {
    return input;
  }

  /**
   * @param input the input to set
   */
  public void setInput(String input)
  {
    this.input = input;
  }

  /**
   * @return the lineNumber
   */
  public int getLineNumber()
  {
    return lineNumber;
  }

  /**
   * @param lineNumber the lineNumber to set
   */
  public void setLineNumber(int lineNumber)
  {
    this.lineNumber = lineNumber;
  }

  /**
   * @return the message
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * @param message the message to set
   */
  public void setMessage(String message)
  {
    this.message = message;
  }
}
