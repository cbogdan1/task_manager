package org.example.gui;
import java.io.*;
public class SerializationOperation {
    public static void saveToFile(String fileName, Object object) {
        try{
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static Object loadFromFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fileIn);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.getMessage();
            return null;
        }
    }
}
