package org.openbaton.cli.util;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;

/**
 * Created by tbr on 26.05.16.
 */
public class Utils {

  /**
   * Returns true if the two passed methods are equal. Methods are in this case regarded as equal if
   * they take the same parameter types, return the same return type and share the same name.
   *
   * @param m1
   * @param m2
   * @return
   */
  public static boolean methodsAreEqual(Method m1, Method m2) {
    if (!m1.getName().equals(m2.getName())) return false;
    if (!m1.getReturnType().equals(m2.getReturnType())) return false;
    if (!ArrayUtils.isEquals(m1.getParameterTypes(), m2.getParameterTypes())) return false;
    return true;
  }
}
