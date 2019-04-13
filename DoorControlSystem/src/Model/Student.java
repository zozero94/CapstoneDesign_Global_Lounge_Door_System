package Model;

public class Student {


    private String studentID;
    private String name;
    private String gender;
    private String nationality;
    private String department;
    private String college;

    public Student() {
        this.studentID = null;
        this.name = null;
        this.gender = null;
        this.nationality = null;
        this.department = null;
    }

    public Student(String studentID, String name, String gender, String nationality, String department, String college) {
        this.studentID = studentID;
        this.name = name;
        this.gender = gender;
        this.nationality = nationality;
        this.department = department;
        this.college = college;
    }

    public String getStudentID() {
        return studentID;
    }
    public String getName() {
        return name;
    }
    public String getGender() {
        return gender;
    }
    public String getNationality() {
        return nationality;
    }
    public String getDepartment() {
        return department;
    }
    public String getCollege() {
        return college;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setCollege(String college) {
        this.college = college;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentID='" + studentID + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", department='" + department + '\'' +
                ", college='" + college + '\'' +
                '}';
    }

}
