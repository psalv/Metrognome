package Panels;

import Events.DeleteEvent;
import Events.DeleteListener;
import Events.PanelEvent;
import Events.PanelListener;
import FileIO.readRecordings;
import FileIO.saveRecordings;
import Panels.Modules.DeletePromptBoxFrame;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class RecPanel extends JPanel {

    private Font fontTeenyTny;
    private Font fontSml;

    private boolean playing = false;
    private Clip song;

    private JList<String> list;
    private JScrollPane scrollPane;

    private ExecutorService executor = Executors.newFixedThreadPool(3);
    private Future toCancel;

    private String[] recordings;

    public RecPanel(Font teenyTny, Font sml){
        Dimension size = getPreferredSize();
        size.width = 375;
        setPreferredSize(size);
        setBackground(Color.WHITE);

        fontTeenyTny = teenyTny;
        fontSml = sml;

        JButton playButton = new JButton("play"){
            public void setBorder(Border border) {
                // None
            }
        };
        playButton.setForeground(Color.LIGHT_GRAY);
        playButton.setFont(fontSml);
        playButton.setOpaque(false);
        playButton.addActionListener(e -> {

            String selection = list.getSelectedValue();
            if (selection == null){
                return;
            }

            if(!playing) {
                try {
                    song = AudioSystem.getClip();
                    song.open(AudioSystem.getAudioInputStream(new File("rec/" + selection + ".wav")));
                    song.start();
                    playButton.setForeground(Color.BLUE);
                    playing = true;

                    toCancel = executor.submit(() -> {
                        long length = song.getMicrosecondLength()/1000;
                        try {
                            Thread.sleep(length);
                        } catch (InterruptedException e1) {
                            // null
                        }
                        playButton.setForeground(Color.LIGHT_GRAY);
                        playing = false;

                    });


                } catch (Exception f) {
                    f.printStackTrace();
                }
            }
            else{
                song.stop();
                toCancel.cancel(true);
                playButton.setForeground(Color.LIGHT_GRAY);
                playing = false;
            }

        });

        JButton delButton = new JButton("delete"){
            public void setBorder(Border border) {
                // None
            }
        };
        delButton.setForeground(Color.LIGHT_GRAY);
        delButton.setFont(fontSml);
        delButton.setOpaque(false);
        delButton.addActionListener(e -> {


            int position = list.getSelectedIndex();

            if (position == -1){
                return;
            }

            String selection = list.getSelectedValue();

            DeletePromptBoxFrame del = new DeletePromptBoxFrame(selection, new DeleteListener() {
                public void deleteEventOccurred(DeleteEvent event) {

                    recordings[position] = "DELETEME__";
                    saveRecordings.save(recordings);

                    loadRecordings();
                    repaint();
                    revalidate();

                    try {
                        Files.delete(Paths.get("rec/" + selection + ".wav"));
                    } catch (IOException e1) {
                        // null
                    }
                }
            });

        });

        JButton backButton = new JButton("return"){
            public void setBorder(Border border) {
                // None
            }
        };
        backButton.setForeground(Color.LIGHT_GRAY);
        backButton.setFont(fontSml);
        backButton.setOpaque(false);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firePanelEvent(new PanelEvent(this, 0, 2));
            }
        });

        setLayout(null);
        add(playButton);
        add(delButton);
        add(backButton);

        Insets insets = getInsets();
        Dimension s = playButton.getPreferredSize();
        playButton.setBounds(33 + insets.left, 115 + insets.top, s.width, s.height);

        s = delButton.getPreferredSize();
        delButton.setBounds(132 + insets.left, 115 + insets.top, s.width, s.height);

        s = backButton.getPreferredSize();
        backButton.setBounds(260 + insets.left, 115 + insets.top, s.width, s.height);
    }

    public void loadRecordings(){
        ArrayList<String> temp = readRecordings.read();

        recordings = new String[temp.size()];
        int i = 0;
        for(String s: temp){
            recordings[i++] = s;
        }

        list = new JList<>(recordings);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setFont(fontTeenyTny);
        list.setForeground(Color.GRAY);

        if (scrollPane != null){
            remove(scrollPane);
        }

        scrollPane = new JScrollPane(list);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(340, 85));
        add(scrollPane, BorderLayout.NORTH);

        Dimension s = scrollPane.getPreferredSize();
        scrollPane.setBounds(20, 20, s.width, s.height);

    }

    public void firePanelEvent(PanelEvent event){
        Object[] listeners = listenerList.getListenerList();

        for(int i = 0; i < listeners.length; i += 2){
            if(listeners[i] == PanelListener.class){
                ((PanelListener)listeners[i + 1]).panelEventOccurred(event);
            }
        }
    }

    public void addPanelListener(PanelListener listener){
            listenerList.add(PanelListener.class, listener);
        }

    public void removeDetailListener(PanelListener listener){
        listenerList.remove(PanelListener.class, listener);
    }

}
