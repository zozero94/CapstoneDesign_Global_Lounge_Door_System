package model;

import model.dto.ExcelOutInfo;
import model.dto.ServerStudent;
import model.dto.StudentAccessInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

public class DataAccessObject {

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public DataAccessObject() {
        conn = null;
        pstmt = null;
        rs = null;
        connectDB();
    }
    private void connectDB(){
        try{
            conn = DriverManager.getConnection(DBConstants.JDBC_URL, DBConstants.JDBC_ID, DBConstants.JDBC_PASSWORD);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void closeDB() {		//데이터베이스에 연결하고 모두 정보를 주고받은 후에 연결을 끊기 위함

        try {
            if(pstmt != null)
                pstmt.close();
            if(rs != null)
                rs.close();
            if(conn != null)
                conn.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public ServerStudent getStudentInfo(String studentID){
        String sql = "select * from studentinfo where studentid = ?";
        ServerStudent student = null;
        try{

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentID);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                student = new ServerStudent(rs.getString("studentid"), rs.getString("name"),rs.getString("gender"),
                        rs.getString("nationality"),rs.getString("department"),rs.getString("college"),rs.getBoolean("loginflag"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return  student;
    }
    public  boolean setLoginFlag(String studentID, boolean loginFlag){
        String sql = "update studentinfo set loginflag = ? where studentid = ?";
        int result = 0;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, loginFlag);
            pstmt.setString(2, studentID);
            result = pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return result == 1;
    }
    public  boolean setUrl(String studentID, String url){
        String sql = "update urlimage set url = ? where id = ?";
        int result = 0;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, url);
            pstmt.setString(2, studentID);
            result = pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return  result == 1 ? true : false;
    }
    public  void setAllLoginFlag(){
        String sql = "update studentinfo set loginflag = false" ;
        try{
            connectDB();
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            closeDB();
        }
    }

    public void insertStudentImage(String fileName, String studentId){
        String sql = "insert into studentimage (image, id) values (?,?)";
        try{
            File file = new File(fileName);
            int fileLength = (int)file.length();
            InputStream image = new FileInputStream(file);

            pstmt = conn.prepareStatement(sql);
            pstmt.setBinaryStream(1, image, fileLength);
            pstmt.setString(2, studentId);
            System.out.println(pstmt.executeUpdate());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Image getStudentImage(String studentId){
        String sql = "select image from studentimage where id = ?";
        Image image = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                InputStream in = rs.getBinaryStream("image");
                image = ImageIO.read(in);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return image;
        }
    }
    public String getStudentImageUrl(String studentId){
        String sql = "select url from urlimage where id = ?";
        String url = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();
            if(rs.next())
                url = rs.getString("url");

        }catch (Exception e){
            e.printStackTrace();
        }
        return url;

    }
    public byte[] getStudentImageByte(String studentId){
        String sql = "select image from studentimage where id = ?";
        byte[] image = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                InputStream in = rs.getBinaryStream("image");
                int len;
                byte[] buff = new byte[131072];
                while((len = in.read(buff)) > 0){
                    image = new byte[len];
                    System.arraycopy(buff,0, image, 0, len);
                 }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return image;
        }
    }
    public void insertStudentLog(StudentAccessInfo studentAccessInfo){
        String sql = "insert into log (studentid, time) values (?,?)";
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentAccessInfo.getStudentID());
            pstmt.setString(2, studentAccessInfo.getTime());
            pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<ExcelOutInfo> getExcelLogData(){
        String sql = " select log.studentid, name, gender, nationality, department, college, time from log join studentinfo on studentinfo.studentid = log.studentid;";
        ArrayList<ExcelOutInfo> logs = null;
        try{
            logs = new ArrayList<ExcelOutInfo>();

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while(rs.next()){
                logs.add(new ExcelOutInfo(rs.getString("log.studentid"),rs.getString("name"),rs.getString("gender"),rs.getString("nationality"),
                        rs.getString("department"),rs.getString("college"),rs.getString("time")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return logs;
        }

    }

}