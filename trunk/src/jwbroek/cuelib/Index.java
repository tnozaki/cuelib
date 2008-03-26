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
 * Simple representation of an INDEX datum in a cue sheet.
 * @author jwbroek
 */
public class Index
{
  private int number = -1;
  private Position position = null;
  
  public Index()
  {
  }

  public Index(int number, Position position)
  {
    this.number = number;
    this.position = position;
  }

  /**
   * @return the number
   */
  public int getNumber()
  {
    return number;
  }

  /**
   * @param number the number to set
   */
  public void setNumber(int number)
  {
    this.number = number;
  }

  /**
   * @return the position
   */
  public Position getPosition()
  {
    return position;
  }

  /**
   * @param position the position to set
   */
  public void setPosition(Position position)
  {
    this.position = position;
  }
}
