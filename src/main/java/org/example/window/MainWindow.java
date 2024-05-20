package org.example.window;


import lombok.Getter;
import org.example.Hough.Hough;
import org.example.binarization.Binarization;
import org.example.canny.CannyEdgeDetector;
import org.example.filters.Splot;
import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.example.pars.Convert;
import org.example.point_operation.Desaturation;
import org.example.point_operation.Negative;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

@Getter
public final class MainWindow extends JFrame {

    @Getter
    private static MainWindow mainWindow;
    private final JMenuBar menuBar = new JMenuBar();
    //file menu
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem openItem = new JMenuItem("open");
    private final JMenuItem saveItem = new JMenuItem("seave");
    private final JMenuItem exitItem = new JMenuItem("exit");
    // point menu
    private final JMenu pointMenu = new JMenu("Point");
    private final JMenuItem desaturationItem = new JMenuItem("Desaturation");
    private final JMenuItem negativeItem = new JMenuItem("Negative");
    private final JMenuItem contrastItem = new JMenuItem("Contrast");
    private final JMenuItem brightnessItem = new JMenuItem("Brightness");
    private final JMenuItem sumItem = new JMenuItem("Sum");
    private final JMenuItem differenceItem = new JMenuItem("Difference");
    private final JMenuItem productItem = new JMenuItem("Product");
    private final JMenuItem saturationItem = new JMenuItem("Saturation");
    // histogram
    private final JMenu histogramMenu = new JMenu("Histogram");
    private final JMenuItem RGBHistogramItem = new JMenuItem("RGB");
    private final JMenuItem grayHistogramItem = new JMenuItem("Gray");
    private final JMenuItem stretchingHistogramItem = new JMenuItem("Stretching");
    private final JMenuItem equalizationItem = new JMenuItem("Equalization");
    // splot
    private final JMenu splotMenu = new JMenu("Splot");
    private final JMenuItem testItem = new JMenuItem("test");
    private final JMenuItem sobelItem = new JMenuItem("Sobel");
    private final JMenuItem previtItem = new JMenuItem("Previt");
    private final JMenuItem robertsItem = new JMenuItem("Roberts");
    private final JMenuItem laplaceItem = new JMenuItem("Laplace");
    private final JMenuItem loGItem = new JMenuItem("LoG");
    private final JMenuItem gaussItem = new JMenuItem("Gauss");
    // binarization
    private final JMenu binarizationMenu = new JMenu("Binarization");
    private final JMenuItem binarizationItem = new JMenuItem("Binarize");
    private final JMenuItem otsusMethod = new JMenuItem("Otsu's method");
    // Canny
    private final JMenu cannyMenu = new JMenu("Canny");
    private final JMenuItem cannyItem = new JMenuItem("Canny edge detector");

    private final JMenu houghMenu = new JMenu("Hough");
    private final JMenuItem houghItem = new JMenuItem("Hough");

    private final JLabel imageLabel = new JLabel();
    private File openFile;
    private ImageIcon originalIcon;
    private PortableAnymap portableAnymap;
    private final int width = 1200;
    private final int height = 900;

    public MainWindow() {
        super("My GIMP");

        super.paintComponents(this.getGraphics());

        setJMenuBar(menuBar);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(imageLabel, BorderLayout.CENTER);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        pointMenu.add(desaturationItem);
        pointMenu.add(negativeItem);
        pointMenu.add(contrastItem);
        pointMenu.add(brightnessItem);
        pointMenu.add(sumItem);
        pointMenu.add(differenceItem);
        pointMenu.add(productItem);
        pointMenu.add(saturationItem);

        histogramMenu.add(RGBHistogramItem);
        histogramMenu.add(grayHistogramItem);
        histogramMenu.add(stretchingHistogramItem);
        histogramMenu.add(equalizationItem);

        splotMenu.add(testItem);
        splotMenu.add(sobelItem);
        splotMenu.add(previtItem);
        splotMenu.add(robertsItem);
        splotMenu.add(laplaceItem);
        splotMenu.add(loGItem);
        splotMenu.add(gaussItem);

        binarizationMenu.add(binarizationItem);
        binarizationMenu.add(otsusMethod);

        cannyMenu.add(cannyItem);

        houghMenu.add(houghItem);

        menuBar.add(fileMenu);
        menuBar.add(pointMenu);
        menuBar.add(histogramMenu);
        menuBar.add(splotMenu);
        menuBar.add(binarizationMenu);
        menuBar.add(cannyMenu);
        menuBar.add(houghMenu);
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
            Desaturation.convert(portableAnymap);
            resizeImage();
        });

        negativeItem.addActionListener(e -> {
            Negative.convert(portableAnymap);
            resizeImage();
        });

        contrastItem.addActionListener(e -> ContrastWindow.CreateWindow(portableAnymap));

        brightnessItem.addActionListener(e -> BrightnessWindow.createWindow(portableAnymap));

        sumItem.addActionListener(e -> {
            SDPWindow.createSDPWindow(0, portableAnymap);
            resizeImage();
        });

        differenceItem.addActionListener(e -> {
            SDPWindow.createSDPWindow(1, portableAnymap);
            resizeImage();
        });

        productItem.addActionListener(e -> {
            SDPWindow.createSDPWindow(2, portableAnymap);
            resizeImage();
        });

        saturationItem.addActionListener(e -> SaturationWindow.createWindow(portableAnymap));

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

        testItem.addActionListener(e -> SplotWindow.createWindow(portableAnymap));

        sobelItem.addActionListener(e -> {
            Splot.sobel(portableAnymap);
            resizeImage();
        });

        previtItem.addActionListener(e -> {
            Splot.previt(portableAnymap);
            resizeImage();
        });

        robertsItem.addActionListener(e -> {
            Splot.roberts(portableAnymap);
            resizeImage();
        });

        gaussItem.addActionListener(e -> GaussWindow.createWindow(portableAnymap));

        laplaceItem.addActionListener(e -> {
            Splot.laplace(portableAnymap);
            resizeImage();
        });

        loGItem.addActionListener(e -> {
            Splot.loG(portableAnymap);
            resizeImage();
        });

        binarizationItem.addActionListener(e -> {
            BinarizationWindow.createBinarization(portableAnymap);
            resizeImage();
        });

        otsusMethod.addActionListener(e -> {
            Binarization.otsuBinarization(portableAnymap);
            resizeImage();
        });

        cannyItem.addActionListener(e -> {
            CannyEdgeDetector.convert(portableAnymap);
            resizeImage();
        });

        houghItem.addActionListener(e -> {
            Hough.convert(portableAnymap);
            resizeImage();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        exitItem.addActionListener(e -> System.exit(0));

        resizeImage();
        openFile();
        setVisible(true);
    }


}
