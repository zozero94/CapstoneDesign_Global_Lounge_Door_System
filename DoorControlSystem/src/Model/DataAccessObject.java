package model;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.sql.*;

public class DataAccessObject {

    private static DataAccessObject dataAccessObject = null;
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public synchronized static DataAccessObject getInstance(){
        if(dataAccessObject == null) dataAccessObject = new DataAccessObject();
        return dataAccessObject;
    }
    private DataAccessObject() {
        conn = null;
        pstmt = null;
        rs = null;
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
        int result = 0;
        ServerStudent student = null;
        try{
            connectDB();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentID);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                student = new ServerStudent(rs.getString("studentid"), rs.getString("name"),rs.getString("gender"),rs.getString("nationality"),rs.getString("department"),rs.getString("college"),rs.getBoolean("loginflag"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            closeDB();
        }
        return  student;
    }
    public boolean setLoginFlag(String studentID, boolean loginFlag){
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
        }finally {
            closeDB();
        }
        return  result == 1 ? true : false;
    }
    public void setAlltLoginFlag(){
        String sql = "update studentinfo set loginflag = false" ;
        int result = 0;
        try{
            connectDB();
            pstmt = conn.prepareStatement(sql);
            result = pstmt.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            closeDB();
        }
    }
    public void insertStudentImage(String fileName, String studentId){
        String sql = "insert into studentimage (image, id) values (?,?)";
        try{
            connectDB();
            File file = new File(fileName);
            int fileLength = (int)file.length();
            InputStream image = new FileInputStream(file);

            pstmt = conn.prepareStatement(sql);
            pstmt.setBinaryStream(1, image, fileLength);
            pstmt.setString(2, studentId);
            System.out.println(pstmt.executeUpdate());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeDB();
        }
    }
    public Image getStudentImage(String studentId){
        String sql = "select image from studentimage where id = ?";
        Image image = null;
        try{
            connectDB();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                InputStream in = rs.getBinaryStream("image");
                image = ImageIO.read(in);

//                JFrame frame = new JFrame();
//                JLabel label = new JLabel(new ImageIcon(im));
//                frame.getContentPane().add(label, BorderLayout.CENTER);
//                frame.pack();
//                frame.setVisible(true);
//                //이미지 출력방법


//                String path = "C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\14011038_copy.jpg";
//                FileOutputStream fos = new FileOutputStream(path);
//                byte[] buff = new byte[8192];
//                int len;
//                while((len = in.read(buff)) > 0){
//                    fos.write(buff, 0 , len);
//                 }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeDB();
            return image;
        }
    }
    public byte[] getStudentImageByte(String studentId){
        String sql = "select image from studentimage where id = ?";
        byte[] image = null;
        try{
            connectDB();
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
//                JFrame frame = new JFrame();
//                JLabel label = new JLabel(new ImageIcon(im));
//                frame.getContentPane().add(label, BorderLayout.CENTER);
//                frame.pack();
//                frame.setVisible(true);
//                //이미지 출력방법


//                String path = "C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\14011038_copy.jpg";
//                FileOutputStream fos = new FileOutputStream(path);
//                byte[] buff = new byte[8192];
//                int len;
//                while((len = in.read(buff)) > 0){
//                    fos.write(buff, 0 , len);
//                 }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeDB();
            return image;
        }
    }
}
