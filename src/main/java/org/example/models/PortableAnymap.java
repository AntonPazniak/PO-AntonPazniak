package org.example.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortableAnymap {

    private Image image;
    private int [][][] matrix;
    private int height;
    private int Width;
    private int color;
    private String head;
    private File file;
    private String content;
    private JLabel imageLabel;
}
