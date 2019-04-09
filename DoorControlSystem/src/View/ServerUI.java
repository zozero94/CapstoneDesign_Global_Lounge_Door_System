package View;

import javax.swing.*;

public class ServerUI {

    private JFrame UI;
    private PrimaryPanel mainPanel;
    public ServerUI(){
         createComponent();
        setUI();
    }
    private void setUI(){
        UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UI.setLocation(0,0);
        UI.add(mainPanel);
        UI.pack();
        UI.setVisible(true);

    }
    private void createComponent(){
        UI = new JFrame("Door Control System");
        mainPanel = new PrimaryPanel();
    }

}
