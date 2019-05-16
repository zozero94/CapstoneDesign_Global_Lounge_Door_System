package model.dto;

public class ExcelOutInfo {

    private String studentID;
    private String name;
    private String gender;
    private String nationality;
    private String department;
    private String college;
    private String time;

    public ExcelOutInfo() {
        this.studentID = null;
        this.name = null;
        this.gender = null;
        this.nationality = null;
        this.department = null;
        this.college = null;
        this.time = null;
    }
    public ExcelOutInfo(String studentID, String name, String gender, String nationality, String department, String college, String time) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.nationality = nationality;
        this.department = department;
        this.college = college;
        this.time = time.substring(0, 19);
    }
    public ExcelOutInfo(Student student, String time) {
        this.studentID = student.getStudentID();
        this.name = student.getName();
        this.gender = student.getGender();
        this.nationality = student.getNationality();
        this.department = student.getDepartment();
        this.college = student.getCollege();
        this.time = time;
    }
    public String getInfo(int i){
        switch (i){
            case 0: return studentID;
            case 1: return name;
            case 2: return gender;
            case 3: return nationality;
            case 4: return department;
            case 5: return college;
            default: return time;
        }
    }
    public String getStudentID() {
        return studentID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getCollege() {
        return college;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
