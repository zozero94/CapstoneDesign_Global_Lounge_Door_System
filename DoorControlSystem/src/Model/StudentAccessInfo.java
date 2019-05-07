package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentAccessInfo {
    private String studentID;
    private SimpleDateFormat dayTime;
    private String time;

    public StudentAccessInfo() {
        this.studentID = null;
        this.dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public StudentAccessInfo(String studentID) {
        this.studentID = studentID;
        this.dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dayTime.format(new Date(System.currentTimeMillis()));
    }

    public StudentAccessInfo(String studentID, SimpleDateFormat dayTime) {
        this.studentID = studentID;
        this.dayTime = dayTime;
    }

    public String getStudentID() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public String getTime() {
        return time;
    }
    public void setDayTime(Date dayTime) {
        this.dayTime.format(dayTime);
    }
}
