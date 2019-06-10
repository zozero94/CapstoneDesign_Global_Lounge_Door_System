package view.right;

import control.action_handel.LogBtnListener;
import control.action_handel.MouseListener;
import control.db.Data;
import control.db.DataAccessObject;
import control.db.Student;
import view.DateCalculator;
import view.GuiConstant;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

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
    private JButton btn[];
    private JLabel lblCurDate;
    private Data data = new Data();

    private DataAccessObject dao;
    private DefaultTableCellRenderer renderer;
    private static final Font TEXT_FONT = new Font("HY견고딕", 0, 12);
    private static final Font TEXT_FONT_HEAD = new Font("HY견고딕", 0, 15);
    private static final Font TEXT_TIME = new Font("HY견고딕", 0, 30);
    private static final String CALNAME[] = {"학번","이름", "성별", "국가","학과","단대", "출입시간"};
    private static final int CAL_SIZE[] = {40, 70,20,60,80,80,100};
    private static final String LABEL[] = {"","학번", "이름", "성별", "국가","학과","단대"};
    private static final String BUTTON[] = {"<<","<","O",">",">>"};
    private static final int NEXT_TIME[][] = {{0,-1,0},{0,0,-1},{0,0,0},{0,0,1},{0,1,0}};

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
        dao = new DataAccessObject();
        model = new DefaultTableModel(CALNAME, 0){
            public boolean isCellEditable(int rowIndex, int mCollndex){
                return false;
            }
        };
        renderer = new DefaultTableCellRenderer();
        tableCellRenderer = new DefaultTableCellRenderer();
        tableLog = new JTable(model);
        scroll = new JScrollPane(tableLog);
        imagePanel = new JPanel();
        lbl = new JLabel[LABEL.length];
        for(int i = 0; i < LABEL.length; i++) lbl[i] = new JLabel(LABEL[i]);
        line = new JPanel();
        btn = new JButton[BUTTON.length];
        for(int i = 0; i < BUTTON.length; i++) btn[i] = new JButton(BUTTON[i]);
        lblCurDate = new JLabel(DateCalculator.currentTimeDay());
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
        this.renderer.setHorizontalAlignment(SwingConstants.CENTER);

        setTableContext();

        this.tableLog.setFont(TEXT_FONT);
        this.tableLog.getTableHeader().setFont(TEXT_FONT_HEAD);
        this.tableLog.getTableHeader().setBackground(GuiConstant.LINE);
        this.tableLog.getTableHeader().setForeground(Color.white);

        for(int i = 0; i < BUTTON.length; i++){
            this.btn[i].setBackground(Color.WHITE);
        }

        this.lblCurDate.setFont(TEXT_TIME);
        data.setTime(lblCurDate.getText());
        setTableLog(dao.getArrayListData(data,2));
    }
    private void addComponent(){
        this.add(imagePanel);
        this.imagePanel.add(lbl[0]);
        for(int i = 1 ; i < lbl.length; i++)
            this.add(lbl[i]);
        this.add(scroll);
        this.add(line);
        for(int i = 0; i < BUTTON.length; i++)
            this.add(btn[i]);
        this.add(lblCurDate);
    }
    private void setComponentPos(){
        this.imagePanel.setBounds(30, 203, 210,210);
        for(int i = 1; i < lbl.length; i++) this.lbl[i].setBounds(30,420 + ((i - 1) * 40),210,40);
        this.line.setBounds(0,60, 1033, 100);
        this.lblCurDate.setBounds(280, 165,750,30);
        this.scroll.setBounds(250, 203,750,503);
        for(int i = 0; i < BUTTON.length; i++) this.btn[i].setBounds(350 + (i * 130), 713, 50, 40);
    }
    private void scrollDown(){
        scrollBar = scroll.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }
    public void imageChange(Image studentImage, String studentId){
        this.studentImage = new ImageIcon(studentImage.getScaledInstance(180,200, Image.SCALE_SMOOTH));
        this.lbl[0].setIcon(this.studentImage);
        Student student = dao.getStudent(studentId);
        for(int i = 1; i < LABEL.length; i++){
            lbl[i].setText(LABEL[i]+" : " + student.getString(i));
        }
    }
    public void initInfo(){
        this.lbl[0].setIcon(null);
        for(int i = 1; i < LABEL.length; i++){
            lbl[i].setText(LABEL[i]+" : ");
        }
    }

    public void insertTableData(Data info, boolean mode){
        Object data[] = new Object[CALNAME.length];
        for(int i = 0; i < CALNAME.length; i++)
            data[i] = info.getInfo(i);
        this.model.addRow(data);
        if(mode){
            this.scrollDown();
            this.tableLog.getColumnModel().getColumn(this.tableLog.getColumnModel().getColumnCount()-1).setCellRenderer(renderer);
        }

    }
    public void setTableLog(ArrayList<Data> logs){
        Object data[] = new Object[CALNAME.length];
        for (Data log: logs) insertTableData(log, false);
        this.scrollDown();
        setTableContext();

    }
    private void setTableContext(){
        for(int i = 0; i < this.tableLog.getColumnModel().getColumnCount(); i++) this.tableLog.getColumnModel().getColumn(i).setCellRenderer(renderer);
        for(int i = 0 ; i < CALNAME.length; i++){
            this.tableLog.getColumn(CALNAME[i]).setPreferredWidth(CAL_SIZE[i]);
        }
    }
    public void setBtnActionListener(LogBtnListener logBtnListener, MouseListener mouseListener){
        for(int i = 0; i < BUTTON.length; i++) this.btn[i].addActionListener(logBtnListener);
        this.tableLog.addMouseListener(mouseListener);
    }
    public void removeTableItem(){
        model = new DefaultTableModel(CALNAME, 0){
            public boolean isCellEditable(int rowIndex, int mCollndex){
                return false;
            }
        };
        tableLog.setModel(model);
    }
    public void showLogTable(JButton button){
        for(int i = 0; i < BUTTON.length; i++){
            if(button.getText().equals(BUTTON[i]))
            {
                removeTableItem();
                if(button.getText().equals(BUTTON[2])) lblCurDate.setText(DateCalculator.currentTimeDay());
                else lblCurDate.setText(DateCalculator.nextDate(this.lblCurDate.getText(), NEXT_TIME[i][0], NEXT_TIME[i][1], NEXT_TIME[i][2]));
                data.setTime(lblCurDate.getText());
                setTableLog(dao.getArrayListData(data,2));
                break;
            }
        }
    }
    public JTable getTableLog() {
        return tableLog;
    }

}
