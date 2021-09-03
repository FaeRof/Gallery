package com.vizor.test;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.awt.event.ActionEvent;

public class DisplayAll extends JPanel implements Serializable {

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) width / imageWidth;
        double scaleY = (double) height / imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(image, new BufferedImage(width, height, image.getType()));
    }

    /**
     * Create the frame.
     */
    public DisplayAll(ArrayList<Photo> album, String filename, int OptedFor, JFrame Parentframe) {


        int size = album.size();
        JFrame frame = new JFrame();
        frame.setTitle("All images");
        frame.setSize(750, 300);
        frame.getContentPane().setLayout(new GridLayout(2, 5, 0, 0));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton pic = null;

        int i;
        int errorFaced = 0;
        for (i = 0; i < size; i++) {

            if (OptedFor == 1)// display images
            {
                File imageFile = new File(album.get(i).getImage());

                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(imageFile);
                } catch (IOException e1) {

                    JOptionPane.showMessageDialog(null, "The original image(s) has been removed");
                    errorFaced = 1;

                    Photo remove = album.remove(i);
                    frame.dispose();
                    Gallery g2 = new Gallery();
                    return;
                }
                if (errorFaced == 0) {
                    BufferedImage resized = null;
                    try {
                        resized = getScaledImage(bufferedImage, 150, 150);
                    } catch (IOException e1) {
                        // e1.printStackTrace();
                    }
                    Icon resizedbtn = new ImageIcon(resized);

                    pic = new JButton(resizedbtn);
                    pic.setToolTipText(album.get(i).getTitle());
                }
            } else// show titles
            {
                pic = new JButton(album.get(i).getTitle());
                pic.setToolTipText("Images ~ 1K titles");
            }
            final int id = i;
            pic.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        ViewImage vI = new ViewImage(album, id, filename, 0, frame);
                    } catch (IndexOutOfBoundsException e) {//Rare condition
                        JOptionPane.showMessageDialog(null, "The original image has been removed");
                        frame.dispose();
                    } catch (IIOException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                    }
                }
            });
            frame.getContentPane().add(pic);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();


        }

    }

}
