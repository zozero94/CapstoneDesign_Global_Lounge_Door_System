package control.action_handel;

import control.socket.SystemServerSocket;
import view.BtnPanel;
import view.RightPrimaryPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e){

        BtnPanel.getInstance().turnButtonColor((JButton)e.getSource());
        RightPrimaryPanel.getInstance().changePanel((JButton)e.getSource());
        if(e.getSource().equals(BtnPanel.getInstance().getBtn(2)))
            BtnPanel.getInstance().saveExcel();
        if(e.getSource().equals(BtnPanel.getInstance().getBtn(3)))
            if(SystemServerSocket.getInstance().getRaspberrypi()!=null) SystemServerSocket.getInstance().getRaspberrypi().openDoor();
    }
}
