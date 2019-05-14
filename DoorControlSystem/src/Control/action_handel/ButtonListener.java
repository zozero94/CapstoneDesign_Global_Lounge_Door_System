package control.action_handel;

import model.dto.ExcelOutInfo;
import view.BtnPanel;
import view.LogPanel;
import view.RightPrimaryPanel;
import view.right.GuiConstant;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e){

        BtnPanel.getInstance().turnButtonColor((JButton)e.getSource());
        RightPrimaryPanel.getInstance().changePanel((JButton)e.getSource());
        //ControlMain.getInstance();
        ExcelOutInfo s = new ExcelOutInfo("14011070","민우","남자","한국","컴퓨터공학과","소프트웨어","2019-05-06 13:32:030");
        LogPanel.getInstance().insertTableData(s);
    }
}
