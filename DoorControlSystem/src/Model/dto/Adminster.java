package model.dto;

public class Adminster {
    private String adminsterID;
    private String password;

    public Adminster() {
        this.adminsterID = null;
        this.password = null;
    }

    public Adminster(String adminsterID, String password) {
        this.adminsterID = adminsterID;
        this.password = password;
    }

    public String getAdminsterID() {
        return adminsterID;
    }
    public void setAdminsterID(String adminsterID) {
        this.adminsterID = adminsterID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
