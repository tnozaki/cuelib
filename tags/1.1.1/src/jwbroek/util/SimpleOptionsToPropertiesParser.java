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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Simple parser for putting arguments (typically command line arguments) into a Properties instance.
 * @author jwbroek
 */
public class SimpleOptionsToPropertiesParser
{
  // Map from option key to a list of property keys.
  Map<String, List<String>> argumentData = new HashMap<String, List<String>>();
  
  public SimpleOptionsToPropertiesParser()
  {
    // Nothing to do. Could have used the default constructor, but I like to be explicit.
  }
  
  /**
   * <p>Register an option with the parser. If the option is found, then the specified properties will be set to the
   * values following the option. The first property key is special in that it will be set to the option key that was
   * parsed. This property key may be null.</p>
   * 
   * <p>For instance, suppose that optionKey is "-add" and that propertyKeys is ["OPERATION", "FIRST",
   * "SECOND"]. In this case, after parsing ["-add", "1", "5"], the property "OPERATION" will be set to
   * "-add", property "FIRST" will be set to "1", and property "SECOND" will be set to "5".</p>
   * @param optionKey The option to be registered. For instance "-a".
   * @param propertyKeys The property keys to set when the option is parsed. 
   */
  public void registerOption(String optionKey, String ... propertyKeys)
  {
    this.argumentData.put(optionKey, Arrays.asList(propertyKeys));
  }
  
  /**
   * <p>Register an option with the parser. If the option is found, then the specified properties will be set to the
   * values following the option. The first property key is special in that it will be set to the option key that was
   * parsed. This property key may be null.</p>
   * 
   * <p>For instance, suppose that optionKey is "-add" and that propertyKeys is {"OPERATION", "FIRST",
   * "SECOND"}. In this case, after parsing ["-add", "1", "5"], the property "OPERATION" will be set to
   * "-add", property "FIRST" will be set to "1", and property "SECOND" will be set to "5".</p>
   * @param optionKey The option to be registered. For instance "-a".
   * @param propertyKeys The property keys to set when the option is parsed. 
   */
  public void registerOption(String optionKey, List<String> propertyKeys)
  {
    this.argumentData.put(optionKey, propertyKeys);
  }

  /**
   * <p>Register an option with the parser. If one of the alternatives for the option is found, then the specified
   * properties will be set to the values following the option. The first property key is special in that it will be
   * set to the option key that was parsed. This property key may be null.</p>
   * 
   * <p>For instance, suppose that optionKeyAlternatives is {"-a", "-add", "-add-numbers"} and that propertyKeys is
   * {"OPERATION", "FIRST", "SECOND"}. In this case, after parsing ["-add", "1", "5"], the property "OPERATION" will be
   *  set to "-add", property "FIRST" will be set to "1", and property "SECOND" will be set to "5".</p>
   * @param optionKeyAlternatives A list of option keys that indicate the same option. For instance {"-a", "-add",
   * "-add-numbers"}.
   * @param propertyKeys The property keys to set when the option is parsed. 
   */
  public void registerOption(List<String> optionKeyAlternatives, List<String> propertyKeys)
  {
    for (String optionKey : optionKeyAlternatives)
    {
      registerOption(optionKey, propertyKeys);
    }
  }
  
  /**
   * <p>Register an option with the parser. If one of the alternatives for the option is found, then the specified
   * properties will be set to the values following the option. The first property key is special in that it will be
   * set to the option key that was parsed. This property key may be null.</p>
   * 
   * <p>For instance, suppose that optionKeyAlternatives is ["-a", "-add", "-add-numbers"] and that propertyKeys is
   * ["OPERATION", "FIRST", "SECOND"]. In this case, after parsing ["-add", "1", "5"], the property "OPERATION" will be
   *  set to "-add", property "FIRST" will be set to "1", and property "SECOND" will be set to "5".</p>
   * @param optionKeyAlternatives An array of option keys that indicate the same option. For instance ["-a", "-add",
   * "-add-numbers"].
   * @param propertyKeys The property keys to set when the option is parsed. 
   */
  public void registerOption(String [] optionKeyAlternatives, String ... propertyKeys)
  {
    for (String optionKey : optionKeyAlternatives)
    {
      registerOption(optionKey, propertyKeys);
    }
  }

  /**
   * Parse the previously registered options.
   * @param options The options to parse. Each element will be considered an atomic option element.
   * @param properties The Properties instance in which the keys will be set that were specified when the options
   * were registered.
   * @return The index of the first option that could not be matched, options.length if everything was matched, or -1
   * if there was a problem.
   */
  public int parseOptions(String [] options, Properties properties)
  {
    return parseOptions(options, 0, options.length, properties);
  }

  /**
   * Parse the previously registered options.
   * @param options The options to parse. Each element will be considered an atomic option element.
   * @param beginIndex First option element to parse.
   * @param endIndex Only parse option elements up to (excluding) this index.
   * @param properties The Properties instance in which the keys will be set that were specified when the options
   * were registered.
   * @return The index of the first option that could not be matched, endIndex if everything was matched, or -1 if
   * there was a problem.
   */
  public int parseOptions(String [] options, int beginIndex, int endIndex, Properties properties)
  {
    for (int optionIndex = beginIndex; optionIndex < endIndex; optionIndex++)
    {
      List<String> propertyKeys = this.argumentData.get(options[optionIndex]);
      
      if (propertyKeys==null)
      {
        // Unknown option.
        return optionIndex;
      }
      
      // We don't count the first optionValueKey as that one corresponds to the option itself. So, we're actually
      // checking !(optionIndex + propertyKeys.size() - 1 < endIndex), which is
      // optionIndex + propertyKeys.size() - 1 >= endIndex, which is optionIndex + propertyKeys.size() > endIndex.
      if (optionIndex + propertyKeys.size() > endIndex)
      {
        // Insufficient values.
        return -1;
      }
      
      // If the first optionValueKeys value is present, then we'll set this property to the parsed option key.
      if (propertyKeys.get(0)!=null)
      {
        properties.setProperty(propertyKeys.get(0), options[optionIndex]);
      }
      
      // Set the various property keys to the specified values.
      for (int propertyKeyIndex = 1; propertyKeyIndex < propertyKeys.size(); propertyKeyIndex++)
      {
        properties.setProperty(propertyKeys.get(propertyKeyIndex), options[++optionIndex]);
      }
    }
    
    return endIndex;
  }
}
