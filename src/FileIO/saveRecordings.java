package FileIO;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class saveRecordings {

    public static void save(String[] list){

        BufferedWriter out = null;
        String filename = "rec/rec.txt";

        try {
            out = new BufferedWriter(new FileWriter(filename));
        }
        catch (FileNotFoundException e){
            System.out.println("File " + filename + " not found.");
            System.exit(0);
        }

        catch (IOException e){
            System.out.println("File " + filename + " not found.");
            System.exit(0);
        }

        try {
            for(String s: list){
                if (!s.equals("DELETEME__")) {
                    out.write(s + "\n");
                }
            }
        }
        catch(IOException e){
            System.out.println("File cannot be written too.");
            System.exit(0);
        }

        try {
            out.close();
        }
        catch(IOException e){
            System.out.println("Trouble closing file.");
            System.exit(0);
        }
    }
}
