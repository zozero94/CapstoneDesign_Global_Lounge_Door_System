package view.right;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends JPanel {

    private RightCenterPanel center;
    private JButton btnLogChart;
    private JButton btnLog;

    public RightPanel(){
        super();
        createComponent();
        setUi();
        setComponentPos();
        addComponent();
    }

    private void createComponent(){
        btnLog = new JButton("Log");
        btnLogChart = new JButton("Chart");
        center = new RightCenterPanel();
    }
    private void setUi(){
        this.setPreferredSize(new Dimension(1033, 768));
        this.setBackground(Color.white);
        this.setLayout(null);
        this.btnLog.setBorderPainted(false);
        this.btnLogChart.setBorderPainted(false);

    }
    private void setComponentPos(){
        this.btnLogChart.setBounds(150,10, 140, 50);
        this.btnLog.setBounds(10,10, 140, 50);
        this.center.setBounds(10,60, 1033,700);
    }
    private void addComponent(){
        this.add(btnLog);
        this.add(btnLogChart);
        this.add(center);
    }

    public void changBtnColor(JButton btn){
        if(btn == this.btnLog)
        {
            this.btnLogChart.setBackground(GuiConstant.BACK_COLOR);
            this.btnLog.setBackground(GuiConstant.BACK_COLOR_CHANG);
        }else{
            this.btnLogChart.setBackground(GuiConstant.BACK_COLOR_CHANG);
            this.btnLog.setBackground(GuiConstant.BACK_COLOR);
        }
    }
    public JButton getBtnLogChart() {
        return btnLogChart;
    }
    public void setBtnLogChart(JButton btnLogChart) {
        this.btnLogChart = btnLogChart;
    }
    public JButton getBtnLog() {
        return btnLog;
    }
    public void setBtnLog(JButton btnLog) {
        this.btnLog = btnLog;
    }
    public RightCenterPanel getCenter() {
        return center;
    }
    public void setCenter(RightCenterPanel center) {
        this.center = center;
    }
}
