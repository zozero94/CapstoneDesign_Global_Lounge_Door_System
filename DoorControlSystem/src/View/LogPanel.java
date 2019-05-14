package view;

import model.dto.ExcelOutInfo;
import view.right.GuiConstant;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LogPanel extends JPanel {

    private static LogPanel logPanel = null;
    private JTable tableLog;
    private JScrollPane scroll;
    private JScrollBar scrollBar;
    private DefaultTableModel model;

    private JPanel line;
    private JPanel imagePanel;
    private JLabel lbl[];
    private ImageIcon studentImage;

    private static final String CALNAME[] = {"학번","이름", "성별", "국가","학과","단대", "출입시간"};
    private static final String LABELNAME[] = {"","이름", "학번", "성별", "국가","학과","단대"};
    public static synchronized LogPanel getInstance(){
        if(logPanel == null) logPanel = new LogPanel();
        return logPanel;
    }
    private LogPanel(){
        super();
        createComponent();
        setComponentUi();
        setComponentPos();
        addComponent();
    }

    private void createComponent(){
        model = new DefaultTableModel(CALNAME, 0);
        tableLog = new JTable(model);
        scroll = new JScrollPane(tableLog);
        imagePanel = new JPanel();
        lbl = new JLabel[7];
        for(int i = 0 ; i < LABELNAME.length; i++) lbl[i] = new JLabel(LABELNAME[i]);
        line = new JPanel();
    }
    private void setComponentUi() {
        this.setPreferredSize(new Dimension(1033, 700));
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.scroll.setWheelScrollingEnabled(true);
        this.imagePanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        this.imagePanel.setBackground(Color.WHITE);
        this.line.setBackground(GuiConstant.LINE);
    }
    private void addComponent(){
        this.add(imagePanel);
        this.imagePanel.add(lbl[0]);
        for(int i = 1 ; i < lbl.length; i++)
            this.add(lbl[i]);
        this.add(scroll);
        this.add(line);
    }
    public void setComponentPos(){
        this.imagePanel.setBounds(30, 203, 210,210);
        for(int i = 1; i < lbl.length; i++) this.lbl[i].setBounds(30,420 + ((i - 1) * 40),100,40);
        this.line.setBounds(0,70, 1033, 100);
        this.scroll.setBounds(250, 203,750,470);
    }
    public void scrollDown(){
        scrollBar = scroll.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }
    public void imageChange(Image studentImage){
        this.studentImage = new ImageIcon(studentImage);
        this.lbl[0].setIcon(this.studentImage);
    }

    public void insertTableData(ExcelOutInfo info){
        Object data[] = new Object[CALNAME.length];
        for(int i = 0; i < CALNAME.length; i++)
            data[i] = info.getInfo(i);
        this.model.addRow(data);
        this.scrollDown();
    }
    //                DefaultTableModel m = (DefaultTableModel)mainPanel.getRightPanel().getTableLog().getModel();
    //                m.addRow(new String[]{"과연?","된걸까?"});
    //                mainPanel.getRightPanel().scrollDown();


}
