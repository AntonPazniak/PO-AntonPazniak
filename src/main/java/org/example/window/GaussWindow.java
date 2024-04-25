package org.example.window;

import lombok.Getter;
import org.example.filters.Splot;
import org.example.models.PortableAnymap;

import javax.swing.*;

@Getter
public final class GaussWindow extends JFrame {

    private final SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 2);
    private final JSpinner spinner = new JSpinner(spinnerModel);
    private final JTextField stepTextField = new JTextField("1", 3);
    private final JButton okButton = new JButton("OK");


    private GaussWindow() {
        super("Gauss options");

        JPanel panel = new JPanel();
        panel.add(spinner);
        panel.add(stepTextField);
        panel.add(okButton);
        add(panel);

        setSize(200, 120);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true);
    }


    public static void createWindow(PortableAnymap image) {
        GaussWindow gaussWindow = new GaussWindow();
        gaussWindow.getOkButton().addActionListener(e -> {
            int size = (int) gaussWindow.getSpinner().getValue();
            double step = Double.parseDouble(gaussWindow.getStepTextField().getText());
            Splot.gauss(image, size, step);
            MainWindow.getMainWindow().resizeImage();
        });
    }
}
