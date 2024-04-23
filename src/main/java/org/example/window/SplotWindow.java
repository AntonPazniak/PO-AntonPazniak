package org.example.window;

import lombok.Getter;
import org.example.filters.Splot;
import org.example.models.PortableAnymap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SplotWindow extends JFrame {

    private final List<JPanel> jPanels = new ArrayList<>();
    private JTextField[][] jTextFields;
    private final SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 2);
    private final JSpinner spinner = new JSpinner(spinnerModel);
    private final JButton setButton = new JButton("Set");
    private final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private final Button filterButton = new Button("Filter");
    private final JPanel mainPanel = new JPanel(new GridLayout(0, 1));

    private SplotWindow() {
        super("Splot");

        JPanel panel = new JPanel();
        panel.add(spinner);
        panel.add(setButton);
        add(panel, BorderLayout.NORTH);

        setButton.addActionListener(e -> {
            for (JPanel p : jPanels) {
                remove(p);
            }
            jPanels.clear();
            int value = (int) spinner.getValue();
            jTextFields = new JTextField[value][value];
            mainPanel.removeAll();
            mainPanel.setLayout(new GridLayout(value, value));
            for (int i = 0; i < value; i++) {
                JPanel panel1 = new JPanel();
                for (int j = 0; j < value; j++) {
                    JTextField textField = new JTextField(4);
                    textField.setText(String.valueOf(0));
                    panel1.add(textField);
                    jTextFields[i][j] = textField;
                }
                jPanels.add(panel1);
                mainPanel.add(panel1);
            }
            revalidate(); // Refresh the layout
        });
        add(mainPanel, BorderLayout.CENTER);

        JPanel panel1 = new JPanel();
        Button button = new Button("Filter");
        panel1.add(button, BorderLayout.CENTER);


        bottomPanel.add(filterButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(300, 300); // Increased height to accommodate more components
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true);
    }

    public static void createWindow(PortableAnymap image) {
        SplotWindow splotWindow = new SplotWindow();
        splotWindow.getFilterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField[][] jTextFields = splotWindow.getJTextFields();
                float[][] matrix = new float[jTextFields.length][jTextFields.length];
                if (!splotWindow.getJPanels().isEmpty()) {
                    for (int i = 0; i < jTextFields.length; i++) {
                        for (int j = 0; j < jTextFields[i].length; j++) {
                            String text = jTextFields[i][j].getText().trim();
                            if (!text.isEmpty()) {
                                matrix[i][j] = Float.parseFloat(text);
                            }
                        }
                    }
                }
                image.setMatrix(Splot.test(matrix, image.getMatrix()));
                image.updateImage();
                MainWindow.getMainWindow().resizeImage();
            }
        });
    }

}
