package view.right;

import view.LogChartPanel;
import view.LogPanel;

import javax.swing.*;
import java.awt.*;

public class RightCenterPanel extends JPanel {

    private LogChartPanel logChartPanel;
    private LogPanel logPanel;
    private CardLayout cardLayout;

    public RightCenterPanel(){
        super();
        createComponent();
        setComponentUi();
        addComponent();
    }
    private void createComponent(){
        logChartPanel = new LogChartPanel();
        logPanel = LogPanel.getInstance();
        cardLayout = new CardLayout();
    }
    private void setComponentUi(){
        this.setPreferredSize(new Dimension(1033, 700));
        this.setLayout(cardLayout);

    }
    private void addComponent(){
        this.add(logPanel, "log");
        this.add(logChartPanel, "chart");
    }

    public void changePanel(JButton btn){
        if(btn.getText().equals("Log"))
            cardLayout.show(this, "log");
        else
            cardLayout.show(this, "chart");
    }
}
