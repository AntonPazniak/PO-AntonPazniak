package org.example.histogram;

import org.example.models.PortableAnymap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;

public class Histogram extends JFrame {

    public Histogram(String title, PortableAnymap image) {
        super(title);

        HistogramDataset dataset = new HistogramDataset();

        double[] redData = new double[image.getWidth() * image.getHeight()];
        double[] greenData = new double[image.getWidth() * image.getHeight()];
        double[] blueData = new double[image.getWidth() * image.getHeight()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int index = y * image.getWidth() + x;
                redData[index] = image.getMatrix()[y][x][0];
                greenData[index] = image.getMatrix()[y][x][1];
                blueData[index] = image.getMatrix()[y][x][2];
            }
        }


        dataset.addSeries("Red", redData, 256);
        dataset.addSeries("Green", greenData, 256);
        dataset.addSeries("Blue", blueData, 256);

        JFreeChart chart = ChartFactory.createHistogram(
                "Histogram of Image",
                "Pixel Value",
                "Frequency",
                dataset
        );

        // Изменение внешнего вида графика
        XYSplineRenderer renderer = new XYSplineRenderer();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesShapesVisible(i, false);
        }
        chart.getXYPlot().setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 200));

        setContentPane(chartPanel);
    }


    public static void creatHistogram(PortableAnymap image) {
        SwingUtilities.invokeLater(() -> {
            Histogram histogram = new Histogram("Histogram", image);
            histogram.pack();
            histogram.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            histogram.setVisible(true);
        });
    }
}
