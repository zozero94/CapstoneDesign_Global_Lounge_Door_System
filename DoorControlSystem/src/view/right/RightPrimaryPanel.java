package view.right;

import view.left.BtnPanel;

import javax.swing.*;
import java.awt.*;

public class RightPrimaryPanel extends JPanel{

    private static RightPrimaryPanel rightPrimaryPanel = null;
    private CardLayout cardLayout;
    private LogChartPanel logChartPanel;
    private LogPanel logPanel;

    public static synchronized RightPrimaryPanel getInstance(){
        if(rightPrimaryPanel == null) rightPrimaryPanel = new RightPrimaryPanel();
        return rightPrimaryPanel;
    }
     private RightPrimaryPanel(){
           super();
            setUi();
            setComponentPos();
            addComponent();
     }

     private void createComponent(){
         cardLayout = new CardLayout();
         logChartPanel = LogChartPanel.getInstance();
         logPanel = LogPanel.getInstance();
     }
     private void setUi(){
         createComponent();
         this.setPreferredSize(new Dimension(1033, 768));
         this.setBackground(Color.white);
         this.setLayout(cardLayout);
     }
     private void setComponentPos(){
     }

     private void addComponent(){
         this.add(logPanel, BtnPanel.BUTTON_NAME[0]);
         this.add(logChartPanel, BtnPanel.BUTTON_NAME[1]);
     }

    public void changePanel(JButton btn){
        if(btn.getText().equals(BtnPanel.BUTTON_NAME[0]))
            cardLayout.show(this, BtnPanel.BUTTON_NAME[0]);
        else if(btn.getText().equals(BtnPanel.BUTTON_NAME[1]))
            cardLayout.show(this, BtnPanel.BUTTON_NAME[1]);
    }

    public LogChartPanel getLogChartPanel() {
        return logChartPanel;
    }
    public LogPanel getLogPanel() {
        return logPanel;
    }
}
