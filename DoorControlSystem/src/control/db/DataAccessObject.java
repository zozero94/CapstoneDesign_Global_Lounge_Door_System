package control.db;

import view.DateCalculator;

import java.sql.*;
import java.util.ArrayList;

public class DataAccessObject {

    public static int admin_count = 0;
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
    private void setPreparedStatement(Data d, int mode) throws Exception{
        switch (mode){
            case 2 : {
                pstmt.setString(1, d.getTime());
                pstmt.setString(2, DateCalculator.nextDate(d.getTime(), 0,0,1));
                break;
            }
            case 3 : {
                pstmt.setString(1,d.getTime().substring(0,7));
                pstmt.setString(2,DateCalculator.nextDate(d.getTime(),0,1,0).substring(0,7));
                break;
            }
            case 6 : {
                pstmt.setString(1, d.getTime().substring(0,10));
                pstmt.setString(2, DateCalculator.nextDate(d.getTime(),0,0,1).substring(0,10));
            }
            default: break;
        }
    }
    private void addLog(ArrayList<Data> logs, int mode) throws  Exception{
        while(rs.next()){
            switch (mode){
                case 1 : case 2 :
                    logs.add(new Data(rs.getString("log.studentid"),rs.getString("name"),rs.getString("gender"),rs.getString("nationality"),
                            rs.getString("department"),rs.getString("college"),rs.getString("time")));
                    break;
                case 3 : case 5 :
                    logs.add(new Data("","",rs.getString("gender"),"","","",
                            rs.getString("m"), rs.getInt("total")));
                    break;
                case 4 :
                    logs.add(new Data("","",rs.getString("gender"),rs.getString("nationality"),
                            "","","" ,rs.getInt("count(*)")));
                    break;
                default :
                    break;
            }
        }
    }
    public Data getStudent(String studentId){
        Data student = null;
        if(studentId.equals("admin")){
            student = new Data("admin"+admin_count++,"관리자","","","","",false);
        }else {
            try {
                connectDB();
                pstmt = conn.prepareStatement(DBConstants.SELECT_STUDENTINFO);
                pstmt.setString(1, studentId);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    student = new Data(rs.getString("studentid"), rs.getString("name"), rs.getString("gender"),
                            rs.getString("nationality"), rs.getString("department"), rs.getString("college"), rs.getBoolean("loginflag"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            closeDB();
        }
        return  student;

    }
    public ArrayList<Data> getArrayListData(Data d, int mode){

        ArrayList<Data> logs = null;
        try{
            connectDB();
            logs = new ArrayList<Data>();
            pstmt = conn.prepareStatement(DBConstants.SELECT_LOG[mode]);
            setPreparedStatement(d, mode);
            rs = pstmt.executeQuery();
            addLog(logs,mode);
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDB();
        return logs;
    }

    public  boolean setLoginFlag(String studentID, boolean loginFlag){
        String sql = "update studentinfo set loginflag = ? where studentid = ?";
        int result = 0;
        try{
            connectDB();
            pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, loginFlag);
            pstmt.setString(2, studentID);
            result = pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }
        closeDB();
        return result == 1;
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
    public void insertStudentLog(Data student){
        String sql = "insert into log (studentid, time) values (?,?)";
        try{
            connectDB();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getStudentID());
            pstmt.setString(2, student.getTime());
            pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        closeDB();
    }
}
//    public void insertStudentImage(String fileName, String studentId){
//        String sql = "insert into studentimage (image, id) values (?,?)";
//        try{
//            File file = new File(fileName);
//            int fileLength = (int)file.length();
//            InputStream image = new FileInputStream(file);
//
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setBinaryStream(1, image, fileLength);
//            pstmt.setString(2, studentId);
//            System.out.println(pstmt.executeUpdate());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//    public Image getStudentImage(String studentId){
//        String sql = "select image from studentimage where id = ?";
//        Image image = null;
//        try{
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, studentId);
//            rs = pstmt.executeQuery();
//            if(rs.next()){
//                InputStream in = rs.getBinaryStream("image");
//                image = ImageIO.read(in);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            return image;
//        }
//    }
//    public byte[] getStudentImageByte(String studentId){
//        String sql = "select image from studentimage where id = ?";
//        byte[] image = null;
//        try{
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, studentId);
//            rs = pstmt.executeQuery();
//            if(rs.next()){
//                InputStream in = rs.getBinaryStream("image");
//                int len;
//                byte[] buff = new byte[131072];
//                while((len = in.read(buff)) > 0){
//                    image = new byte[len];
//                    System.arraycopy(buff,0, image, 0, len);
//                 }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            return image;
//        }
//    }
//select log.studentid, gender, time from log join studentinfo on studentinfo.studentid = log.studentid where log.time > '2019-05-03' && log.time < '2019-05-04';
// select log.studentid,MID(time,6,5) m,gender, count(*) from log join studentinfo on studentinfo.studentid = log.studentid && time >= '2019-05' && time < '2019-06' group by m,gender; 일별
//select b.studentid, b.t, gender, count(*) count from studentinfo right join (select distinct MID(time,6,5) t, studentid from log) b on studentinfo.studentid = b.studentid group by b.t, gender; 일별 중복데이터 제거
//distinct 중복제거

//select log.studentid,MID(time,1,7),gender, count(*) from log join studentinfo on studentinfo.studentid = log.studentid group by MID(time,1,7),gender; 월별 조회
//select log.studentid,MID(time,12,2) m,gender, count(*) from log join studentinfo on studentinfo.studentid = log.studentid group by m; 시간별 조회
//select count(*), nationality from log join studentinfo on studentinfo.studentid = log.studentid group by nationality;