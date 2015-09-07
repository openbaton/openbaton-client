package org.project.openbaton.cli.model;

import org.project.openbaton.cli.util.PrintFormat;
import org.project.openbaton.catalogue.nfvo.VimInstance;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.io.*;


/**
 * Created by tce on 27.08.15.
 */
public class PrintVimInstance extends PrintFormat{


    public static boolean isVimInstance(List<Object> ob) {
        return ob.get(0) instanceof VimInstance;
    }


    public static String printVimInstance(String comand, List<Object> object)  {
        String result = "";


            /*VimInstance vims = (VimInstance) object.get(0);
            Field[] field = vims.getClass().getDeclaredFields();
            String value = field[0].getName();
            addRow("\n");
            addRow("+-------------------------------------",  "+-------------------", "+");
            addRow("| ID", "| TENANT", "|");
            addRow("+-------------------------------------",  "+-------------------", "+");
            for (int i = 0; i < field.length; i++) {
                //String variable = "get"+field[i].getName().subSequence(0,0).toString().toUpperCase()+field[i].getName().substring(1);
                addRow("| " + field[i].getName(), "| " ,field[i].get(vims).toString(),  "|");
                addRow("|", "|", "|");
            }
            addRow("+-------------------------------------",  "+-------------------", "+");
            result = printer();*/




            addRow("\n");
            addRow("+-------------------------------------", "+-----------------------", "+-------------------", "+");
            addRow("| ID", "| TENANT", "| NAME", "|");
            addRow("+-------------------------------------", "+-----------------------", "+-------------------", "+");
            for (int i = 0; i < object.size(); i++) {
                VimInstance vims = (VimInstance) object.get(i);
                addRow("| " + vims.getId(), "| " + vims.getTenant(), "| " + vims.getName(), "|");
                addRow("|", "|", "|", "|");
            }
            addRow("+-------------------------------------", "+-----------------------", "+-------------------", "+");


            result = printer();



            return result;
        }

}
