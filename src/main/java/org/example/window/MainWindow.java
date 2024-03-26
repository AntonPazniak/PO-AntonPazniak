package org.example.window;


import lombok.Getter;
import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.example.pars.Convert;
import org.example.point_operation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

@Getter
public class MainWindow {

    private JFrame frame;
    private final JMenuItem openItem, saveItem, exitItem;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private final JMenu pointMenu;
    private final JMenuItem desaturationItem, negativeItem, contrastItem, brightnessItem, sumItem, differenceItem, productItem, saturationItem;
    private final JMenu histogramMenu;
    private final JMenuItem RGBHistogramItem, grayHistogramItem;
    private final JMenu splotMenu;
    private final JMenuItem testItem;
    private JLabel imageLabel;
    private File openFile;
    private ImageIcon originalIcon;
    private PortableAnymap portableAnymap;
    private final int width = 720;
    private final int height = 500;


    public MainWindow() {
        frame = new JFrame("My GIMP");
        menuBar = new JMenuBar();

        imageLabel = new JLabel();
        frame.getContentPane().add(imageLabel, BorderLayout.CENTER);

        fileMenu = new JMenu("File");

        openItem = new JMenuItem("open");
        saveItem = new JMenuItem("seave");
        exitItem = new JMenuItem("exit");

        pointMenu = new JMenu("Point");

        desaturationItem = new JMenuItem("Desaturation");
        negativeItem = new JMenuItem("Negative");
        contrastItem = new JMenuItem("Contrast");
        brightnessItem = new JMenuItem("Brightness");
        sumItem = new JMenuItem("Sum");
        differenceItem = new JMenuItem("Difference");
        productItem = new JMenuItem("Product");
        saturationItem = new JMenuItem("Saturation");

        pointMenu.add(desaturationItem);
        pointMenu.add(negativeItem);
        pointMenu.add(contrastItem);
        pointMenu.add(brightnessItem);
        pointMenu.add(sumItem);
        pointMenu.add(differenceItem);
        pointMenu.add(productItem);
        pointMenu.add(saturationItem);

        histogramMenu = new JMenu("Histogram");

        RGBHistogramItem = new JMenuItem("RGB");
        grayHistogramItem = new JMenuItem("Gray");

        histogramMenu.add(RGBHistogramItem);
        histogramMenu.add(grayHistogramItem);


        splotMenu = new JMenu("Splot");
        testItem = new JMenuItem("test");

        splotMenu.add(testItem);


        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(pointMenu);
        menuBar.add(histogramMenu);
        menuBar.add(splotMenu);
        frame.setJMenuBar(menuBar);

        frame.setSize(width, height);

    }

    public void start(){

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseSaveFolder();
            }
        });
        desaturationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desaturation.convert(portableAnymap);
                resizeImage();
            }
        });
        negativeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Negative.convert(portableAnymap);
                resizeImage();
            }
        });
        contrastItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Contrast.convert(portableAnymap, MainWindow.this);
                resizeImage();
            }
        });
        brightnessItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Brightness.convert(portableAnymap, MainWindow.this);
                resizeImage();
            }
        });
        sumItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SDP.convert(0, portableAnymap);
                resizeImage();
            }
        });
        differenceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SDP.convert(1, portableAnymap);
                resizeImage();
            }
        });
        productItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SDP.convert(2, portableAnymap);
                resizeImage();
            }
        });
        saturationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Saturation.convert(portableAnymap, MainWindow.this);
                resizeImage();
            }
        });

        RGBHistogramItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Histogram.creatRGBHistogram(portableAnymap);
            }
        });
        grayHistogramItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Histogram.creatGrayHistogram(portableAnymap);
            }
        });
        testItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SplotWindow.test(portableAnymap, MainWindow.this);
            }
        });

        openFile();
        exit();
        frame.setVisible(true);
    }

    private void exit(){

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void openFile() {
        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                openFile = fileChooser.getSelectedFile();
                int dotIndex = openFile.getName().lastIndexOf('.');
                if (dotIndex > 0) {
                    String fileExtension = openFile.getName().substring(dotIndex + 1);
                    if(!fileExtension.equals("png") && !fileExtension.equals("jpg") && !fileExtension.equals("JPG")){
                        portableAnymap = Convert.open(openFile.getAbsolutePath());
                        portableAnymap.setImageLabel(imageLabel);
                        originalIcon = new ImageIcon(portableAnymap.getImage());
                        resizeImage();
                    }else {
                        originalIcon = new ImageIcon(openFile.getAbsolutePath());
                        resizeImage();
                    }
                }
            }
        });
    }


    public void resizeImage() {
        if (portableAnymap != null) {
            originalIcon = new ImageIcon(portableAnymap.getImage());
            // Получаем размеры окна
            int windowWidth = imageLabel.getWidth();
            int windowHeight = imageLabel.getHeight();

            // Получаем размеры изображения
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();

            // Вычисляем новые размеры для подстройки под окно
            int newWidth, newHeight;
            double aspectRatio = (double) originalWidth / originalHeight;

            if (windowWidth / aspectRatio <= windowHeight) {
                newWidth = windowWidth;
                newHeight = (int) (windowWidth / aspectRatio);
            } else {
                newHeight = windowHeight;
                newWidth = (int) (windowHeight * aspectRatio);
            }

            // Масштабируем изображение
            Image scaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Обновляем метку с новым изображением
            imageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }

    private void chooseSaveFolder() {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            portableAnymap.setFile(new File(selectedFolder,openFile.getName()));
            Convert.save(portableAnymap);


        } else {
            System.out.println("Отменено пользователем.");
        }
    }
}
