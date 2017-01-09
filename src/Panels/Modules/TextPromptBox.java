package Panels.Modules;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TextPromptBox extends JFrame{

    private static String ENTER = "enter";
    private String entry;
    private JTextField input;

    private Font fontSml = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 23);
    private Font fontTny = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 13);
    private String prompt = "        Name this recording        ";

    private JButton hear;
    private JButton cancel;

    private Clip song;
    private boolean playing = false;

    public TextPromptBox(){
        super();

        super.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setBackground(Color.WHITE);
        input = new JTextField(prompt);
        input.setFont(fontTny);
        input.setForeground(Color.DARK_GRAY);

        hear = new JButton("   listen"){
            public void setBorder(Border border) {
                // None
            }
        };
        hear.setFont(fontSml);
        hear.setForeground(Color.LIGHT_GRAY);
        hear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(!playing) {
                    try {
                        song = AudioSystem.getClip();
                        song.open(AudioSystem.getAudioInputStream(new File("temp/temp.wav")));
                        song.start();
                        playing = true;
                    } catch (Exception f) {
                        f.printStackTrace();
                    }
                }
                else{
                    song.stop();
                    playing = false;
                }
            }
        });

        cancel = new JButton("delete   "){
            public void setBorder(Border border) {
                // None
            }
        };
        cancel.setFont(fontSml);
        cancel.setForeground(Color.LIGHT_GRAY);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        bindKeys(input);

        add(input, BorderLayout.NORTH);
        add(hear, BorderLayout.WEST);
        add(cancel, BorderLayout.EAST);

        pack();
        setSize(200, 75);
        setVisible(true);
        setResizable(false);

    }


    private void close(){
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void bindKeys(JComponent p){
        p.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), ENTER);
        p.getActionMap().put(ENTER, new EnterAction());
    }

    private class EnterAction extends AbstractAction{

        public void actionPerformed(ActionEvent e){
            entry = input.getText();

            if(!entry.equals(prompt)) {

                try {
                    Files.copy(Paths.get("temp/temp.wav"), Paths.get("rec/" + entry + ".wav"));
                    Files.write(Paths.get("rec/rec.txt"), (entry + "\n").getBytes(), StandardOpenOption.APPEND);

                    close();

                } catch (IOException e1) {
                    System.out.println("Invalid file path.");
                }
            }
        }
    }
}
