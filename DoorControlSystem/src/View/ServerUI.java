package view;

import model.DataAccessObject;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ServerUI{

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
        UI.addWindowListener(new ExitListener());
        UI.pack();
        UI.setVisible(true);

    }
    private void createComponent(){
        UI = new JFrame("Door control System");
        mainPanel = new PrimaryPanel();
    }

    class ExitListener implements WindowListener {
        public void windowClosing(WindowEvent e) {
            DataAccessObject.getInstance().setAlltLoginFlag();
        }
        /* 나머지 이벤트 빈 오버라이딩 */
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
