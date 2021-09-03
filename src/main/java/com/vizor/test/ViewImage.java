package com.vizor.test;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

public class ViewImage extends JFrame implements Serializable {

    private JFrame frame;
    private JTextField txtDescription;

    /**
     * Launch the application.
     */

    /**
     * Create the application.
     */

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }

    public ViewImage(ArrayList<Photo> album, int index, String file, int goBackToMain, JFrame ParentFrame) throws IIOException, IndexOutOfBoundsException {

        String title = album.get(index).getTitle();
        String annotation = album.get(index).getAnnotation();
        String image = album.get(index).getImage();

        frame = new JFrame();
        frame.setTitle("View Image");
        frame.setBounds(100, 100, 400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        // JOptionPane.showMessageDialog(null, "You clicked "+index+" !");

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
        lblTitle.setBounds(100, 10, 200, 25);
        frame.getContentPane().add(lblTitle);

        txtDescription = new JTextField();
        txtDescription.setText(annotation);
        txtDescription.setBounds(10, 260, 360, 90);
        frame.getContentPane().add(txtDescription);
        txtDescription.setColumns(100);

        /* Unsuccessful attempts at conversion */
        // Icon Dp = (Icon) new ImageIcon(getClass().getResource(image));
        // Icon Dp = (Icon) new
        // ImageIcon((this.getClass().getResource(image))).getImage();
        // Icon Dp = (Icon) new ImageIcon(image).getImage();
        // Icon Dp = new ImageIcon(image);

        int file_compromised = 0;

        File imageFile = new File(image);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imageFile);
        } catch (IOException e1) {
            // e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "Image went missing !");

            //remove error image
            Photo removed = album.remove(index);
            ReadData r = new ReadData(album, file);
            frame.dispose();
            if (goBackToMain == 1) {
                Gallery g = new Gallery();
            } else {
                ParentFrame.dispose();
                DisplayAll dA2 = new DisplayAll(album, file, 0, null);
            }

            file_compromised = 1;
        }

        if (file_compromised == 0) {
            BufferedImage resized = null;
            try {
                resized = getScaledImage(bufferedImage, 200, 200);
            } catch (IOException e1) {
                // e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Image went missing !");
                Photo remove = album.remove(index);
                ReadData r = new ReadData(album, file);
                frame.dispose();
                if (goBackToMain == 1) {
                    Gallery g = new Gallery();
                } else {
                    ParentFrame.dispose();
                    DisplayAll dA2 = new DisplayAll(album, file, 1, null);
                }
                file_compromised = 1;

            }
            if (file_compromised == 0) {
                Icon resizedbtn = new ImageIcon(resized);
                JButton btnImage = new JButton(resizedbtn);// not ((Icon) Dp) as it leads to classException
                btnImage.setBounds(85, 50, 200, 200);
                btnImage.setBorderPainted(false);
                btnImage.setFocusPainted(false);
                frame.getContentPane().add(btnImage);
            }
        }
    }
}