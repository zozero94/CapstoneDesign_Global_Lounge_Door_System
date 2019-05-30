package control.action_handel;

import control.socket.SystemServerSocket;
import view.left.BtnPanel;
import view.right.RightPrimaryPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e){

        BtnPanel.getInstance().turnButtonColor((JButton) e.getSource());
        RightPrimaryPanel.getInstance().changePanel((JButton) e.getSource());
        if (e.getSource().equals(BtnPanel.getInstance().getBtn(2))) BtnPanel.getInstance().saveExcel();
        else if (e.getSource().equals(BtnPanel.getInstance().getBtn(3))) {
            if (SystemServerSocket.getInstance().getRaspberrypi() != null) SystemServerSocket.getInstance().getRaspberrypi().openDoor();
            else JOptionPane.showMessageDialog(null, "출입문과의 연결을 확인하세요", "경고", JOptionPane.WARNING_MESSAGE);
        }

    }
}
