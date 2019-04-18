package control;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class SystemServerSocket {

    private static SystemServerSocket systemServerSocket = null;

    private ServerSocket serverSocket;
    private Socket socket;

    private Logger logger;

    private ArrayList<SocketThread> client;
    private SocketThread newClient;

    public synchronized static SystemServerSocket getInstance(){
        if(systemServerSocket == null) systemServerSocket = new SystemServerSocket();
        return systemServerSocket;
    }
    private SystemServerSocket(){
        client = new ArrayList<SocketThread>();
        logger = Logger.getLogger(this.getClass().getName());
    }

    public void start(){
        try{
            serverSocket = new ServerSocket(5050);
            logger.info("Server Start");
            while(true){
                socket = serverSocket.accept();
                logger.info("Client access");
                newClient = new SocketThread(socket);
                newClient.start();
                client.add(newClient);

            }
        }catch (Exception e){
            logger.info("SystemServer Exception start()");
            e.printStackTrace();
        }
    }
    public boolean compareQrString(String qrString){
        boolean qrExistFlag = false;
        for(int i = 0 ; i < client.size(); i++){
            if(client.get(i).compareQrString(qrString))
            {
                qrExistFlag = true;
                break;
            }
        }
        return qrExistFlag;
    }
    public void removeClient(SocketThread client){
        this.client.remove(client);
    }
    public void removeClient(String studentID){
        for(int i = 0 ; i < client.size(); i++){
            if(client.get(i).compareStudentId(studentID)) {
                client.remove(client.get(i));
                break;
            }
        }
    }
}
