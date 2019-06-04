package control.action_handel;

import view.GuiConstant;
import view.right.LogPanel;

import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = LogPanel.getInstance().getTableLog().getSelectedRow();
        LogPanel.getInstance().imageChange(GuiConstant.getImageUrl(LogPanel.getInstance().getTableLog().getValueAt(row,0).toString()), LogPanel.getInstance().getTableLog().getValueAt(row,0).toString());
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override//마우스가 버튼 안으로 들어오면 빨간색으로 바뀜
    public void mouseEntered(MouseEvent e) {

    }
    @Override//마우스가 버튼 밖으로 나가면 노란색으로 바뀜
    public void mouseExited(MouseEvent e) {

    }
}
