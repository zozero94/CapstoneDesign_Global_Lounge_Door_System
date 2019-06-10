package control.db;

public class Data extends Student {
    private String time;
    private int count;
    private boolean loginFlag;
    public Data(){
        setNull();
    }
    public Data(String data, int mode){
        setNull();
        switch (mode){
            case 0: this.setStudentID(data);
            case 1: this.time = data;
        }
    }
    public Data(String studentID, String name, String gender, String nationality, String department, String college, String time){
        super(studentID, name, gender, nationality, department, college);
        this.time = time.substring(0, 19);
    }

    public Data(String studentID, String name, String gender, String nationality, String department, String college, boolean loginFlag){
        super(studentID, name, gender, nationality, department, college);
        this.loginFlag = loginFlag;
    }
    public Data(String studentID, String name, String gender, String nationality, String department, String college,String time, int count){
        super(studentID, name, gender, nationality, department, college);
        this.time = time;
        this.count = count;
    }


    public void setTime(String time){this.time = time;}
    public String getTime(){return time;}
    public int getCount(){return count;}
    public String getInfo(int i){
        switch (i){
            case 0: return this.getStudentID();
            case 1: return this.getName();
            case 2: return this.getGender();
            case 3: return this.getNationality();
            case 4: return this.getDepartment();
            case 5: return this.getCollege();
            default: return time;
        }
    }
    public boolean adminCheck(){
        return this.getStudentID().substring(0,5).equals("admin");
    }
    public boolean isLoginFlag() {
        return loginFlag;
    }
}
