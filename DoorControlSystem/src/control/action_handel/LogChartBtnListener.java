package control.action_handel;
import view.right.LogChartPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogChartBtnListener implements ActionListener {
    private LogChartPanel logChartPanel = LogChartPanel.getInstance();
    int i = 0;
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(logChartPanel.getChartType())) logChartPanel.showDayLogChart(logChartPanel.getChartType().getSelectedItem().toString());
        else logChartPanel.changDate((JButton)e.getSource());
    }
}