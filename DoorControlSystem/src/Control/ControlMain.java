package control;
import model.DataAccessObject;
import model.StudentAccessInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import view.ServerUI;

import java.io.FileOutputStream;
import java.io.IOException;

public class ControlMain {

    private DataAccessObject systemDAO;
    private ServerUI systemUI;
    private SystemServerSocket systemServerSocket;

    public ControlMain() {

        //excelSaveFile("C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\140.xls");
        this.systemDAO = DataAccessObject.getInstance();
        this.systemUI = new ServerUI();
        this.systemServerSocket = SystemServerSocket.getInstance();
        systemServerSocket.start();


    }

    public void excelSaveFile(String filePath){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("log");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        //this.systemDAO

        try{
            FileOutputStream fis = new FileOutputStream(filePath);
            workbook.write(fis);
            fis.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    // this.systemDAO.insertStudentImage("C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\14011038.jpg","14011038");

}
