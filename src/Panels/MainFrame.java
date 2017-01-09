package Panels;

import Events.PanelEvent;
import Events.PanelListener;
import Panels.Modules.MetroGnome;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainFrame extends JFrame {

    private Container c;

    private MainPanel mainPanel;
    private OptionPanel optionPanel;
    private RecPanel recPanel;
    private JPanel currentPanel;

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    private ImagePanel imagePanel;

    private MetroGnome metronome = new MetroGnome(Integer.parseInt("096"));

    private Font fontLrg;
    private Font fontSml;
    private Font fontTny;
    private Font fontTeenyTny;

    public MainFrame(String title){
        super(title);
        setLayout(new BorderLayout());


        // Font init
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/OpenSans-CondLight.ttf")));
        } catch (IOException|FontFormatException e) {
            e.printStackTrace();
        }

        fontLrg = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 40);
        fontSml = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 30);
        fontTny = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 18);
        fontTeenyTny = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 14);

        // Panel init
        mainPanel = new MainPanel(fontLrg, fontSml, fontTny, metronome);
        optionPanel = new OptionPanel(fontTny, fontSml, metronome);
        recPanel = new RecPanel(fontTeenyTny, fontSml);
        imagePanel = new ImagePanel();

        // This listener does the actual changing of the panels
        // Our events have information in them to tell us which panel to add, and we will always be removing the currentPanel.
        PanelListener listener = new PanelListener() {
            public void panelEventOccurred(PanelEvent event) {

                c = getContentPane();
                int num = event.getPanelNum();

                // If coming form the option panel than the delay and increase values need
                // to be set as they can be changed from this panel.
                if(event.getCurNum() == 1){

                    executor.submit(() -> {
                        System.out.println((event.getDelay()));

                        mainPanel.setDelay(event.getDelay());
                        mainPanel.setIncrease(event.getInc());
                    });
                }

                // Add main panel
                if(num == 0){
                    c.remove(currentPanel);
                    mainPanel.init();
                    currentPanel = mainPanel;
                    c.add(currentPanel, BorderLayout.EAST);
                    revalidate();
                    repaint();
                }

                // Add option panel
                else if(num == 1){
                    metronome.kill();
                    c.remove(currentPanel);
                    currentPanel = optionPanel;
                    c.add(currentPanel, BorderLayout.EAST);
                    revalidate();
                    repaint();
                }

                // Add recording panel
                else if (num == 2){
                    metronome.kill();
                    c.remove(currentPanel);
                    recPanel.loadRecordings();
                    currentPanel = recPanel;
                    c.add(currentPanel, BorderLayout.EAST);
                    revalidate();
                    repaint();
                }
            }
        };

        // We add the listener that we have made to each panel, so when an event occurs on that panel,
        // the consequence we defined here will occur.
        mainPanel.addPanelListener(listener);
        optionPanel.addPanelListener(listener);
        recPanel.addPanelListener(listener);

        currentPanel = mainPanel;

        c = getContentPane();
        c.add(imagePanel, BorderLayout.WEST);
        c.add(currentPanel, BorderLayout.EAST);

    }
}
