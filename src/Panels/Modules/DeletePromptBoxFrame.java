package Panels.Modules;

import Events.DeleteListener;

import javax.swing.*;

public class DeletePromptBoxFrame extends JFrame {

    private DeletePromptBoxPanel delPanel;

    public DeletePromptBoxFrame(String name, DeleteListener listener) {
        super();

        super.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        delPanel = new DeletePromptBoxPanel(name, listener);
        add(delPanel);

        pack();
        setSize(320, 100);
        setVisible(true);
        setResizable(false);
    }
}
