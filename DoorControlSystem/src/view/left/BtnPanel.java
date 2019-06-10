package view.left;

import control.action_handel.ButtonListener;
import control.db.Data;
import control.db.DataAccessObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import view.GuiConstant;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BtnPanel extends JPanel {
    private static BtnPanel btnPanel = null;
    private JLabel lblImage;
    private ImageIcon mainImage;
    private JButton btn[];
    private ImageIcon icon[];
    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter;
    private String path;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private DataAccessObject dao;
    private ArrayList<Data> logs;
    private int modeBtn;
    private Font font = new Font("HY엽서M", Font.BOLD, 30);
    public static final String BUTTON_NAME[] = {"로그             ", "차트             " ,"데이터저장     ", "문열기           "};
    public static final String INFO[] = {"학번","이름","성별","국가","학과","단대","출입시간"};
    public static final String BUTTON_IMAGE_PATH[] = {"../../resource/log.png", "../../resource/chart.png" ,"../../resource/excel.png", "../../resource/open.png","../../resource/title.png"};
    public synchronized static BtnPanel getInstance(){
        if(btnPanel == null) btnPanel = new BtnPanel();
        return btnPanel;
    }
    private BtnPanel(){
        super();
        setComponentUi();
        modeBtn = 0;
    }
    private void setComponentUi(){

        createComponent();
        this.setPreferredSize(new Dimension(333, 768));
        this.setBackground(GuiConstant.BACK_COLOR);
        this.setLayout(null);
        for(int i = 0 ; i < BUTTON_NAME.length; i++) {
            this.btn[i].setBorderPainted(false);
            this.btn[i].setBackground(GuiConstant.BACK_COLOR);
            this.btn[i].setForeground(i == 0 ? GuiConstant.TEXT : Color.white);
            this.btn[i].setFont(font);
            this.btn[i].setFocusPainted(false);
            this.btn[i].setFocusTraversalPolicy(null);
            this.btn[i].setHorizontalTextPosition(SwingConstants.LEFT);
        }
        this.lblImage.setIcon(mainImage);

        setComponentPos();
        addComponent();
    }
    private void createComponent(){
        this.lblImage = new JLabel();
        this.btn = new JButton[4];
        this.icon = new ImageIcon[4];

        for(int i = 0 ; i < BUTTON_NAME.length; i++){
            this.icon[i] = new ImageIcon(getClass().getResource(BUTTON_IMAGE_PATH[i]));
            this.btn[i] = new JButton(BUTTON_NAME[i],icon[i]);
        }
        this.mainImage = new ImageIcon(getClass().getResource(BUTTON_IMAGE_PATH[4]));
        this.fileChooser = new JFileChooser();
        this.filter = new FileNameExtensionFilter("excel[xls형식만지원]", "xls");
        this.dao = new DataAccessObject();
    }
    private void setComponentPos(){
        lblImage.setBounds(5,5,333,200);
        for(int i = 0; i < 4; i++) btn[i].setBounds(0, (203 + i * 80), 333,80);
    }
    private void addComponent(){
        for(int i = 0 ; i < 4; i++) this.add(btn[i]);
        this.add(lblImage);
    }
    public JButton getBtn(int i) {
        return i < btn.length ? btn[i] : null;
    }
    public void setBtnActionListener(ButtonListener btnListener){
        for(int i = 0 ; i < btn.length; i++) {
            btn[i].addActionListener(btnListener);
            btn[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {


                }
                @Override
                public void mousePressed(MouseEvent e) {

                }
                @Override
                public void mouseReleased(MouseEvent e) {

                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    JButton btn = (JButton) e.getSource();
                    btn.setForeground(Color.gray);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    JButton btn = (JButton) e.getSource();
                    btn.setForeground(Color.white);
                    BtnPanel.getInstance().getBtn(BtnPanel.getInstance().getModeBtn()).setForeground(GuiConstant.TEXT);
                }
            });
        }
    }
    public void turnButtonColor(JButton button) {
        if (btn[0] == button) {
            btn[0].setForeground(GuiConstant.TEXT);
            btn[1].setForeground(Color.WHITE);
            modeBtn = 0;
        }
        else if(btn[1] == button) {
            btn[0].setForeground(Color.WHITE);
            btn[1].setForeground(GuiConstant.TEXT);
            modeBtn = 1;
        }
    }
    public void saveExcel(){
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Choose a directory to dave your file");
        fileChooser.setApproveButtonText("저장");
        fileChooser.setApproveButtonToolTipText("Excel file로 저장");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            fileChooser.updateUI();

        }catch (Exception e){
            e.printStackTrace();
        }
        int ret = fileChooser.showOpenDialog(null);

        if(ret == JFileChooser.APPROVE_OPTION){
            path = fileChooser.getSelectedFile().getPath();
            if(!path.substring(path.length()-4).equals(".xls")) path += ".xls";
            excelSaveFile(path);
        }else{
            JOptionPane.showMessageDialog(null,"데이터 저장 취소","경고",JOptionPane.WARNING_MESSAGE);
        }
    }
    public void excelSaveFile(String filePath) {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("log");
        sheet.setColumnWidth(0, 17<<8);
        sheet.setColumnWidth(1, 20<<8);
        sheet.setColumnWidth(2, 9<<8);
        sheet.setColumnWidth(3, 20<<8);
        sheet.setColumnWidth(4, 24<<8);
        sheet.setColumnWidth(5, 40<<8);
        sheet.setColumnWidth(6, 32<<8);
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontName("나눔고딕");
        font.setFontHeight((short)(14*20));

        cellStyle.setFont(font);

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;

        logs = dao.getArrayListData(null, 1);
        for(int r = 0; r < INFO.length; r++){
            cell = row.createCell(r);
            cell.setCellValue(INFO[r]);
            cell.setCellStyle(cellStyle);
        }
        for(int r = 0; r < logs.size(); r++){
            row = sheet.createRow(r + 1);
            for(int i = 0 ; i < 7; i ++) {
                cell = row.createCell(i);
                cell.setCellValue(logs.get(r).getInfo(i));
                cell.setCellStyle(cellStyle);
            }
        }
        try{
            FileOutputStream fis = new FileOutputStream(filePath);
            workbook.write(fis);
            fis.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public int getModeBtn(){return this.modeBtn;}
}
