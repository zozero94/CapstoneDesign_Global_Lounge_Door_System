package view;
import control.action_handel.ButtonListener;
import control.action_handel.MouseListener;
import model.DataAccessObject;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class ServerUI{

    private JFrame UI;
    private PrimaryPanel mainPanel;

    public ServerUI(){
        setUI();
    }
    private void setUI() {
        createComponent();
        UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UI.setLocation(0,0);
        addComponent();
        UI.setResizable(false);
        UI.setVisible(true);
        UI.pack();
    }
    private void createComponent(){
        UI = new JFrame("Door control System");
        mainPanel = new PrimaryPanel();
    }
    private void addComponent(){
        UI.getContentPane().add(mainPanel);
        UI.addWindowListener(new ExitListener());
    }

    public void addActionListener(MouseListener mouseListener, ButtonListener buttonListener){
        this.mainPanel.getBtnPanel().setBtnMouseListener(mouseListener);
        this.mainPanel.getBtnPanel().setBtnActionListener(buttonListener);
    }
    class ExitListener implements WindowListener {
        public void windowClosing(WindowEvent e) {
            DataAccessObject.getInstance().setAllLoginFlag();
        }
        public void windowActivated(WindowEvent e) {
        }
        public void windowClosed(WindowEvent e) {
        }
        public void windowDeactivated(WindowEvent e) {
        }
        public void windowDeiconified(WindowEvent e) {
        }
        public void windowIconified(WindowEvent e) {
        }
        public void windowOpened(WindowEvent e) {
        }
    }
}
