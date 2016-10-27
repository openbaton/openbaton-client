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

package org.openbaton.cli.model;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.openbaton.cli.util.PrintFormat.*;

/**
 * Created by tce on 25.09.15.
 */
public class GeneralVimInstance {

  public static String print(List<Object> object)
      throws InvocationTargetException, IllegalAccessException {
    String result = "";

    String firstLine = "";
    String secondLine = "";
    String line1 = "";
    String line2 = "";
    String[] rowProperty = new String[500];
    String[] rowValue = new String[500];
    String[] row1 = new String[500];
    String[] row2 = new String[500];

    int rowCount = 0;

    rowProperty[rowCount] = "| ID";
    rowValue[rowCount] = "| VERSION";
    row1[rowCount] = "| NAME";
    row2[rowCount] = "| TENANT";
    rowCount++;

    for (Object anObject : object) {
      Method[] methodBase = anObject.getClass().getDeclaredMethods();
      Method[] methodSuper = anObject.getClass().getSuperclass().getDeclaredMethods();
      Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

      for (Method method : methods) {

        if (method.getName().equalsIgnoreCase("getID")) {
          rowProperty[rowCount] = "| " + method.invoke(anObject).toString();
        }

        if (method.getName().equalsIgnoreCase("getVersion")) {
          rowValue[rowCount] = "| " + method.invoke(anObject).toString();
        }

        if (method.getName().equalsIgnoreCase("getName")) {
          row1[rowCount] = "| " + method.invoke(anObject).toString();
        }

        if (method.getName().equalsIgnoreCase("getTenant")) {
          row2[rowCount] = "| " + method.invoke(anObject).toString();
        }
      }
      rowCount++;
    }

    addRow("\n");

    firstLine = buildLine(rowProperty);
    secondLine = buildLine(rowValue);
    line1 = buildLine(row1);
    line2 = buildLine(row2);

    addRow(firstLine, secondLine, line1, line2, "+");

    for (int c = 0; c < rowCount; c++) {
      addRow(rowProperty[c], rowValue[c], row1[c], row2[c], "|");
      if (c == 0) {
        addRow(firstLine, secondLine, line1, line2, "+");
      }
    }

    addRow(firstLine, secondLine, line1, line2, "+");

    result = printer();

    return result;
  }
}
