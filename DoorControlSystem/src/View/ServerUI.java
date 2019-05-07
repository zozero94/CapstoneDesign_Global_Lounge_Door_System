package view;
import model.DataAccessObject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class ServerUI{


    private JFrame UI;
    private PrimaryPanel mainPanel;
    public ServerUI(){
        createComponent();
        setUI();


//        private ArrayList<String> st;
//        private  int i;
//        st = new ArrayList<String>();
//        st.add("14011038");
//        st.add("14011070");
//        st.add("14011078");
//        st.add("14011262");
//        i = 0;
        mainPanel.getRightPanel().btn.addActionListener(new ButtonListener());

    }
    private void setUI(){
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

        class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(mainPanel.getRightPanel().btn)){

            }
        }
    }


    class ExitListener implements WindowListener {
        public void windowClosing(WindowEvent e) {
            //UI 종료시에 데이터베이스의 로그인 flag를 모두 초기화
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
