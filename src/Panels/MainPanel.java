package Panels;

import Events.PanelEvent;
import Events.PanelListener;
import Panels.Modules.MetroGnome;
import Panels.Modules.Record;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MainPanel extends JPanel {


    private Object delayLock = new Object();

    private Font fontLrg;
    private Font fontSml;
    private Font fontTny;
    private String previous = "096";
    private final JTextField bpmField;

    private MetroGnome metronome;
    private boolean on = false;
    private Record record;
    private boolean recording = false;
    private Future toCancel;

    private ExecutorService executor = Executors.newFixedThreadPool(8);

    private volatile int incDelay = 10;
    private int increase = 1;
    private Future cancelInc;


    // Coloured buttons
    private JButton playButton;
    private JButton incButton;
    private JButton recButton;


    public MainPanel(Font lrg, Font sml, Font tny, MetroGnome metronomeInput){

        // Size and instance variable initialization.

        Dimension size = getPreferredSize();
        size.width = 375;
        setPreferredSize(size);
        setBackground(Color.WHITE);

        fontLrg = lrg;
        fontSml = sml;
        fontTny = tny;

        record = new Record();

        this.metronome = metronomeInput;

    // Play button, toggles the metronome in a new thread.
        playButton = new JButton("Go"){
            public void setBorder(Border border) {
                // None
            }
        };
        playButton.setForeground(Color.LIGHT_GRAY);
        playButton.setFont(fontTny);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                toCancel = executor.submit(new Runnable() {
                    public void run() {

                        if(on){
                            on = false;
                            playButton.setForeground(Color.LIGHT_GRAY);
                        }
                        else{
                            on = true;
                            playButton.setForeground(Color.BLUE);
                        }

                        metronome.toggle();
                    }
                });
            }
        });


    // Inc button, submits a new thread that will sleep for the set amount of incDelay and then increase the bpm.
        incButton = new JButton("inc."){
            public void setBorder(Border border) {
                // None
            }
        };
        incButton.setForeground(Color.LIGHT_GRAY);
        incButton.setFont(fontSml);
        incButton.setOpaque(false);
        incButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // If inc is already on we need to turn it off.
                if (cancelInc != null) {
                    cancelInc.cancel(true);
                    cancelInc = null;
                    incButton.setForeground(Color.LIGHT_GRAY);
                } else {
                    cancelInc = executor.submit(new Runnable() {
                        public void run() {

                            incButton.setForeground(Color.BLUE);

                            while (true) {

                                // Sleep before increasing the bpm
                                try {
                                    Thread.sleep(incDelay * 1000);
                                } catch (InterruptedException e) {
                                    return;
                                }


                                // Parse what the current bpm is and increment it
                                double inField = Double.parseDouble(bpmField.getText()) + increase;

                                // The max bpm is 999
                                if (inField == 1000) {
                                    return;
                                }

                                // Proper formatting for the number we are to set in the JTextField.
                                String toSet = Integer.toString((int) inField);
                                if (toSet.length() == 1) {
                                    toSet = "00" + toSet;
                                } else if (toSet.length() == 2) {
                                    toSet = "0" + toSet;
                                }

                                System.out.println(toSet);

                                // Actually setting the number to both the field and the metronome.
                                bpmField.setText(toSet);
                                previous = toSet;
                                metronome.setBPM((int) inField);
                            }
                        }
                    });
                }
            }
        });


    // Options takes us to a new screen, the option screen.
        JButton optionButton = new JButton("| options |"){
            public void setBorder(Border border) {
                // None
            }
        };
        optionButton.setForeground(Color.LIGHT_GRAY);
        optionButton.setFont(fontSml);
        optionButton.setOpaque(false);
        optionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                firePanelEvent(new PanelEvent(this, 1, 0));

            }
        });

    // Rec takes us to a new screen, the recordings screen.
        recButton = new JButton("rec."){
            public void setBorder(Border border) {
                // None
            }
        };
        recButton.setForeground(Color.LIGHT_GRAY);
        recButton.setFont(fontSml);
        recButton.setOpaque(false);
        recButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                firePanelEvent(new PanelEvent(this, 2, 0));

            }
        });

        // Rec takes us to a new screen, the recordings screen.
        JButton recordButton = new JButton("rec"){
            public void setBorder(Border border) {
                // None
            }
        };
        recordButton.setForeground(Color.LIGHT_GRAY);
        recordButton.setFont(fontLrg);
        recordButton.setOpaque(false);
        recordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executor.submit(() -> {

                    if(recording){
                        recording = false;
                        recordButton.setForeground(Color.LIGHT_GRAY);
                    }
                    else{
                        recording = true;
                        recordButton.setForeground(Color.RED);
                    }

                   record.toggleRecord();
                });
            }
        });

    // + Speed submits a new thread that will increment the textfield (much in the same way as the incButton does)
        JButton plusButton = new JButton("+"){
            public void setBorder(Border border) {
                 // None
            }
        };
        plusButton.setForeground(Color.LIGHT_GRAY);
        plusButton.setFont(fontSml);
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                executor.submit(new Runnable() {
                    public void run() {

                        double inField = Double.parseDouble(bpmField.getText()) + 1;

                        if(inField == 1000) {
                            return;
                        }

                        String toSet = Integer.toString((int)inField);
                        if(toSet.length() == 1){
                            toSet = "00" + toSet;
                        }
                        else if(toSet.length() == 2){
                            toSet = "0" + toSet;
                        }

                        System.out.println(toSet);

                        bpmField.setText(toSet);
                        previous = toSet;
                        metronome.setBPM((int)inField);

                    }
                });
            }
        });

        plusButton.setBorderPainted(false);

    // - Speed, works the same as the + button, decrements the textfield by 1
        JButton minusButton = new JButton("-"){
            public void setBorder(Border border) {
                // None
            }
        };
        minusButton.setForeground(Color.LIGHT_GRAY);
        minusButton.setFont(fontSml);
        minusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executor.submit(new Runnable() {
                    public void run() {

                        double inField = Double.parseDouble(bpmField.getText()) - 1;

                        if(inField == -1) {
                            return;
                        }

                        String toSet = Integer.toString((int)inField);
                        if(toSet.length() == 1){
                            toSet = "00" + toSet;
                        }
                        else if(toSet.length() == 2){
                            toSet = "0" + toSet;
                        }

                        System.out.println(toSet);

                        bpmField.setText(toSet);
                        previous = toSet;
                        metronome.setBPM((int)inField);

                    }
                });
            }
        });

        // The text field itself, we add a document listener to control what inputs are accepted.
        bpmField = new JTextField(previous, 4){
            public void setBorder(Border border) {
                // None
            }
        };
        bpmField.setForeground(Color.LIGHT_GRAY);
        bpmField.setFont(fontLrg);
        bpmField.setOpaque(false);
        AbstractDocument document = (AbstractDocument) bpmField.getDocument();
        document.setDocumentFilter(new DocumentFilter() {

               private final int maxCharacters = 3;

               public void insertString(FilterBypass fb, int offs,
                                        String str, AttributeSet a)
                       throws BadLocationException {

                   // Based on length of string, also must be numeric
                   if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
                       try {
                           double inp = Double.parseDouble(str);
                           super.insertString(fb, offs, str, a);

                           double inField = Double.parseDouble(bpmField.getText());

                           executor.submit(new Runnable() {

                               public void run() {
                                   metronome.setBPM((int)inField);

                                   if(toCancel != null && metronome.status()) {

                                       toCancel.cancel(true);

                                       toCancel = executor.submit(new Runnable() {

                                           public void run() {
                                               metronome.pulseLoop();
                                           }
                                       });
                                   }
                               }
                           });


                       } catch (NumberFormatException e) {
                           Toolkit.getDefaultToolkit().beep();
                       }
                   } else
                       Toolkit.getDefaultToolkit().beep();
               }

               public void replace(FilterBypass fb, int offs,
                                   int length,
                                   String str, AttributeSet a)
                       throws BadLocationException {


                   // Again based on length
                   if ((fb.getDocument().getLength() + str.length()
                           - length) <= maxCharacters)
                       try {
                           double inp = Double.parseDouble(str);
                           super.replace(fb, offs, length, str, a);

                           double inField = Double.parseDouble(bpmField.getText());

                           executor.submit(new Runnable() {

                               public void run() {
                                   metronome.setBPM((int)inField);


                                   if(toCancel != null && metronome.status()) {

                                       toCancel.cancel(true);

                                       toCancel = executor.submit(new Runnable() {

                                           public void run() {
                                               metronome.pulseLoop();
                                           }
                                       });
                                   }
                               }
                           });

                       } catch (NumberFormatException e) {
                           Toolkit.getDefaultToolkit().beep();
                       }
               }

               public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

                   try {
                       double inFieldTest = Double.parseDouble(bpmField.getText());
                       super.remove(fb, offset, length);
                       double inField = Double.parseDouble(bpmField.getText());

                       executor.submit(new Runnable() {

                           public void run() {
                               metronome.setBPM((int) inField);

                               if(toCancel != null && metronome.status()) {

                                   toCancel.cancel(true);

                                   toCancel = executor.submit(new Runnable() {

                                       public void run() {
                                           metronome.pulseLoop();
                                       }
                                   });
                               }
                           }
                       });
                   }
                   catch(NumberFormatException e){
                       Toolkit.getDefaultToolkit().beep();
                   }
               }
           });


        // For absolute positioning we cannot use a layout.
        setLayout(null);

        // We add all of our components to the JPanel
        add(bpmField);
        add(playButton);
        add(plusButton);
        add(minusButton);
        add(incButton);
        add(optionButton);
        add(recButton);
        add(recordButton);

        // We find where the JPanel is on the JFrame by getting the insets
        Insets insets = getInsets();

        // We then get the size of the component and select it's position.
        Dimension s = bpmField.getPreferredSize();

        // The position depends on the JPanel's insets
        // It also requires the size of the JPanel to set boundaries for the component.
        bpmField.setBounds(40 + insets.left, 20 + insets.top, s.width - 72, s.height);

        s = plusButton.getPreferredSize();
        plusButton.setBounds(168 + insets.left, 25 + insets.top, s.width, s.height - 10);
        minusButton.setBounds(167 + insets.left, 47 + insets.top, s.width, s.height - 20);

        s = playButton.getPreferredSize();
        playButton.setBounds(168 + insets.left, 80 + insets.top, s.width, s.height);

        s = recordButton.getPreferredSize();
        recordButton.setBounds(250 + insets.left, 20 + insets.top, s.width, s.height);

        s = incButton.getPreferredSize();
        incButton.setBounds(41 + insets.left, 115 + insets.top, s.width, s.height);

        s = optionButton.getPreferredSize();
        optionButton.setBounds(102 + insets.left, 115 + insets.top, s.width, s.height);

        s = recButton.getPreferredSize();
        recButton.setBounds(257 + insets.left, 115 + insets.top, s.width, s.height);

    }



    // This method looks at the JComponent instance variable listenerList.
    // It takes this entire list and traverses it two elements at a time (the lis tis of the form [class, listner, class, lis...]
    // When it finds the correct listener matching the event that has occurred-in this case a Events.PanelEvent-
    // the method calls the panelEventOccurred method defined in the Panels.MainFrame class (will act to chane panels).
    private void firePanelEvent(PanelEvent event){
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

    public void setDelay(int delay){
        synchronized (delayLock) {
            incDelay = delay;
        }
    }

    public void setIncrease(int increase){
        synchronized (delayLock) {
            this.increase = increase;
        }
    }

    public void init(){
        if(cancelInc != null){
            cancelInc.cancel(true);
            cancelInc = null;
        }
        on = false;
        playButton.setForeground(Color.LIGHT_GRAY);
        incButton.setForeground(Color.LIGHT_GRAY);
    }

}


