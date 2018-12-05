/*
 * Copyright (c) 2016 Open Baton (http://www.openbaton.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.openbaton.sdk.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** OpenBaton SDK Property Reader. Provides URL information from the properties file */
public class PropertyReader {

  private Properties sdkProperties;

  /**
   * Creates a property reader that deserializes the property file from a jar
   *
   * @param sdkPropertiesPath path to the properties file
   */
  public PropertyReader(final String sdkPropertiesPath) {
    sdkProperties = readProperties(sdkPropertiesPath);
  }

  /**
   * @param propertiesPath path to the properties file
   * @return Properties
   */
  private Properties readProperties(final String propertiesPath) {
    Properties properties = null;
    InputStream inputStream = null;
    try {
      // load the jar's properties files
      inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(propertiesPath);
      // if there is an inputstream, execute the following
      properties = new Properties();
      properties.load(inputStream);
    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return properties;
  }

  /**
   * Get the URL for the given class name.
   *
   * @param className the class name
   * @return the URL path property
   */
  public String getRestUrl(String className) {
    return sdkProperties.getProperty("rest" + className + "Path");
  }

  public String getSimpleProperty(String key, String def) {
    return sdkProperties.getProperty(key, def);
  }
}
