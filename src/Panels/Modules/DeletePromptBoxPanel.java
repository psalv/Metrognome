package Panels.Modules;

import Events.DeleteEvent;
import Events.DeleteListener;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class DeletePromptBoxPanel extends JPanel {

    private Font fontSml = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 23);
    private Font fontTny = new Font("OpenSans-CondLight", Font.TRUETYPE_FONT, 13);

    public DeletePromptBoxPanel(String name, DeleteListener listener) {
        super();

        setBackground(Color.WHITE);
        setLayout(null);

        addDeleteListener(listener);
        JLabel sure = new JLabel("Are you sure you want to delete the recording:");
        sure.setFont(fontTny);
        sure.setForeground(Color.LIGHT_GRAY);
        JLabel title = new JLabel(name);
        title.setFont(fontTny);
        title.setForeground(Color.RED);

        JButton yes = new JButton("yes"){
            public void setBorder(Border border) {
                // None
            }
        };
        yes.setFont(fontSml);
        yes.setForeground(Color.LIGHT_GRAY);
        yes.addActionListener(e -> {
            fireDeleteEvent(new DeleteEvent());
            SwingUtilities.getWindowAncestor(this).dispose();
        });


        JButton no = new JButton("no"){
            public void setBorder(Border border) {
                // None
            }
        };
        no.setFont(fontSml);
        no.setForeground(Color.LIGHT_GRAY);
        no.addActionListener(e -> {
            System.out.println("no clicked");
            SwingUtilities.getWindowAncestor(this).dispose();
            }
        );

        add(sure);
        add(title);
        add(yes);
        add(no);

        Dimension s = sure.getPreferredSize();
        sure.setBounds(15, 5, s.width, s.height);

        s = title.getPreferredSize();
        title.setBounds(5, 25, s.width, s.height);

        s = yes.getPreferredSize();
        yes.setBounds(90, 40, s.width, s.height);

        s = no.getPreferredSize();
        no.setBounds(185, 40, s.width, s.height);

        setSize(320, 100);
        setVisible(true);

    }


    public void fireDeleteEvent(DeleteEvent event){
        Object[] listeners = listenerList.getListenerList();

        for(int i = 0; i < listeners.length; i += 2){
            if(listeners[i] == DeleteListener.class){
                ((DeleteListener)listeners[i + 1]).deleteEventOccurred(event);
            }
        }
    }

    public void addDeleteListener(DeleteListener listener){
        listenerList.add(DeleteListener.class, listener);
    }


}
