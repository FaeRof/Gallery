package com.vizor.test;

import java.awt.*;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Gallery implements Serializable{

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;

    private JFrame frame;
    static ArrayList<Photo> album = new ArrayList<>();
    private JTable table;
    String filename = "Images.dat";

    /**
     * Launch the application.
     */


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Gallery window = new Gallery();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Create the application.
     * @return
     */
    public Gallery ()
    {
        int savedSize =0 ;
        ArrayList<Photo> album1 = new ArrayList<>();

        try   {
            FileInputStream infile = new FileInputStream(filename);
            ObjectInputStream outfile = new ObjectInputStream(infile);

            savedSize = (int) outfile.readInt();

            try {
                album1 = (ArrayList<Photo>) outfile.readObject();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }


            infile.close();
            outfile.close();
        }
        catch (FileNotFoundException e)
        {
            // System.err.println("File not found");
            try{
                File f = new File(filename);

                boolean bool = false;
                bool = f.createNewFile();
                while(bool==false)
                {
                    f.delete();
                    bool = f.createNewFile();
                }
            }catch(Exception ex){
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            //	System.err.println("Read failed");
        }

        if(savedSize!=0)
        {
            album = album1;
        }

        frame = new JFrame();
        frame.setTitle("Photo Album");
        frame.setBounds(100, 100, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


//        String[] columns = {"Title","Description"};

        TableModel tableModel = createTableModel();
        table = new JTable(tableModel);

//        table.setBounds(10, 40, 980, 600);
//        frame.getContentPane().add(table);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setBounds(373, 15, 78, 18);
        frame.getContentPane().add(lblSearch);

        JTextField filterField = RowFilterUtil.createRowFilter(table);
        JPanel jp = new JPanel();
        jp.setBounds(420, 11, 168, 25);
        jp.add(filterField);
        frame.add(jp, BorderLayout.NORTH);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(10, 40, 980, 600);
        frame.add(pane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JButton btnTitles = new JButton("Titles!");
        btnTitles.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                DisplayAll dA = new DisplayAll(album,filename,0,frame);//0 for titles , 1 for images
                //because a picture is worth 1K words
            }
        });
        btnTitles.setBounds(10, 9, 80, 30);
        frame.getContentPane().add(btnTitles);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int found = 0;
                String path = " ";

                //Allowing max size to be flexible
				/*
				if(album.size()==10)
				{
					JOptionPane.showMessageDialog(null, "Only 10 photos can be saved at a time !");
				}

				else
				{
				*/	 final JFileChooser  fileDialog = new JFileChooser();
                int returnVal = fileDialog.showOpenDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {

                    String filename=fileDialog.getSelectedFile().getName();
                    java.io.File file = fileDialog.getSelectedFile();

                    path=fileDialog.getSelectedFile().getAbsolutePath();
                    // String extension = file.getFileExtension(path);

                    String validate = filename.substring(filename.lastIndexOf("."),filename.length());
                    //JOptionPane.showMessageDialog(null,"Path = "+path);


                    String[] validTypes = {".png",".jpg",".jpeg",".gif",".bmp"
                            ,".PNG",".JPG",".JPEG",".GIF",".BMP"};//allowed photo types
                    int possible_size = validTypes.length;

                    for(int i = 0; i < possible_size ; i++)
                    {
                        if(validate.equals(validTypes[i]))
                        {
                            found = 1;
                            break;
                        }
                    }
                    if(found==1)
                    {
                        JOptionPane.showMessageDialog(null,"Image selected!");
                        //JOptionPane.showMessageDialog(null,"Path = "+path);
                    }
                    else
                    {	found = 0;
                        JOptionPane.showMessageDialog(null,"Chosen file is not an image!");
                    }
                }
                else
                {
                    found = 0;
                    JOptionPane.showMessageDialog(null,"No image selected !");
                }

                if(found==1)
                {	String title = "qwertyuiopasdfghjklzxcvbnm";

                    title = JOptionPane.showInputDialog("Enter Title");
                    while(title.length()>20||title.equals(""))
                    {
                        title = JOptionPane.showInputDialog("Enter Title upto 20 characters");

                    }
                    //JOptionPane.showMessageDialog(null, "{"+title+"}");

                    String annotation = "qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnm";
                    //had to use long string as RandomUtilString unable to import

                    annotation = JOptionPane.showInputDialog("Enter Annotation ");

                    while(annotation.length()>100||annotation.equals(""))
                    {
                        annotation = JOptionPane.showInputDialog("Enter Annotation upto 100 characters");
                    }

                    Photo newPhoto = new Photo();


                    newPhoto.setImage(path);
                    newPhoto.setTitle(title);
                    newPhoto.setAnnotation(annotation);

                    album.add(newPhoto);
                    ReadData r = new ReadData(album,filename);
                    //frame.getContentPane().revalidate();
                    //frame.getContentPane().repaint();
                    frame.dispose();
                    Gallery g = new Gallery();
                }
                //}
            }
        });
        btnAdd.setBounds(10, 650, 100, 50);
        frame.getContentPane().add(btnAdd);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                int remove = table.getSelectedRow();
                if(remove==-1)
                {
                    JOptionPane.showMessageDialog(null, "Select an image first");
                }
                else
                {
                    Photo removed = album.remove(remove);

                    ReadData r = new ReadData(album,filename);

                    JOptionPane.showMessageDialog(null,"Image " + removed.getTitle() + " has been removed");
                    //frame.getContentPane().revalidate();
                    //frame.getContentPane().repaint();
                    frame.dispose();
                    Gallery g2 = new Gallery();
                }
            }
        });
        btnDelete.setBounds(900, 650, 91, 50);
        frame.getContentPane().add(btnDelete);


        JButton btnGallery = new JButton("Gallery!");
        btnGallery.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                DisplayAll dA = new DisplayAll(album,filename,1,frame);

            }
        });
        btnGallery.setBounds(790, 650, 100, 50);
        frame.getContentPane().add(btnGallery);

        frame.setVisible(true);

    }

    private static TableModel createTableModel() {
        Vector<String> columns = new Vector<>(Arrays.asList("Title","Annotation"));
        Vector<Vector<Object>> rows = new Vector<>();

        int size = album.size();
        Object[][] album_object  = new String[size][2];

        int i;
        for(i = 0; i < size ; i++)
        {
            Vector<Object> v = new Vector<>();
            v.add(album_object[i][0] = album.get(i).getTitle());
            v.add(album_object[i][1] = album.get(i).getAnnotation());
            rows.add(v);
        }

        DefaultTableModel dtm = new DefaultTableModel(rows, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };
        return dtm;
    }
}