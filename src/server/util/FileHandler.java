// Kane Templeton
// FileHandler.java

//this is a generic file i copy and paste throughout several of my projects
//does basic stuff with reading/writing text files

package server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class FileHandler {
    
    public static void appendToBeginning(String path, String txt) {
        File F = new File(path);
        if (!F.exists()) {
            writeFile(path,txt);
        }
        else {
            String x = txt+loadFileAsString(path);
            writeFile(path,x);
        }
    }
    
    public static void appendToFile(String path, String txt) {
        File F = new File(path);
        if (!F.exists())
            writeFile(path,txt);
        else {
            String x = loadFileAsString(path);
            x+=txt;
            writeFile(path,x);
        }
    }
    
    public static void writeFile(String path, String txt) {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(txt);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String loadFileAsString(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        return stringBuilder.toString();
    }

}
