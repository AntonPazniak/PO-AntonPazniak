package org.example.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SplotWindow extends JFrame {

    private final List<JPanel> jPanels = new ArrayList<>();
    private JTextField[][] jTextFields;

    public SplotWindow(String title) {
        super(title);

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        JButton setButton = new JButton("Set");
        JPanel panel = new JPanel();
        panel.add(spinner);
        panel.add(setButton);
        add(panel, BorderLayout.NORTH); // Add panel to the top of the frame


        JPanel mainPanel = new JPanel(new GridLayout(0, 1)); // Panel to contain dynamically added panels

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JPanel p : jPanels) {
                    remove(p);
                }
                jPanels.clear();

                int value = (int) spinner.getValue();

                jTextFields = new JTextField[value][value];

                mainPanel.removeAll(); // Clear previous components
                mainPanel.setLayout(new GridLayout(value, value)); // Set layout for grid of components
                for (int i = 0; i < value; i++) {
                    JPanel panel1 = new JPanel();
                    for (int j = 0; j < value; j++) {
                        JTextField textField = new JTextField(2);
                        textField.setText(String.valueOf(1));
                        panel1.add(textField);
                        jTextFields[i][j] = textField;
                    }
                    jPanels.add(panel1);
                    mainPanel.add(panel1);
                }
                revalidate(); // Refresh the layout
            }
        });
        add(mainPanel, BorderLayout.CENTER);

        JPanel panel1 = new JPanel();
        Button button = new Button("Filter");
        panel1.add(button, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel to contain the button
        Button filterButton = new Button("Filter"); // Button to be added
        bottomPanel.add(filterButton); // Add button to the bottom panel
        add(bottomPanel, BorderLayout.SOUTH); // Add bottom panel to the bottom of the frame


        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float[][] matrix = new float[jTextFields.length][jTextFields.length];
                if (!jPanels.isEmpty()) {
                    for (int i = 0; i < jTextFields.length; i++) {
                        for (int j = 0; j < jTextFields[i].length; j++) {
                            String text = jTextFields[i][j].getText().trim();
                            if (!text.isEmpty()) {
                                matrix[i][j] = Float.parseFloat(text);
                            }
                        }
                    }
                }
                for (float[] i : matrix) {
                    for (float j : i) {
                        System.out.print(j + " ");
                    }
                    System.out.println();
                }
            }
        });


        setSize(300, 300); // Increased height to accommodate more components
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplotWindow("SplotWindow Demo"));
    }

}
