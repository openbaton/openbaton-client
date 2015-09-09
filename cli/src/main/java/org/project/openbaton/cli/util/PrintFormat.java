package org.project.openbaton.cli.util;


import org.apache.commons.lang3.StringUtils;
import org.project.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.project.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.cli.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.apache.commons.lang3.ArrayUtils;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by tce on 25.08.15.
 */
public abstract class PrintFormat {

    private static List<String[]> rows = new LinkedList<String[]>();

    public static String printResult(String comand,Object obj) throws InvocationTargetException, IllegalAccessException {

        List<Object> object = new ArrayList<Object>();
        rows.clear();
        String result = "";

        if (obj == null) {
            //TODO
            result = "Error: invalid command line";
            return result;

        } else if (isCollection(obj)) {
            object = (List<Object>) obj;
        } else {
            object.add((Object) obj);
        }


        if (object.size() == 0) {
            result = "Empty List";

        } else {

            result = PrintTables(comand,object);
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

    public static String PrintTables(String comand,List<Object> object) throws InvocationTargetException, IllegalAccessException {
        String result = "";

        if(comand.contains("create") || comand.contains("update"))
        {
            result = showObject(object);
        }
        else
        {
           result =  generalPrint(object);

        }


        return result;

    }

    public static String showObject(List<Object> object)throws IllegalAccessException, InvocationTargetException
    {
        String result = "";


        addRow("\n");
        addRow("+-------------------------------------",  "+-------------------------------------", "+");
        addRow("| PROPERTY", "| VALUE", "|");
        addRow("+-------------------------------------",  "+-------------------------------------", "+");
        Field[] fieldBase = object.get(0).getClass().getDeclaredFields();
        Field[] fieldSuper = object.get(0).getClass().getSuperclass().getDeclaredFields();
        Field[] field = ArrayUtils.addAll(fieldBase, fieldSuper);

        for (int i = 0; i < field.length; i++)
        {
            Method[] methodBase = object.get(0).getClass().getDeclaredMethods();
            Method[] methodSuper = object.get(0).getClass().getSuperclass().getDeclaredMethods();
            Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

            for (int z = 0; z < methods.length; z++)
            {

                if(methods[z].getName().equalsIgnoreCase("get" + field[i].getName()))
                {
                    if (methods[z].invoke(object.get(0)) != null && methods[z].invoke(object.get(0)).toString().contains("{") == false) {
                        try {

                            addRow("| " + field[i].getName(), "| " + methods[z].invoke(object.get(0)).toString(), "|");
                            addRow("|", "|", "|");
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();

                        }

                    }
                }
            }
        }
        addRow("+-------------------------------------",  "+-------------------------------------", "+");
        result = printer();

        return result;
    }


    public static String generalPrint(List<Object> object)throws IllegalAccessException, InvocationTargetException
    {
        String result = "";
        String id = "";
        String version = "";

        Field[] fieldBase = object.get(0).getClass().getDeclaredFields();
        Field[] fieldSuper = object.get(0).getClass().getSuperclass().getDeclaredFields();
        Field[] field = ArrayUtils.addAll(fieldBase, fieldSuper);

        addRow("\n");
        addRow("+-------------------------------------", "+-------------------", "+");
        addRow("| ID", "| VERSION", "|");
        addRow("+-------------------------------------", "+-------------------", "+");
        for (int i = 0; i < object.size(); i++) {
            Method[] methodBase = object.get(i).getClass().getDeclaredMethods();
            Method[] methodSuper = object.get(i).getClass().getSuperclass().getDeclaredMethods();
            Method[] methods = ArrayUtils.addAll(methodBase, methodSuper);

            for (int z = 0; z < methods.length; z++)
            {

                if (methods[z].getName().equalsIgnoreCase("getID"))
                {
                    id = methods[z].invoke(object.get(i)).toString();
                }

                if (methods[z].getName().equalsIgnoreCase("getVersion"))
                {
                    version = methods[z].invoke(object.get(i)).toString();
                }
            }
            addRow("| " + id, "| " + version, "|");
            addRow("|", "|", "|");
        }
        addRow("+-------------------------------------", "+-------------------", "+");


        result = printer();

        return result;
    }


}



