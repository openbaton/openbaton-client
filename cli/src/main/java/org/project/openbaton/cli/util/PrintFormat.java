package org.project.openbaton.cli.util;


import org.apache.commons.lang3.StringUtils;
import org.project.openbaton.cli.model.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by tce on 25.08.15.
 */
public abstract class PrintFormat {

    private static List<String[]> rows = new LinkedList<String[]>();

    public static String printResult(Object obj) {

        List<Object> object = new ArrayList<Object>();
        rows.clear();
        String result = "";

        if (obj == null) {
            //TODO
            return result;
        } else if (isCollection(obj)) {
            object = (List<Object>) obj;
        } else {
            object.add((Object) obj);
        }


        if (object.size() == 0) {
            result = "Empty List";

        } else {

            result = PrintTables(object);
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

    public static String PrintTables(List<Object> object) {
        String result = "";

        if (PrintVimInstance.isVimInstance(object)) {
            result = PrintVimInstance.printVimInstance(object);
        }

        if (PrintNetworkServiceDescriptor.isNetworkServiceDescriptor(object)) {
            result = PrintNetworkServiceDescriptor.printNetworkServiceDescriptor(object);
        }

        if (PrintNetworkServiceRecord.isNetworkServiceRecord(object)) {
            result = PrintNetworkServiceRecord.printNetworkServiceRecord(object);
        }

        if (PrintVirtualLink.isVirtualLink(object)) {
            result = PrintVirtualLink.printVirtualLink(object);
        }

        if (PrintVNFFG.isVNFFG(object)) {
            result = PrintVNFFG.printVNFFG(object);
        }


        return result;

    }


}



