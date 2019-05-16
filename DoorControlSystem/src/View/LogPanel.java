package view;

import model.dto.ExcelOutInfo;
import model.dto.Student;
import view.right.GuiConstant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LogPanel extends JPanel {

    private static LogPanel logPanel = null;
    private JTable tableLog;
    private JScrollPane scroll;
    private JScrollBar scrollBar;
    private DefaultTableModel model;
    private DefaultTableCellRenderer tableCellRenderer;

    private JPanel line;
    private JPanel imagePanel;
    private JLabel lbl[];
    private ImageIcon studentImage;


    private static final Font TEXT_FONT = new Font("궁서", 0, 12);
    private static final Font TEXT_FONT_HEAD = new Font("궁서", 0, 15);
    private static final String CALNAME[] = {"학번","이름", "성별", "국가","학과","단대", "출입시간"};
    private static final String LABEL[] = {"","학번", "이름", "성별", "국가","학과","단대"};

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
        tableCellRenderer = new DefaultTableCellRenderer();
        tableLog = new JTable(model);
        scroll = new JScrollPane(tableLog);
        imagePanel = new JPanel();
        lbl = new JLabel[7];
        for(int i = 0; i < LABEL.length; i++) lbl[i] = new JLabel(LABEL[i]);
        line = new JPanel();
    }
    private void setComponentUi() {
        this.setPreferredSize(new Dimension(1033, 700));
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.scroll.setWheelScrollingEnabled(true);
        this.scroll.setBackground(Color.WHITE);
        this.scroll.getViewport().setBackground(Color.WHITE);

        this.imagePanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        this.imagePanel.setBackground(Color.WHITE);

        for(int i = 0; i < LABEL.length; i++) lbl[i].setFont(TEXT_FONT_HEAD);

        this.line.setBackground(GuiConstant.LINE);

        this.tableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        this.tableLog.setGridColor(Color.WHITE);
        this.tableLog.setBackground(Color.WHITE);
        this.tableLog.setSelectionBackground(GuiConstant.TEXT);
        //this.tableLog.setGridColor(GuiConstant.LINE);


        this.tableLog.setFont(TEXT_FONT);
        this.tableLog.getTableHeader().setFont(TEXT_FONT_HEAD);
        this.tableLog.getTableHeader().setBackground(GuiConstant.LINE);
        this.tableLog.getTableHeader().setForeground(Color.white);
    }
    private void addComponent(){
        this.add(imagePanel);
        this.imagePanel.add(lbl[0]);
        for(int i = 1 ; i < lbl.length; i++)
            this.add(lbl[i]);
        this.add(scroll);
        this.add(line);
    }
    private void setComponentPos(){
        this.imagePanel.setBounds(30, 203, 210,210);
        for(int i = 1; i < lbl.length; i++) this.lbl[i].setBounds(30,420 + ((i - 1) * 40),100,40);
        this.line.setBounds(0,60, 1033, 110);
        this.scroll.setBounds(250, 203,750,470);
    }
    private void scrollDown(){
        scrollBar = scroll.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }
    public void imageChange(Image studentImage, Student student){
        this.studentImage = new ImageIcon(studentImage);
        this.lbl[0].setIcon(this.studentImage);
        for(int i = 1; i < LABEL.length; i++){
            lbl[i].setText(student.getString(i));
        }

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
