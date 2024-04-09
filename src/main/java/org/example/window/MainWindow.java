package org.example.window;


import lombok.Getter;
import org.example.filters.Splot;
import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.example.pars.Convert;
import org.example.point_operation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

@Getter
public final class MainWindow extends JFrame {

    @Getter
    private static MainWindow mainWindow;
    private final JMenuItem openItem, saveItem, exitItem;
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenu pointMenu;
    private final JMenuItem desaturationItem, negativeItem, contrastItem, brightnessItem, sumItem, differenceItem, productItem, saturationItem;
    private final JMenu histogramMenu;
    private final JMenuItem RGBHistogramItem, grayHistogramItem, stretchingHistogramItem, equalizationItem;
    private final JMenu splotMenu;
    private final JMenuItem testItem, sobelItem, previtItem, robertsItem, laplaceItem, loGItem, gaussItem;
    private final JLabel imageLabel;
    private File openFile;
    private ImageIcon originalIcon;
    private PortableAnymap portableAnymap;
    private final int width = 720;
    private final int height = 500;

    public MainWindow() {
        super("My GIMP");
        menuBar = new JMenuBar();

        super.paintComponents(this.getGraphics());

        setJMenuBar(menuBar);
        imageLabel = new JLabel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(imageLabel, BorderLayout.CENTER);

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
        stretchingHistogramItem = new JMenuItem("Stretching");
        equalizationItem = new JMenuItem("Equalization");


        histogramMenu.add(RGBHistogramItem);
        histogramMenu.add(grayHistogramItem);
        histogramMenu.add(stretchingHistogramItem);
        histogramMenu.add(equalizationItem);



        splotMenu = new JMenu("Splot");
        testItem = new JMenuItem("test");

        sobelItem = new JMenuItem("Sobel");
        previtItem = new JMenuItem("Previt");
        robertsItem = new JMenuItem("Roberts");
        laplaceItem = new JMenuItem("Laplace");
        loGItem = new JMenuItem("LoG");
        gaussItem = new JMenuItem("Gauss");

        splotMenu.add(testItem);
        splotMenu.add(sobelItem);
        splotMenu.add(previtItem);
        splotMenu.add(robertsItem);
        splotMenu.add(laplaceItem);
        splotMenu.add(loGItem);
        splotMenu.add(gaussItem);


        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        menuBar.add(pointMenu);
        menuBar.add(histogramMenu);
        menuBar.add(splotMenu);
        setJMenuBar(menuBar);

        setSize(width, height);

    }

    @Override
    public MenuBar getMenuBar() {
        return super.getMenuBar();
    }

    public static void create() {
        mainWindow = new MainWindow();
        mainWindow.start();
    }

    private void openFile() {
        openItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/src/main/resources/images/P3"));

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                openFile = fileChooser.getSelectedFile();
                int dotIndex = openFile.getName().lastIndexOf('.');
                if (dotIndex > 0) {
                    String fileExtension = openFile.getName().substring(dotIndex + 1);
                    if (!fileExtension.equals("png") && !fileExtension.equals("jpg") && !fileExtension.equals("JPG")) {
                        portableAnymap = Convert.open(openFile.getAbsolutePath());
                        originalIcon = new ImageIcon(portableAnymap.getImage());
                        resizeImage();
                    } else {
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

    public void start(){

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });

        saveItem.addActionListener(e -> chooseSaveFolder());
        desaturationItem.addActionListener(e -> {
            PointFilter pointFilter = new Desaturation();
            pointFilter.convert(portableAnymap);
            resizeImage();
        });

        negativeItem.addActionListener(e -> {
            PointFilter pointFilter = new Negative();
            pointFilter.convert(portableAnymap);
            resizeImage();
        });

        contrastItem.addActionListener(e -> {
            PointFilter pointFilter = new Contrast();
            pointFilter.convert(portableAnymap);
            resizeImage();
        });

        brightnessItem.addActionListener(e -> {
            PointFilter pointFilter = new Brightness();
            pointFilter.convert(portableAnymap);
            resizeImage();
        });

        sumItem.addActionListener(e -> {
            SDP.convert(0, portableAnymap);
            resizeImage();
        });

        differenceItem.addActionListener(e -> {
            SDP.convert(1, portableAnymap);
            resizeImage();
        });

        productItem.addActionListener(e -> {
            SDP.convert(2, portableAnymap);
            resizeImage();
        });

        saturationItem.addActionListener(e -> {
            PointFilter pointFilter = new Negative();
            pointFilter.convert(portableAnymap);
            resizeImage();
        });

        RGBHistogramItem.addActionListener(e -> HistogramWindow.creatHistogramRGB(portableAnymap));

        grayHistogramItem.addActionListener(e -> HistogramWindow.creatHistogramGray(portableAnymap));

        stretchingHistogramItem.addActionListener(e -> {
            Histogram.stretching(portableAnymap);
            resizeImage();
        });

        equalizationItem.addActionListener(e -> {
            Histogram.equalization(portableAnymap);
            resizeImage();
        });

        testItem.addActionListener(e -> SplotWindow.test(portableAnymap, MainWindow.this));

        sobelItem.addActionListener(e -> {
            Splot.sobel(portableAnymap.getMatrix());
            portableAnymap.updateImage();
            resizeImage();
        });

        previtItem.addActionListener(e -> {
            Splot.previt(portableAnymap.getMatrix());
            portableAnymap.updateImage();
            resizeImage();
        });

        robertsItem.addActionListener(e -> {
            Splot.roberts(portableAnymap.getMatrix());
            portableAnymap.updateImage();
            resizeImage();
        });

        gaussItem.addActionListener(e -> {
            Splot.gauss(portableAnymap);
            resizeImage();
        });

        laplaceItem.addActionListener(e -> {
            Splot.laplace(portableAnymap.getMatrix());
            portableAnymap.updateImage();
            resizeImage();
        });

        loGItem.addActionListener(e -> {
            Splot.loG(portableAnymap.getMatrix());
            portableAnymap.updateImage();
            resizeImage();
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        exitItem.addActionListener(e -> System.exit(0));

        resizeImage();
        openFile();
        setVisible(true);
    }


}
