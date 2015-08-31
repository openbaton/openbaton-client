package org.project.openbaton.cli.model;

import org.project.openbaton.catalogue.mano.descriptor.VirtualLinkDescriptor;
import org.project.openbaton.cli.util.PrintFormat;

import java.util.List;

/**
 * Created by tce on 31.08.15.
 */
public class PrintVirtualLink extends PrintFormat {

    public static boolean isVirtualLink(List<Object> ob) {
        return ob.get(0) instanceof VirtualLinkDescriptor;
    }

    public static String printVirtualLink(List<Object> object) {
        String result = "";

        addRow("\n");
        addRow("ID", "VENDOR", "VERSION");
        addRow("-------------------------------------", "--------------", "--------------");
        for (int i = 0; i < object.size(); i++) {
            VirtualLinkDescriptor virtualinkdescriptor = (VirtualLinkDescriptor) object.get(i);
            addRow(virtualinkdescriptor.getId(), virtualinkdescriptor.getVendor(), virtualinkdescriptor.getDescriptor_version());
            addRow("-------------------------------------", "--------------", "--------------", "--------------");
        }

        result = printer();

        return result;
    }


}
