package control.action_handel;
import model.DataAccessObject;
import view.DateCalculator;
import view.LogChartPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogChartBtnListener implements ActionListener {


    private DataAccessObject dao = new DataAccessObject();
    @Override
    public void actionPerformed(ActionEvent e){

        if(LogChartPanel.getInstance().getChartType().getSelectedIndex() == 0)
            LogChartPanel.getInstance().showDayLogChart(dao.getShowLogData(DateCalculator.currentTimeMonth(), 0), LogChartPanel.getInstance().getChartType().getSelectedItem().toString()) ;
        else if(LogChartPanel.getInstance().getChartType().getSelectedIndex() == 1)
            LogChartPanel.getInstance().showDayLogChart(dao.getLogNationality(), LogChartPanel.getInstance().getChartType().getSelectedItem().toString()) ;
        else if(LogChartPanel.getInstance().getChartType().getSelectedIndex() == 2)
            LogChartPanel.getInstance().showDayLogChart(dao.getLogTime(), LogChartPanel.getInstance().getChartType().getSelectedItem().toString()) ;
    }
}