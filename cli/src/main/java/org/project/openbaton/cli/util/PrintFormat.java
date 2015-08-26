package org.project.openbaton.cli.util;


import org.apache.commons.lang3.StringUtils;
import org.project.openbaton.catalogue.nfvo.VimInstance;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by tce on 25.08.15.
 */
public class PrintFormat {

    private static List<String[]> rows = new LinkedList<String[]>();

    public static String printFindAll( List<Object> object) {

        rows.clear();
        String result = "";

        addRow("\n");
        addRow("ID","TENANT","NAME");
        addRow("-------------------------------------","--------------","--------------");
        for (int i = 0; i < object.size(); i++) {
            VimInstance vims = (VimInstance) object.get(i);
            addRow(vims.getId(),vims.getTenant(),vims.getName());
            addRow("-------------------------------------","--------------","--------------");
        }

        result = printer();

        return result;


    }


    public static void addRow(String... cols)
    {
        rows.add(cols);
    }

    private static int[] colWidths()
    {
        int cols = -1;

        for(String[] row : rows)
            cols = Math.max(cols, row.length);

        int[] widths = new int[cols];

        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] =
                        Math.max(
                                widths[colNum],
                                StringUtils.length(row[colNum]));
            }
        }

        return widths;
    }


    public static String printer()
    {
        StringBuilder buf = new StringBuilder();

        int[] colWidths = colWidths();

        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
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




}



