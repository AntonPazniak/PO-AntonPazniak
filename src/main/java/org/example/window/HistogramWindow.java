package org.example.window;

import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;

public final class HistogramWindow extends JFrame {

    private HistogramWindow(double[][] histogram, boolean isRGB) {
        super("Histogram");

        HistogramDataset dataset = new HistogramDataset();

        if (isRGB) {
            dataset.addSeries("Red", histogram[0], 256);
            dataset.addSeries("Green", histogram[1], 256);
            dataset.addSeries("Blue", histogram[2], 256);
        } else {
            dataset.addSeries("Gray", histogram[0], 256);
        }

        JFreeChart chart = ChartFactory.createHistogram(
                "Histogram of Image",
                "Pixel Value",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

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
