package Control;

import Encryption.RSAencoded;
import Model.DataAccessObject;
import Model.Student;
import View.ServerUI;

public class ControlMain {

    private DataAccessObject systemDAO;
    private ServerUI systemUI;
    private SystemServerSocket systemServerSocket;
    private RSAencoded rsa;
    private Student student;
    public ControlMain() {
        this.systemDAO = new DataAccessObject();
        this.systemUI = new ServerUI();
        this.systemServerSocket = new SystemServerSocket();
        systemServerSocket.start();
    }


}
