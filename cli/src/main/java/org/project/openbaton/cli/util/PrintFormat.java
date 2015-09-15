package org.project.openbaton.cli.util;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by tce on 25.08.15.
 */
public abstract class PrintFormat {

    private static List<String[]> rows = new LinkedList<String[]>();

    public static String printResult(String comand, Object obj) throws InvocationTargetException, IllegalAccessException {

        List<Object> object = new ArrayList<Object>();
        rows.clear();
        String result = "";

        if (obj == null) {
            //TODO
            if (!comand.contains("delete")) {
                result = "Error: invalid command line";
            }
            return result;

        } else if (isCollection(obj)) {
            object = (List<Object>) obj;
        } else {
            object.add((Object) obj);
        }


        if (object.size() == 0) {
            result = "Empty List";

        } else {

            result = PrintTables(comand, object);
        }

        return result;


    }


    public static void addRow(String... cols) {
        rows.add(cols);
    }

    private static int[] colWidths() {
        int cols = -1;

        for (String[] row : rows)
            cols = Math.max(cols, row.length);

        int[] widths = new int[cols];

        for (String[] row : rows) {
            for (int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] =
                        Math.max(
                                widths[colNum],
                                StringUtils.length(row[colNum]));
            }
        }

        return widths;
    }


    public static String printer() {
        StringBuilder buf = new StringBuilder();

        int[] colWidths = colWidths();

        for (String[] row : rows) {
            for (int colNum = 0; colNum < row.length; colNum++) {
                buf.append(
                        StringUtils.rightPad(
                                StringUtils.defaultString(
                                        row[colNum]), colWidths[colNum]));
                buf.append(' ');
            }

            buf.append('\n');
        }

        return buf.toString();
    }

    public static boolean isCollection(Object ob) {
        return ob instanceof List;
    }

    public static String PrintTables(String comand, List<Object> object) throws InvocationTargetException, IllegalAccessException {
        String result = "";

        if (comand.contains("create") || comand.contains("update") || comand.contains("ById")) {
            result = showObject(object);
        } else {
            result = generalPrint(object);

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

    public static String showObject(List<Object> object) throws IllegalAccessException, InvocationTargetException {
        String result = "";
        String firstline = "";
        String secondline = "";
        String[] rowproperty = new String[500];
        String[] rowvalue = new String[500];
        int rowcount = 0;

        Field[] fieldBase = object.get(0).getClass().getDeclaredFields();
        Field[] fieldSuper = object.get(0).getClass().getSuperclass().getDeclaredFields();
        Field[] field = ArrayUtils.addAll(fieldBase, fieldSuper);

        rowproperty[rowcount] = "| PROPERTY";
        rowvalue[rowcount] = "| VALUE";
        rowcount++;

        for (int i = 0; i < field.length; i++) {
            Method[] methodBase = object.get(0).getClass().getDeclaredMethods();
            Method[] methodSuper = object.get(0).getClass().getSuperclass().getDeclaredMethods();
            Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

            for (int z = 0; z < methods.length; z++) {

                if (methods[z].getName().equalsIgnoreCase("get" + field[i].getName())) {
                    if (methods[z].invoke(object.get(0)) != null) {
                        try {

                            if (methods[z].invoke(object.get(0)).toString().contains("[")) {
                                rowproperty[rowcount] = "| " + field[i].getName().toUpperCase();
                                rowvalue[rowcount] = "|";
                                rowcount++;
                                rowproperty[rowcount] = "|";
                                rowvalue[rowcount] = "|";
                                rowcount++;

                                LinkedHashSet<Object> objectHash = (LinkedHashSet<Object>) methods[z].invoke(object.get(0));

                                for (Object obj : objectHash) {
                                    Field[] fieldBase2 = obj.getClass().getDeclaredFields();
                                    Field[] fieldSuper2 = obj.getClass().getSuperclass().getDeclaredFields();
                                    Field[] field2 = ArrayUtils.addAll(fieldBase2, fieldSuper2);

                                    for (int r = 0; r < field2.length; r++) {
                                        Method[] methodBase2 = obj.getClass().getDeclaredMethods();
                                        Method[] methodSuper2 = obj.getClass().getSuperclass().getDeclaredMethods();
                                        Method[] methods2 = ArrayUtils.addAll(methodBase2, methodSuper2);

                                        for (int s = 0; s < methods2.length; s++) {

                                            if (methods2[s].getName().equalsIgnoreCase("get" + field2[r].getName()) && methods2[s].invoke(obj) != null && !methods2[s].invoke(obj).toString().contains("[")) {
                                                rowproperty[rowcount] = "| " + field2[r].getName();
                                                rowvalue[rowcount] = "| " + methods2[s].invoke(obj).toString();
                                                rowcount++;
                                                rowproperty[rowcount] = "|";
                                                rowvalue[rowcount] = "|";
                                                rowcount++;
                                            }

                                        }
                                    }
                                }


                            } else {
                                rowproperty[rowcount] = "| " + field[i].getName();
                                rowvalue[rowcount] = "| " + methods[z].invoke(object.get(0)).toString();
                                rowcount++;
                                rowproperty[rowcount] = "|";
                                rowvalue[rowcount] = "|";
                                rowcount++;

                            }


                        } catch (InvocationTargetException e) {
                            e.printStackTrace();

                        }

                    }
                }
            }
        }

        addRow("\n");
        firstline = buildLine(rowproperty);
        secondline = buildLine(rowvalue);
        addRow(firstline, secondline, "+");

        for (int c = 0; c < rowcount; c++) {
            addRow(rowproperty[c], rowvalue[c], "|");
            if (c == 0) {
                addRow(firstline, secondline, "+");
            }
        }

        //end
        addRow(firstline, secondline, "+");


        result = printer();

        return result;
    }


    public static String generalPrint(List<Object> object) throws IllegalAccessException, InvocationTargetException {
        String result = "";
        String firstline = "";
        String secondline = "";
        String[] rowproperty = new String[500];
        String[] rowvalue = new String[500];
        int rowcount = 0;

        Field[] fieldBase = object.get(0).getClass().getDeclaredFields();
        Field[] fieldSuper = object.get(0).getClass().getSuperclass().getDeclaredFields();
        Field[] field = ArrayUtils.addAll(fieldBase, fieldSuper);

        rowproperty[rowcount] = "| ID";
        rowvalue[rowcount] = "| VERSION";
        rowcount++;

        for (int i = 0; i < object.size(); i++) {
            Method[] methodBase = object.get(i).getClass().getDeclaredMethods();
            Method[] methodSuper = object.get(i).getClass().getSuperclass().getDeclaredMethods();
            Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

            for (int z = 0; z < methods.length; z++) {

                if (methods[z].getName().equalsIgnoreCase("getID")) {
                    rowproperty[rowcount] = "| " + methods[z].invoke(object.get(i)).toString();
                }

                if (methods[z].getName().equalsIgnoreCase("getVersion")) {
                    rowvalue[rowcount] = "| " + methods[z].invoke(object.get(i)).toString();
                }
            }
            rowcount++;
        }


        addRow("\n");
        firstline = buildLine(rowproperty);
        secondline = buildLine(rowvalue);
        addRow(firstline, secondline, "+");

        for (int c = 0; c < rowcount; c++) {
            addRow(rowproperty[c], rowvalue[c], "|");
            if (c == 0) {
                addRow(firstline, secondline, "+");
            }
        }

        //end
        addRow(firstline, secondline, "+");

        result = printer();

        return result;
    }


}



