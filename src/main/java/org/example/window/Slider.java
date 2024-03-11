package org.example.window;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Slider {

    private JFrame frame;
    private JSlider slider;
    private JButton saveButton, applyButton;

    public Slider(String title, int max, int min, int start) {
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 150);
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

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.NORTH);

        frame.setVisible(true);
    }


    public static void main(String[] args) {

        Slider slider1 = new Slider("Test", 10, -10, 0);

        slider1.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider1.slider.getValue();
                System.out.println("Значение ползунка: " + value);
            }
        });


    }
}
