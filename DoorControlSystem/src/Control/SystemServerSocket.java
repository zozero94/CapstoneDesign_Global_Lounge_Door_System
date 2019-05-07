package control;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.socket.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SystemServerSocket {

    private static SystemServerSocket systemServerSocket = null;
    private ServerSocket serverSocket;
    private Socket socket;
    private Logger logger;
    private ArrayList<Aplication> clients;
    private SocketThread newSocket;
    private JsonObject object;
    private JsonParser parser;
    private BufferedReader inMsg;
    private String msg;

    public synchronized static SystemServerSocket getInstance(){
        if(systemServerSocket == null) systemServerSocket = new SystemServerSocket();
        return systemServerSocket;
    }
    private SystemServerSocket(){
        clients = new ArrayList<Aplication>();
        logger = Logger.getLogger(this.getClass().getName());
    }
    public void start(){
        parser = new JsonParser();
        try{
            serverSocket = new ServerSocket(5050);
            logger.info("Server Start");
            while(true){
                socket = serverSocket.accept();
                logger.info("Client access");
                inMsg = new BufferedReader(new InputStreamReader((socket.getInputStream())));
                msg = inMsg.readLine();
                object = (JsonObject) parser.parse(msg);
                newSocket = SocketFactory.getSocket(object, socket, inMsg);
                if(newSocket != null)   newSocket.start();
            }
        }catch (Exception e){
            logger.info("SystemServer Exception start()");
            e.printStackTrace();
        }
    }

    public boolean compareQrString(String qrString){
        boolean qrExistFlag = false;
        for(int i = 0 ; i < clients.size(); i++){
            if(clients.get(i).compareQrString(qrString))
            {
                qrExistFlag = true;
                break;
            }
        }
        return qrExistFlag;
    }
    public void addClient(Aplication client){
        this.clients.add(client);
    }
    public void removeClient(Aplication client){
        this.clients.remove(client);
    }
    public void removeClient(String studentID){
        for(int i = 0 ; i < clients.size(); i++){
            if(clients.get(i).compareStudentId(studentID)) {
                clients.remove(clients.get(i));
                break;
            }
        }
    }

}
