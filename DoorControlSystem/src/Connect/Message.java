package connect;

public class Message {

    private int type;
    private String studentID;
    private String administerID;
    private String password;
    private String encodedMsg;
    private String publicKey;


    public Message(int type, String encodedMsg) {
        this.type = type;
        this.encodedMsg = encodedMsg;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getAdministerID() {
        return administerID;
    }

    public void setAdministerID(String administerID) {
        this.administerID = administerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncodedMsg() {
        return encodedMsg;
    }

    public void setEncodedMsg(String encodedMsg) {
        this.encodedMsg = encodedMsg;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
