package Model;
import Constant.Constants;
import java.sql.*;

public class DataAccessObject {
    public Connection conn;
    public PreparedStatement pstmt;
    public ResultSet rs;

    public DataAccessObject() {
        conn = null;
        pstmt = null;
        rs = null;
        connectDB();
        closeDB();
    }


    private void connectDB(){
        try{
            conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.JDBC_ID, Constants.JDBC_PASSWORD);
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

}
