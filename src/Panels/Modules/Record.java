package Panels.Modules;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Record {

    private final Object toggleLock = new Object();

    private final AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;

    private AudioFormat format = new AudioFormat(16000, 8, 2, true, true);
    private TargetDataLine line;
    private volatile boolean recording = false;

    private File temp = new File("temp/temp.wav");


    private void saveData(){

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Unsupported file type.");
            return;
        }

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }


        line.start();


        while(recording) {

            try {
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, type, temp);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void toggleRecord(){


        if (recording) {
            recording = false;
            line.stop();
            line.close();
            new TextPromptBox();
        } else {
            recording = true;
            saveData();
        }
    }
}
