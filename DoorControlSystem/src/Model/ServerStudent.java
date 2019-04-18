package model;

import java.io.Serializable;

public class ServerStudent extends Student  implements Serializable{

    private boolean loginFlag;
    public ServerStudent() {
        super();
    }

    public ServerStudent(String studentID, String name, String gender, String nationality, String department, String college) {
        super(studentID, name, gender, nationality, department, college);
    }

    public ServerStudent(String studentID, String name, String gender, String nationality, String department, String college, boolean loginFlag) {
        super(studentID, name, gender, nationality, department, college);
        this.loginFlag = loginFlag;
    }

    @Override
    public String getStudentID() {
        return super.getStudentID();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getGender() {
        return super.getGender();
    }

    @Override
    public String getNationality() {
        return super.getNationality();
    }

    @Override
    public String getDepartment() {
        return super.getDepartment();
    }
    @Override
    public String getCollege() {
        return super.getCollege();
    }
    @Override
    public void setStudentID(String studentID) {
        super.setStudentID(studentID);
    }
    @Override
    public void setName(String name) {
        super.setName(name);
    }
    @Override
    public void setGender(String gender) {
        super.setGender(gender);
    }
    @Override
    public void setNationality(String nationality) {
        super.setNationality(nationality);
    }

    @Override
    public byte[] getImages() {
        return super.getImages();
    }
    @Override
    public void setImages(byte[] images) {
        super.setImages(images);
    }

    @Override
    public void setDepartment(String department) {
        super.setDepartment(department);
    }
    @Override
    public void setCollege(String college) {
        super.setCollege(college);
    }
    @Override
    public String toString() {
        return super.toString();
    }

    public boolean isLoginFlag() {
        return this.loginFlag;
    }
    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }
}
