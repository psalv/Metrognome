package Panels;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImagePanel extends JPanel {

//    private Image image;
    private BufferedImage image;
    private String imageLocation = "res/gnome.png";


    public ImagePanel(){

        Dimension size = getPreferredSize();
        size.width = 125;
        setPreferredSize(size);
        setBackground(Color.WHITE);

        try{
            image = ImageIO.read(new File("res/gnome.png"));
        }
        catch(IOException e){
            System.out.println("Error loading image.");
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 16, 10, null);
    }
}
