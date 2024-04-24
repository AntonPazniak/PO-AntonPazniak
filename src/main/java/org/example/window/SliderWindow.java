package org.example.window;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class SliderWindow extends JFrame {

    private final JSlider slider;
    private final JButton saveButton = new JButton("Save");
    private final JButton applyButton = new JButton("Apply");

    protected SliderWindow(String title, int max, int min, int start) {
        super(title);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(300, 150);
        slider = new JSlider(JSlider.HORIZONTAL, min, max, start);
        slider.setMajorTickSpacing(max / 2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        JPanel panel = new JPanel();
        panel.add(slider);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(applyButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }

}
