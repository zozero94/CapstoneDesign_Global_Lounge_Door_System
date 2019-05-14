package view.right;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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
            this.btnLogChart.setBackground(GuiConstant.RIGHTPANEL_2);
            this.btnLog.setBackground(GuiConstant.RIGHTPANEL);
        }else{
            this.btnLogChart.setBackground(GuiConstant.RIGHTPANEL);
            this.btnLog.setBackground(GuiConstant.RIGHTPANEL_2);
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
