package view;

import control.action_handel.ButtonListener;
import control.action_handel.MouseListener;
import model.DataAccessObject;
import model.dto.ExcelOutInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import view.right.GuiConstant;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BtnPanel extends JPanel {

    private static BtnPanel btnPanel = null;
    private JLabel lblImage;
    private ImageIcon mainImage;
    private JButton btn[];
    private ImageIcon icon[];
    private JButton save;

    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter;
    private String path;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;

    private DataAccessObject dao;
    private ArrayList<ExcelOutInfo> logs;

    private Font font = new Font("HY엽서M", Font.BOLD, 30);
    public static final String BUTTON_NAME[] = {"로그             ", "차트             " ,"데이터저장     ", "문열기           "};
    public static final String INFO[] = {"학번","이름","성별","국가","학과","단대","출입시간"};
    public static final String BUTTON_IMAGE_PATH[] = {"../resource/log.png", "../resource/chart.png" ,"../resource/excel.png", "../resource/open.png"};


    public synchronized static BtnPanel getInstance(){
        if(btnPanel == null) btnPanel = new BtnPanel();
        return btnPanel;
    }
    private BtnPanel(){
        super();
        setComponentUi();
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
        this.mainImage = new ImageIcon(getClass().getResource("../resource/main.png"));
        this.fileChooser = new JFileChooser();
        this.filter = new FileNameExtensionFilter("excel[xls형식만지원]", "xls");
        this.save = new JButton("Save to Exel");
        this.dao = new DataAccessObject();
    }
    private void setComponentPos(){
        lblImage.setBounds(50,0,333,153);
        for(int i = 0; i < 4; i++) btn[i].setBounds(0, (203 + i * 80), 333,80);
    }
    private void addComponent(){
        for(int i = 0 ; i < 4; i++) this.add(btn[i]);
        this.add(lblImage);
    }

    public JLabel getLblImage() {
        return lblImage;
    }
    public void setLblImage(JLabel lblImage) {
        this.lblImage = lblImage;
    }
    public JButton[] getBtn() {
        return btn;
    }
    public JButton getBtn(int i) {
        return i < btn.length ? btn[i] : null;
    }
    public void setBtn(JButton[] btn) {
        this.btn = btn;
    }

    public void setBtnMouseListener(MouseListener mouseListener){
        for(int i = 0 ; i < btn.length; i++)
            btn[i].addMouseListener(mouseListener);
    }
    public void setBtnActionListener(ButtonListener btnListener){
        for(int i = 0 ; i < btn.length; i++)
            btn[i].addActionListener(btnListener);
    }
    public void turnButtonColor(JButton button) {
        if (btn[0] == button) {
            btn[0].setForeground(GuiConstant.TEXT);
            btn[1].setForeground(Color.WHITE);
        }
        else if(btn[1] == button) {
            btn[0].setForeground(Color.WHITE);
            btn[1].setForeground(GuiConstant.TEXT);
        }
    }
    public void saveExcel(){
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Choose a directory to dave your file");
        int ret = fileChooser.showOpenDialog(this.save);
        if(ret == JFileChooser.APPROVE_OPTION){
            // TODO
            // 파일명 예외처리
            path = fileChooser.getSelectedFile().getPath();
            if(!path.substring(path.length()-4, path.length()).equals(".xls")) path += ".xls";
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

        logs = dao.getExcelLogData();
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
}
