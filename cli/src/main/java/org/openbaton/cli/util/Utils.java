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

package org.openbaton.cli.util;

import java.lang.reflect.Method;
import org.apache.commons.lang3.ArrayUtils;

/** Created by tbr on 26.05.16. */
public class Utils {

  /**
   * Returns true if the two passed methods are equal. Methods are in this case regarded as equal if
   * they take the same parameter types, return the same return type and share the same name.
   *
   * @param m1
   * @param m2
   * @return true if method m1 and method m2 are considered equal for our case
   */
  public static boolean methodsAreEqual(Method m1, Method m2) {
    if (!m1.getName().equals(m2.getName())) return false;
    if (!m1.getReturnType().equals(m2.getReturnType())) return false;
    if (!ArrayUtils.isEquals(m1.getParameterTypes(), m2.getParameterTypes())) return false;
    return true;
  }
}
