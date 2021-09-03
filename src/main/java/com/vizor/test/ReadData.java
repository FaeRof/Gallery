package com.vizor.test;

import java.io.*;
import java.util.ArrayList;


public class ReadData {
    public ReadData(ArrayList<Photo> album, String file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream Output = new ObjectOutputStream(fileOut);
            Output.writeInt(album.size());
            Output.writeObject(album);
            fileOut.close();
            Output.close();
        } catch (FileNotFoundException e) {
            //System.err.println("File not found");
        } catch (IOException e) {
            //System.err.println("Read or write failed");
        }
    }
}

