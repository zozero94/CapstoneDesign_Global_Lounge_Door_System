package control;

import encryption.RsaManager;
import model.DataAccessObject;
import model.Student;
import view.ServerUI;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ControlMain {

    private DataAccessObject systemDAO;
    private ServerUI systemUI;
    private SystemServerSocket systemServerSocket;

    public ControlMain() {
        this.systemDAO = DataAccessObject.getInstance();
        this.systemUI = new ServerUI();
        this.systemServerSocket = SystemServerSocket.getInstance();
        systemServerSocket.start();
    }

}
