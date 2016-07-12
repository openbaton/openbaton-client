package org.openbaton.cli.model;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/*
 * Copyright (c) 2015 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import static org.openbaton.cli.util.PrintFormat.*;

/**
 * Created by tce on 25.09.15.
 */
public class GeneralTarget {

  public static String print(List<Object> object)
      throws InvocationTargetException, IllegalAccessException {
    String result = "";

    String firstline = "";
    String secondline = "";
    String line1 = "";
    String[] rowproperty = new String[500];
    String[] rowvalue = new String[500];
    String[] row1 = new String[500];

    int rowcount = 0;

    rowproperty[rowcount] = "| ID";
    rowvalue[rowcount] = "| VERSION";
    row1[rowcount] = "| TARGET";
    rowcount++;

    for (Object anObject : object) {
      Method[] methodBase = anObject.getClass().getDeclaredMethods();
      Method[] methodSuper = anObject.getClass().getSuperclass().getDeclaredMethods();
      Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

      for (int z = 0; z < methods.length; z++) {

        if (methods[z].getName().equalsIgnoreCase("getID")) {
          rowproperty[rowcount] = "| " + methods[z].invoke(anObject).toString();
        }

        if (methods[z].getName().equalsIgnoreCase("getVersion")) {
          rowvalue[rowcount] = "| " + methods[z].invoke(anObject).toString();
        }

        if (methods[z].getName().equalsIgnoreCase("getTarget")) {
          row1[rowcount] = "| " + methods[z].invoke(anObject).toString();
        }
      }
      rowcount++;
    }

    addRow("\n");

    firstline = buildLine(rowproperty);
    secondline = buildLine(rowvalue);
    line1 = buildLine(row1);

    addRow(firstline, secondline, line1, "+");

    for (int c = 0; c < rowcount; c++) {
      addRow(rowproperty[c], rowvalue[c], row1[c], "|");
      if (c == 0) {
        addRow(firstline, secondline, line1, "+");
      }
    }

    addRow(firstline, secondline, line1, "+");

    result = printer();

    return result;
  }
}
