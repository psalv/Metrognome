import Panels.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainFrame frame = new MainFrame("Metrognome");
                frame.setSize(500, 200);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setVisible(true);

                // To fix the issue of focusing on the JTextField.
                // We use a robot to hit and release tab when the application is launched.
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_TAB);
                    robot.keyRelease(KeyEvent.VK_TAB);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
