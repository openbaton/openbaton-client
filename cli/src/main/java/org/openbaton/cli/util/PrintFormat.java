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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openbaton.cli.model.GeneralName;
import org.openbaton.cli.model.GeneralTarget;
import org.openbaton.cli.model.GeneralVimInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tce on 25.08.15.
 *
 * <p>Used to print out the results of a cli command to the console.
 */
public class PrintFormat {

  private static List<String[]> rows = new LinkedList<String[]>();
  public static Logger log = LoggerFactory.getLogger(PrintFormat.class);

  public static String printResult(String command, Object obj)
      throws InvocationTargetException, IllegalAccessException {

    List<Object> object = new ArrayList<Object>();
    rows.clear();
    String result = "";

    if (obj == null) {
      //TODO
      if (!command.contains("delete")
          && !command.contains("changePassword")
          && !command.contains("startVNFCInstance")
          && !command.contains("stopVNFCInstance")
          && !command.contains("createVNFCInstance")) {
        result = "Error: invalid command line";
      }
      return result;

    } else if (isCollection(obj)) {
      object = (List<Object>) obj;
    } else if (isString(obj)) {
      return (String) obj;
    } else {
      object.add((Object) obj);
    }

    if (object.size() == 0) {
      result = "Empty List";

    } else {

      result = printTables(command, object);
    }

    return result;
  }

  public static void addRow(String... cols) {
    rows.add(cols);
  }

  private static int[] colWidths() {
    int cols = -1;

    for (String[] row : rows) cols = Math.max(cols, row.length);

    int[] widths = new int[cols];

    for (String[] row : rows) {
      for (int colNum = 0; colNum < row.length; colNum++) {
        widths[colNum] = Math.max(widths[colNum], StringUtils.length(row[colNum]));
      }
    }

    return widths;
  }

  public static String printer() {
    StringBuilder buf = new StringBuilder();

    int[] colWidths = colWidths();

    for (String[] row : rows) {
      for (int colNum = 0; colNum < row.length; colNum++) {
        buf.append(StringUtils.rightPad(StringUtils.defaultString(row[colNum]), colWidths[colNum]));
        buf.append(' ');
      }

      buf.append('\n');
    }

    return buf.toString();
  }

  private static boolean isCollection(Object ob) {
    return ob instanceof List;
  }

  private static boolean isString(Object ob) {
    return ob instanceof String;
  }

  private static String printTables(String command, List<Object> object)
      throws InvocationTargetException, IllegalAccessException {

    String result = "";

    if (command.contains("Event")) {
      result = showEvent(object);
    } else if (command.contains("create")
        || command.contains("update")
        || command.contains("ById")
        || command.endsWith("Dependency")
        || command.endsWith("Descriptor")
        || command.endsWith("getVirtualNetworkFunctionRecord")
        || command.contains("ByName")) {
      result = showObject(object);

    } else {
      result = generalPrint(command, object);
    }

    return result;
  }

  public static String buildLine(String[] dimension) {
    String result = "+";
    int maxLength = 0;
    for (String s : dimension) {
      if (s != null) {
        if (s.length() > maxLength) {
          maxLength = s.length();
        }
      }
    }

    for (int i = 0; i < maxLength + 2; i++) {
      result = result + "-";
    }

    return result;
  }

