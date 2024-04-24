package org.example.window;

import org.example.models.PortableAnymap;
import org.example.pars.Convert;
import org.example.point_operation.SDP;

import javax.swing.*;
import java.io.File;

public class SDPWindow {

    public static void createSDPWindow(int mun, PortableAnymap portableAnymap) {

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        PortableAnymap portableAnymap1;

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            int dotIndex = file.getName().lastIndexOf('.');
            if (dotIndex > 0) {
                String fileExtghghhgension = file.getName().substring(dotIndex + 1);
                portableAnymap1 = Convert.open(file.getAbsolutePath());
                switch (mun) {
                    case 0 -> SDP.sum(portableAnymap, portableAnymap1);
                    case 1 -> SDP.dif(portableAnymap, portableAnymap1);
                    case 2 -> SDP.product(portableAnymap, portableAnymap1);
                }
            }
        }
        portableAnymap.updateImage();
    }

}
