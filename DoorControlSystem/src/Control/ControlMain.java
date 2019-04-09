package Control;

import Model.DataAccessObject;
import Model.Student;
import View.ServerUI;

public class ControlMain {

    private DataAccessObject systemDAO;
    private ServerUI systemUI;

    public ControlMain() {
        this.systemDAO = new DataAccessObject();
        this.systemUI = new ServerUI();

        Student s = new Student("14011070", "민우","남자","한국","컴퓨터공학과","소프트웨어융합대학");
        System.out.println(s.toString());
    }
}
