package org.example.window;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SliderWindow extends JFrame {

    private JSlider slider;
    private JButton saveButton, applyButton;

    public SliderWindow(String title, int max, int min, int start) {
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
        saveButton = new JButton("Save");
        applyButton = new JButton("Apply");

        buttonPanel.add(applyButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }

}
