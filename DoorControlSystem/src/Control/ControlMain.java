package control;

import control.action_handel.ButtonListener;
import control.action_handel.MouseListener;
import control.socket.SystemServerSocket;
import view.ServerUI;

public class ControlMain {

    private static ControlMain controlMain = null;

    private ServerUI systemUI;
    private SystemServerSocket systemServerSocket;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;

    public static synchronized ControlMain getInstance(){
        if(controlMain == null) controlMain = new ControlMain();
        return controlMain;
    }
    private ControlMain() {
        this.systemUI = new ServerUI();
        this.buttonListener = new ButtonListener();
        this.mouseListener = new MouseListener();
        this.systemServerSocket = SystemServerSocket.getInstance();
        setActionListener();
        systemServerSocket.start();
    }

    private void setActionListener(){
        this.systemUI.addActionListener(mouseListener,buttonListener);
    }

//        excelSaveFile("C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\140.xls");
    // this.systemDAO.insertStudentImage("C:\\Users\\kmw81\\IdeaProjects\\DoorControlSystem\\image\\14011038.jpg","14011038");

}
