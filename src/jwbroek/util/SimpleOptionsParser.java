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
package jwbroek.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple parser for options (typically command line arguments).
 * @author jwbroek
 */
public class SimpleOptionsParser
{
  public interface OptionHandler
  {
    /**
     * Handle the option found at the specified offset in the options list. The handler must handle at least the option
     * at the offset. If it cannot, it must throw an Exception. It may handle more than one option.
     * @param options
     * @param offset
     * @return The index of the last option parsed + 1. The handler must handle at least the option at the offset. If
     * it cannot, it must throw an Exception.
     */
    public int handleOption(String [] options, int offset);
  }
  
  // Map from option key to handler for that option.
  Map<String, OptionHandler> optionHandlers = new HashMap<String, OptionHandler>();
  
  public SimpleOptionsParser()
  {
    // Nothing to do. Could have used the default constructor, but I like to be explicit.
  }
  
  /**
   * <p>Register an option with the parser. If the option is found, then the specified handler will be called.</p>
   * @param optionKey The option to be registered. For instance "-a".
   * @param handler The handler to handle this option.
   * @deprecated The prefered method is {@link #registerOption(jwbroek.util.SimpleOptionsParser.OptionHandler, String[])}.
   */
  public void registerOption(String optionKey, OptionHandler handler)
  {
    this.optionHandlers.put(optionKey, handler);
  }
  
  /**
   * <p>Register options with identical handler with the parser. When one of the the options is found,
   * the specified handler will be called.</p>
   * @param optionKeys The options to be registered. For instance "-a", "--alpha".
   * @param handler The handler to handle these options. 
   */
  public void registerOption(OptionHandler handler, String ... optionKeys)
  {
    for (String optionKey : optionKeys)
    {
      this.optionHandlers.put(optionKey, handler);
    }
  }
  
  /**
   * Parse the previously registered options.
   * @param options The options to parse. Each element will be considered an atomic option element.
   * @return The index of the first option that could not be matched, or options.length if everything was matched.
   */
  public int parseOptions(final String [] options)
  {
    return parseOptions(options, 0);
  }

  /**
   * Parse the previously registered options.
   * @param options The options to parse. Each element will be considered an atomic option element.
   * @param offset First option element to parse.
   * @return The index of the first option that could not be matched, or options.length if everything was matched.
   */
  public int parseOptions(final String [] options, final int offset)
  {
    int currentOffset = offset;
    OptionHandler currentHandler = null;
    
    while (currentOffset < options.length)
    {
      currentHandler = this.optionHandlers.get(options[currentOffset]);
      
      // Can't handle this option.
      if (currentHandler==null)
      {
        return currentOffset;
      }
      
      int nextOffset = currentHandler.handleOption(options, currentOffset);
      
      // Handler did not handle at least the first option. This is an exception state, as it would lead
      // to an infinite loop.
      if (nextOffset==currentOffset)
      {
        throw new IllegalStateException ( "Handler registered for option \""
                                        + options[currentOffset]
                                        + "\" did not handle it."
                                        );
      }
      
      // Handler claims to have handled more options than actually exist.
      if (nextOffset > options.length)
      {
        throw new IllegalStateException ( "Handler registered for option \""
                                        + options[currentOffset]
                                        + "\" claims to have handled more options than are existent."
                                        );
      }
      
      currentOffset = nextOffset;
    }
    
    return currentOffset;
  }
}
