package org.project.openbaton.cli.model;

import org.project.openbaton.catalogue.mano.descriptor.VNFForwardingGraphDescriptor;
import org.project.openbaton.cli.util.PrintFormat;

import java.util.List;

/**
 * Created by tce on 31.08.15.
 */
public class PrintVNFFG extends PrintFormat {

    public static boolean isVNFFG(List<Object> ob) {
        return ob.get(0) instanceof VNFForwardingGraphDescriptor;
    }

    public static String printVNFFG(List<Object> object) {
        String result = "";

        addRow("\n");
        addRow("ID", "VENDOR", "VERSION");
        addRow("-------------------------------------", "--------------", "--------------");
        for (int i = 0; i < object.size(); i++) {
            VNFForwardingGraphDescriptor vnffg = (VNFForwardingGraphDescriptor) object.get(i);
            addRow(vnffg.getId(), vnffg.getVendor(), vnffg.getVersion());
            addRow("-------------------------------------", "--------------", "--------------", "--------------");
        }

        result = printer();

        return result;
    }


}
