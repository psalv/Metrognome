package Panels.Modules;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MetroGnome{

    private final Object loopLock = new Object();
    private final Object setLock = new Object();
    private final Object fileLock = new Object();
    private final Object toneLock = new Object();

    private volatile int bpm;
    private final float SECONDS = 60;
    private volatile boolean on = false;
    private long sleep;                     //milliseconds
    private long time;

    private Clip beat;
    private  boolean first;


    private final String SOUND1 = "res/brass-sd.wav";
    private final String SOUND2 = "res/cross-stick-sd.wav";
    private final String SOUND3 = "res/useful-snare.wav";
    private ExecutorService executor = Executors.newFixedThreadPool(4);
    private Future cancel;
    private String sound = SOUND3;



    public MetroGnome(int bpm){
        setBPM(bpm);
        soundinit();
    }

    // Works in it' s own thread, loads the clip that is currently specified as the tone and prepares the beat.
    private void soundinit(){
        synchronized (fileLock) {

            try {
                beat = AudioSystem.getClip();
                beat.open(AudioSystem.getAudioInputStream(new File(sound)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Changes the tone based on number it is called with
    public void changeSound(int num){

        synchronized (toneLock) {
            try {

                if (num == 1) {
                    sound = SOUND1;
                    soundinit();
                    beat.start();
                    soundinit();
                } else if (num == 2) {
                    sound = SOUND2;
                    soundinit();
                    beat.start();
                    soundinit();
                } else {
                    sound = SOUND3;
                    soundinit();
                    beat.start();
                    soundinit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setBPM(int bpm){

        synchronized (setLock) {
            this.bpm = bpm;
            sleep = (long) ((SECONDS / this.bpm) * 1000);
        }
    }

    public void kill(){
        on = false;

        if(cancel != null) {
            cancel.cancel(true);
        }
    }

    public boolean status(){
        return on;
    }

    private void pulse(){


        cancel = executor.submit(new Runnable() {

            public void run() {
                beat.start();
                soundinit();
            }
        });

        long newTime = System.nanoTime();
        time = newTime;
    }

    public void toggle(){

        on = !on;
        first = true;

        if(on) {
            pulseLoop();
        }

    }

    public void pulseLoop(){

        synchronized (loopLock) {

            time = System.nanoTime();

            while (on) {
                try {
                    Thread.sleep(sleep);
                    if(cancel != null){
                        cancel.cancel(true);
                    }
                    if(!first) {
                        pulse();
                    }
                    first = false;
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
