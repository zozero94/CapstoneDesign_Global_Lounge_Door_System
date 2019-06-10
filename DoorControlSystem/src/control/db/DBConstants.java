package control.db;


public class DBConstants {
    //public final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public final static String JDBC_URL = "jdbc:mysql://localhost:3306/doorcontrolsystem?characterEncoding=UTF-8&&serverTimezone=UTC";
    public final static String JDBC_ID = "root";
    public final static String JDBC_PASSWORD = "kim_min828049";

    public final static String SELECT_STUDENTINFO = "select * from studentinfo where studentid = ?;"; // mode 0
    public final static String SELECT_LOG[] = {
            "",
            "select log.studentid, name, gender, nationality, department, college, time from log join studentinfo on studentinfo.studentid = log.studentid;", // mode 1
            "select log.studentid, name, gender, nationality, department, college, time from log join studentinfo on studentinfo.studentid = log.studentid where time >= ? && time < ?;", // mode 2
            "select m, gender, count(*) total from studentinfo right join (select distinct MID(time,6,5) m, studentid from log where time >= ? && time < ?) b on studentinfo.studentid = b.studentid group by b.m, gender;", // mode 3
            "select count(*), nationality,gender from log join studentinfo on studentinfo.studentid = log.studentid group by nationality, gender;", // mode 4
            "select MID(time,12,2) m, count(*) total ,gender from log join studentinfo on studentinfo.studentid = log.studentid group by m, gender;", // mode 5
            "select count(distinct studentid) total from log where time > ? && time < ?;" // mode 6
    };
//
//    public final static String UPDATE_LOGINFLAG = "update studentinfo set loginflag = ? where studentid = ?;";
//    public final static String UPDATE_LOGINFLAG_ALL = "update studentinfo set loginflag = false;";
//
//    public final static String INSERT_LOG = "insert into log (studentid, time) values (?,?);";
}
