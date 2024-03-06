package org.example.window;


import org.example.models.PortableAnymap;
import org.example.pars.Convert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;


public class Window {

    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JLabel imageLabel;

    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;

    private File openFile;
    private ImageIcon originalIcon;
    private PortableAnymap portableAnymap;
    private final int width = 720;
    private final int height = 500;


    public Window() {
        frame = new JFrame("My GIMP");
        menuBar = new JMenuBar();

        imageLabel = new JLabel();
        frame.getContentPane().add(imageLabel, BorderLayout.CENTER);

        fileMenu = new JMenu("File");

        openItem = new JMenuItem("open");
        saveItem = new JMenuItem("seave");
        exitItem = new JMenuItem("exit");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

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


    private void resizeImage() {
        if (originalIcon != null) {
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
