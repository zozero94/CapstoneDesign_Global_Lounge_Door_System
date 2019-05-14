package control;
import control.action_handel.ButtonListener;
import control.action_handel.MouseListener;
import control.serverReaction.SystemServerSocket;
import model.DataAccessObject;
import model.dto.ExcelOutInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import view.ServerUI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ControlMain {

    private static ControlMain controlMain = null;
    private DataAccessObject systemDAO;
    private ServerUI systemUI;
    private SystemServerSocket systemServerSocket;
    private ArrayList<ExcelOutInfo> logs;

    private HSSFWorkbook workbook;
    private HSSFSheet sheet;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;

    public static final String INFO[] = {"학번","이름","성별","국가","학과","단대","출입시간"};

    public static synchronized ControlMain getInstance(){
        if(controlMain == null) controlMain = new ControlMain();
        return controlMain;
    }
    private ControlMain() {
        this.systemDAO = DataAccessObject.getInstance();
        this.systemUI = new ServerUI();
        this.buttonListener = new ButtonListener();
        this.mouseListener = new MouseListener();
        this.systemServerSocket = SystemServerSocket.getInstance();
        setActionListener();
        systemServerSocket.start();
    }

    private void setActionListener(){
        this.systemUI.addActionListener(mouseListener,buttonListener);
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

        Font font = workbook.createFont();
        font.setFontName("나눔고딕");
        font.setFontHeight((short)(14*20));

        cellStyle.setFont(font);

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;

        logs = this.systemDAO.getExcelLogData();
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
//        excelSaveFile("C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\140.xls");
    // this.systemDAO.insertStudentImage("C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\14011038.jpg","14011038");

}
