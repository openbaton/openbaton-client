package org.project.openbaton.cli.model;

import org.project.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.project.openbaton.cli.util.PrintFormat;

import java.util.List;

/**
 * Created by tce on 27.08.15.
 */
public class PrintNetworkServiceRecord extends PrintFormat {


    public static boolean isNetworkServiceRecord(List<Object> ob) {
        return ob.get(0) instanceof NetworkServiceRecord;
    }

    public static String printNetworkServiceRecord(List<Object> object) {
        String result = "";

        addRow("\n");
        addRow("ID", "NAME", "VENDOR", "VERSION");
        addRow("-------------------------------------", "--------------", "--------------", "--------------");
        for (int i = 0; i < object.size(); i++) {
            NetworkServiceRecord networkservicerecord = (NetworkServiceRecord) object.get(i);
            addRow(networkservicerecord.getId(), networkservicerecord.getName(), networkservicerecord.getVendor(), networkservicerecord.getVersion());
            addRow("-------------------------------------", "--------------", "--------------", "--------------");
        }

        result = printer();

        return result;
    }
}
