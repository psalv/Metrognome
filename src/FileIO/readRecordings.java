package FileIO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class readRecordings {

    public static ArrayList<String> read(){

        String filename = "rec/rec.txt";
        BufferedReader in;
        ArrayList<String> list = new ArrayList<>();

        try {
            in = new BufferedReader(new FileReader(filename));

            try {
                String line = in.readLine();

                while (!(line == null)) {
                    if (!(line.equals(""))) {
                        list.add(line.replace("\n", ""));
                        line = in.readLine();
                    }
                }
            }
            catch(IOException e){
                System.out.println("File cannot be read.");
            }

            in.close();
        }

        catch (FileNotFoundException ee){
            System.out.println("File " + filename + " not found.");
            System.exit(0);
        }

        catch(IOException e){
            System.out.println("Trouble closing file.");
            System.exit(0);
        }

        return list;
    }
}
