package org.example.window;

import lombok.Getter;
import org.example.binarization.Binarization;
import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.awt.*;

@Getter
public final class BinarizationWindow extends JFrame {

    private final JTextField minTextField = new JTextField("0", 3);
    private final JTextField maxTextField = new JTextField("255", 3);
    private final JButton applyButton = new JButton("Apply");

    private BinarizationWindow(double[][] histogram) throws HeadlessException {
        super("Binarization");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Gray", histogram[0], 256);

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

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS)); // Устанавливаем вертикальное расположение

        panel1.add(minTextField);
        panel1.add(maxTextField);
        panel1.add(applyButton);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 300));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(panel1, BorderLayout.SOUTH); // Добавляем панель с текстовыми полями в верхнюю часть
        contentPane.add(chartPanel, BorderLayout.CENTER); // Добавляем панель с графиком в центр

        setContentPane(contentPane);

        pack(); // Устанавливаем размер окна автоматически
    }

    public static void createBinarization(PortableAnymap image) {
        BinarizationWindow binarizationWindow = new BinarizationWindow(Histogram.getGrayHistogram(image));
        binarizationWindow.setVisible(true);
        binarizationWindow.getApplyButton().addActionListener(e -> {
            Binarization.simpleBinarization(
                    image,
                    Integer.parseInt(binarizationWindow.getMinTextField().getText()),
                    Integer.parseInt(binarizationWindow.getMaxTextField().getText()));
            MainWindow.getMainWindow().resizeImage();
        });
    }


}
