package org.project.openbaton.cli.model;

import org.project.openbaton.cli.util.PrintFormat;
import org.project.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import java.util.List;

/**
 * Created by tce on 27.08.15.
 */
public class PrintNetworkServiceDescriptor extends PrintFormat {


    public static boolean isNetworkServiceDescriptor(List<Object> ob) {
        return ob.get(0) instanceof NetworkServiceDescriptor;
    }


    public static String printNetworkServiceDescriptor( List<Object> object)
    {
        String result="";

        addRow("\n");
        addRow("ID","NAME","VENDOR","VERSION");
        addRow("-------------------------------------","--------------","--------------","--------------");
        for (int i = 0; i < object.size(); i++) {
            NetworkServiceDescriptor networkservicedescriptor = (NetworkServiceDescriptor) object.get(i);
            addRow(networkservicedescriptor.getId(),networkservicedescriptor.getName(),networkservicedescriptor.getVendor(),networkservicedescriptor.getVersion());
            addRow("-------------------------------------","--------------","--------------","--------------");
        }

        result = printer();

        return result;
    }
}