  private static String showObject(List<Object> object)
      throws IllegalAccessException, InvocationTargetException {
    String result = "";
    String firstLine = "";
    String secondLine = "";
    String[] rowProperty = new String[5000];
    String[] rowValue = new String[5000];
    int rowCount = 0;

    String fieldName = "";
    String fieldCheck = "";

    Field[] fieldBase = object.get(0).getClass().getDeclaredFields();
    Field[] fieldSuper = object.get(0).getClass().getSuperclass().getDeclaredFields();
    Field[] field = ArrayUtils.addAll(fieldBase, fieldSuper);

    rowProperty[rowCount] = "| PROPERTY";
    rowValue[rowCount] = "| VALUE";
    rowCount++;

    for (int i = 0; i < field.length; i++) {
      Method[] methodBase = object.get(0).getClass().getDeclaredMethods();
      Method[] methodSuper = object.get(0).getClass().getSuperclass().getDeclaredMethods();
      Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

      for (int z = 0; z < methods.length; z++) {

        if (methods[z].getName().equalsIgnoreCase("get" + field[i].getName())) {
          Object lvlDown = methods[z].invoke(object.get(0));
          if (lvlDown != null)
            try {

              if (lvlDown instanceof Set
                  || lvlDown instanceof List
                  || lvlDown instanceof Iterable) {

                Set<Object> objectHash = new HashSet<Object>();

                if (methods[z].getName().contains("security")
                    || methods[z].getName().contains("Source")
                    || methods[z].getName().contains("Target")) {

                  objectHash.add(lvlDown);

                } else {
                  if (lvlDown instanceof ArrayList) objectHash = new HashSet<>((ArrayList) lvlDown);
                  else objectHash = (Set<Object>) lvlDown;
                }

                for (Object obj : objectHash) {
                  Field[] fieldBase2 = obj.getClass().getDeclaredFields();
                  Field[] fieldSuper2 = obj.getClass().getSuperclass().getDeclaredFields();
                  Field[] field2 = ArrayUtils.addAll(fieldBase2, fieldSuper2);

                  String name = "";
                  String id = "";

                  for (Field aField2 : field2) {
                    Method[] methodBase2 = obj.getClass().getDeclaredMethods();
                    Method[] methodSuper2 = obj.getClass().getSuperclass().getDeclaredMethods();
                    Method[] methods2 = ArrayUtils.addAll(methodBase2, methodSuper2);

                    for (Method aMethods2 : methods2) {

                      if (aMethods2.getName().equalsIgnoreCase("get" + aField2.getName())) {
                        Object lvlDown2 = aMethods2.invoke(obj);
                        if (lvlDown2 != null)
                          try {

                            if (!(lvlDown2 instanceof Set)
                                && !(lvlDown2 instanceof List)
                                && !(lvlDown2 instanceof Iterable)) {

                              if (aMethods2.getName().equalsIgnoreCase("getId")) {
                                id = aMethods2.invoke(obj).toString();

                                fieldName = field[i].getName();

                                if (!fieldCheck.equalsIgnoreCase(fieldName)) {
                                  rowProperty[rowCount] = "| " + field[i].getName().toUpperCase();
                                  rowValue[rowCount] = "|";
                                  rowCount++;

                                  rowProperty[rowCount] = "|";
                                  rowValue[rowCount] = "|";
                                  rowCount++;

                                  fieldCheck = fieldName;
                                }
                              }

                              if (aMethods2.getName().equalsIgnoreCase("getName")) {
                                name = aMethods2.invoke(obj).toString();
                              }
                            }

                          } catch (InvocationTargetException e) {
                            e.printStackTrace();
                          }
                      }
                    }
                  }

                  if (id.length() > 0 || name.length() > 0) {
                    rowCount--;
                    rowProperty[rowCount] = "|";
                    if (name.length() > 0) {
                      rowValue[rowCount] = "| id: " + id + " - name:  " + name;
                    } else if (id.length() > 0) {
                      rowValue[rowCount] = "| id: " + id;
                    }
                    rowCount++;
                    rowProperty[rowCount] = "|";
                    rowValue[rowCount] = "|";
                    rowCount++;
                  }
                }

              } else {
                if (lvlDown instanceof String
                    || lvlDown instanceof Integer
                    || lvlDown instanceof Enum) {
                  rowProperty[rowCount] = "| " + field[i].getName();
                  rowValue[rowCount] = "| " + lvlDown.toString();
                  rowCount++;
                  rowProperty[rowCount] = "|";
                  rowValue[rowCount] = "|";
                  rowCount++;
                }
              }

            } catch (InvocationTargetException e) {

              e.printStackTrace();
            }
        }
      }
    }

    addRow("\n");
    firstLine = buildLine(rowProperty);
    secondLine = buildLine(rowValue);
    addRow(firstLine, secondLine, "+");

    for (int c = 0; c < rowCount; c++) {
      addRow(rowProperty[c], rowValue[c], "|");
      if (c == 0) {
        addRow(firstLine, secondLine, "+");
      }
    }

    //end
    addRow(firstLine, secondLine, "+");

    result = printer();

    return result;
  }

