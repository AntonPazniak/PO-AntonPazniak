package org.example.window;

import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;

public class HistogramWindow extends JFrame {

    public HistogramWindow(double[][] histogram, boolean isRGB) {
        super("Histogram");


        HistogramDataset dataset = new HistogramDataset();
        JFreeChart chart;
        if (isRGB) {
            dataset.addSeries("Red", histogram[0], 255);
            dataset.addSeries("Blue", histogram[2], 255);
            dataset.addSeries("Green", histogram[1], 255);

            chart = ChartFactory.createHistogram(
                    "Histogram of Image",
                    "Pixel Value",
                    "Frequency",
                    dataset
            );
        } else {
            dataset.addSeries("Gray", histogram[0], 255);

            chart = ChartFactory.createHistogram(
                    "Histogram of Image",
                    "Pixel Value",
                    "Frequency",
                    dataset
            );
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 200));

        setContentPane(chartPanel);

        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public static void creatHistogramRGB(PortableAnymap image) {
        HistogramWindow histogramWindow = new HistogramWindow(Histogram.getRGBHistogram(image), true);
        histogramWindow.setVisible(true);
    }

    public static void creatHistogramGray(PortableAnymap image) {
        HistogramWindow histogramWindow = new HistogramWindow(Histogram.getGrayHistogram(image), false);
        histogramWindow.setVisible(true);
    }


}
