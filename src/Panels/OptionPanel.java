package Panels;

import Events.PanelEvent;
import Events.PanelListener;
import Panels.Modules.MetroGnome;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class OptionPanel extends JPanel{

    private MetroGnome metronome;
    private Font fontTny;
    private Font fontSml;

    private JTextField incBy;
    private JTextField timeBy;
    private JTextField message;

    private final int INITDELAY = 10;
    private final int INITINCREASE = 1;

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    public OptionPanel(Font tny, Font sml, MetroGnome metronome) {
        Dimension size = getPreferredSize();
        size.width = 375;
        setPreferredSize(size);
        setBackground(Color.WHITE);

        fontTny = tny;
        fontSml = sml;
        this.metronome = metronome;

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

                firePanelEvent(new PanelEvent(this, 0, 1, Integer.parseInt(timeBy.getText()), Integer.parseInt(incBy.getText())));

            }
        });

        // These tone buttons have the functionality that they submit a new thread to change the tone of the metronome.

        JButton tone1Button = new JButton("tone #1 |"){
            public void setBorder(Border border) {
                // None
            }
        };
        tone1Button.setForeground(Color.LIGHT_GRAY);
        tone1Button.setFont(fontTny);
        tone1Button.setOpaque(false);
        tone1Button.addActionListener(e -> executor.submit(() -> metronome.changeSound(1)));

        JButton tone2Button = new JButton("tone #2 |"){
            public void setBorder(Border border) {
                // None
            }
        };
        tone2Button.setForeground(Color.LIGHT_GRAY);
        tone2Button.setFont(fontTny);
        tone2Button.setOpaque(false);
        tone2Button.addActionListener(e -> executor.submit(() -> metronome.changeSound(2)));

        JButton tone3Button = new JButton("tone #3"){
            public void setBorder(Border border) {
                // None
            }
        };
        tone3Button.setForeground(Color.LIGHT_GRAY);
        tone3Button.setFont(fontTny);
        tone3Button.setOpaque(false);
        tone3Button.addActionListener(e -> executor.submit(() -> metronome.changeSound(3)));



        String m = "Increase by        every       seconds.";
        message = new JTextField(m, 100){
            public void setBorder(Border border) {
                // None
            }
        };
        message.setForeground(Color.LIGHT_GRAY);
        message.setFont(fontTny);
        message.setOpaque(false);
        message.setFocusable(false);


        incBy = new JTextField("0" + Integer.toString(INITINCREASE), 2){
            public void setBorder(Border border) {
                // None
            }
        };
        incBy.setForeground(Color.LIGHT_GRAY);
        incBy.setFont(fontTny);
        incBy.setOpaque(false);
        AbstractDocument document1 = (AbstractDocument) incBy.getDocument();
        document1.setDocumentFilter(new DocumentFilter() {
            private final int maxCharacters = 2;

            public void insertString(FilterBypass fb, int offs,
                                     String str, AttributeSet a)
                    throws BadLocationException {

                if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
                    try {
                        double inp = Double.parseDouble(str);
                        super.insertString(fb, offs, str, a);
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

                if ((fb.getDocument().getLength() + str.length()
                        - length) <= maxCharacters)
                    try {
                        double inp = Double.parseDouble(str);
                        super.replace(fb, offs, length, str, a);

                        executor.submit(() -> {
                            try {
                                Thread.sleep(800);
                                if (inp < 10) {
                                    insertString(fb, offs, "0", a);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });
                    }
                        catch (NumberFormatException e) {
                        Toolkit.getDefaultToolkit().beep();
                    }
            }

            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

                try {
                    double inFieldTest = Double.parseDouble(incBy.getText());
                    super.remove(fb, offset, length);
                }
                catch(NumberFormatException e){
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        timeBy = new JTextField(Integer.toString(INITDELAY), 4){
            public void setBorder(Border border) {
                // None
            }
        };
        timeBy.setForeground(Color.LIGHT_GRAY);
        timeBy.setFont(fontTny);
        timeBy.setOpaque(false);

        AbstractDocument document2 = (AbstractDocument) timeBy.getDocument();
        document2.setDocumentFilter(new DocumentFilter() {
            private final int maxCharacters = 2;

            public void insertString(FilterBypass fb, int offs,
                                     String str, AttributeSet a)
                    throws BadLocationException {

                if ((fb.getDocument().getLength() + str.length()) <= maxCharacters) {
                    try {
                        double inp = Double.parseDouble(str);
                        super.insertString(fb, offs, str, a);
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


                if ((fb.getDocument().getLength() + str.length()
                        - length) <= maxCharacters)
                    try {
                        double inp = Double.parseDouble(str);
                        super.replace(fb, offs, length, str, a);

                        executor.submit(() -> {
                            try {
                                Thread.sleep(800);
                                if(inp < 10 ){
                                    insertString(fb, offs, "0", a);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        });
                    }
                    catch (NumberFormatException e) {
                        Toolkit.getDefaultToolkit().beep();
                    }
            }

            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

                try {
                    double inFieldTest = Double.parseDouble(timeBy.getText());
                    super.remove(fb, offset, length);
                }
                catch(NumberFormatException e){
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });


        setLayout(null);
        add(backButton);
        add(tone1Button);
        add(tone2Button);
        add(tone3Button);
        add(incBy);
        add(timeBy);
        add(message);

        Insets insets = getInsets();
        Dimension s = backButton.getPreferredSize();
        backButton.setBounds(260 + insets.left, 115 + insets.top, s.width, s.height);

        s = tone1Button.getPreferredSize();
        tone1Button.setBounds(41 + insets.left, 20 + insets.top, s.width, s.height);
        tone2Button.setBounds(41 + insets.left + s.width + 9, 20 + insets.top, s.width, s.height);
        tone3Button.setBounds(41 + insets.left + 2*(s.width) + 10, 20 + insets.top, s.width, s.height);

        s = message.getPreferredSize();
        message.setBounds(41 + insets.left, 70 + insets.top, s.width, s.height);

        s = incBy.getPreferredSize();
        incBy.setBounds(152 + insets.left, 70 + insets.top, s.width, s.height);
        timeBy.setBounds(242 + insets.left, 70 + insets.top, s.width, s.height);
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

    public String toString(){
        return "OPTION_PANEL";
    }
}