  private static String showEvent(List<Object> object)
      throws IllegalAccessException, InvocationTargetException {

    String result = "";
    String firstline = "";
    String[] rowValue = new String[500];
    int rowCount = 0;

    rowValue[rowCount] = "| EVENT";
    rowCount++;

    for (int i = 0; i < object.size(); i++) {
      Method[] methodBase = object.get(i).getClass().getDeclaredMethods();
      Method[] methodSuper = object.get(i).getClass().getSuperclass().getDeclaredMethods();
      Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

      for (int z = 0; z < methods.length; z++) {

        if (methods[z].getName().equalsIgnoreCase("getEvent")) {
          rowValue[rowCount] = "| " + methods[z].invoke(object.get(i)).toString();
        }
      }
      rowCount++;
    }

    addRow("\n");
    firstline = buildLine(rowValue);
    addRow(firstline, "+");

    for (int c = 0; c < rowCount; c++) {
      addRow(rowValue[c], "|");
      if (c == 0) {
        addRow(firstline, "+");
      }
    }
    //end
    addRow(firstline, "+");

    result = printer();

    return result;
  }

  private static String generalPrint(String comand, List<Object> object)
      throws IllegalAccessException, InvocationTargetException {
    String result = "";
    String firstLine = "";
    String secondLine = "";
    String[] rowProperty = new String[500];
    String[] rowValue = new String[500];
    int rowCount = 0;

    if (comand.contains("VimInstance")) {
      result = GeneralVimInstance.print(object);

    } else if (comand.contains("Image")
        || comand.contains("Configuration")
        || comand.contains("NetworkServiceDescriptor-findAll")
        || comand.contains("NetworkServiceRecord-findAll")
        || comand.contains("getVirtualNetworkFunctionRecords")
        || comand.contains("Key-findAll")) {

      result = GeneralName.print(object);

    } else if (comand.contains("Record-getVNFDependencies")) {

      result = GeneralTarget.print(object);

    } else {

      rowProperty[rowCount] = "| ID";
      rowValue[rowCount] = "| VERSION";

      rowCount++;

      for (int i = 0; i < object.size(); i++) {
        Method[] methodBase = object.get(i).getClass().getDeclaredMethods();
        Method[] methodSuper = object.get(i).getClass().getSuperclass().getDeclaredMethods();
        Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

        for (int z = 0; z < methods.length; z++) {

          if (methods[z].getName().equalsIgnoreCase("getID")) {
            rowProperty[rowCount] = "| " + methods[z].invoke(object.get(i)).toString();
          }

          if (methods[z].getName().equalsIgnoreCase("getVersion")) {
            rowValue[rowCount] = "| " + methods[z].invoke(object.get(i)).toString();
          }
        }
        rowCount++;
      }

      addRow("\n");
      firstLine = buildLine(rowProperty);
      secondLine = buildLine(rowValue);
      addRow(firstLine, secondLine, "+");

      for (int c = 0; c < rowCount; c++) {
        addRow(rowProperty[c], rowValue[c], "|");
        if (c == 0) {
          addRow(firstLine, secondLine, "+");
        }
      }

      //end
      addRow(firstLine, secondLine, "+");

      result = printer();
    }

    return result;
  }
}
