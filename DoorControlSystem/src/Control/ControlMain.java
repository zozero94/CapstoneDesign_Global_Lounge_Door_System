package control;

import control.action_handel.ButtonListener;
import control.action_handel.LogBtnListener;
import control.action_handel.LogChartBtnListener;
import control.action_handel.MouseListener;
import control.socket.SystemServerSocket;
import view.UI.ServerUI;

public class ControlMain {

    private static ControlMain controlMain = null;

    private ServerUI systemUI;
    private SystemServerSocket systemServerSocket;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;
    private LogBtnListener logBtnListener;
    private LogChartBtnListener logChartBtnListener;

    public static synchronized ControlMain getInstance(){
        if(controlMain == null) controlMain = new ControlMain();
        return controlMain;
    }
    private ControlMain() {
        this.systemUI = new ServerUI();
        this.buttonListener = new ButtonListener();
        this.mouseListener = new MouseListener();
        this.logBtnListener = new LogBtnListener();
        this.logChartBtnListener = new LogChartBtnListener();
        this.systemServerSocket = SystemServerSocket.getInstance();
        setActionListener();
        systemServerSocket.start();
    }

    private void setActionListener(){
        this.systemUI.addActionListener(mouseListener,buttonListener, logBtnListener, logChartBtnListener);
    }

}
