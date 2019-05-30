package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.SeqTypeConstants;
import control.encryption.RsaManager;
import control.socket.SystemServerSocket;
import model.DataAccessObject;
import model.dto.Student;

public class Login implements StateAP {
    private JsonObject objectReturn;
    private JsonObject data;
    private ServerContextAP serverContext;
    private JsonParser parser;
    private String strData;

    private RsaManager rsaManager;
    private DataAccessObject dao;

    public Login(ServerContextAP serverContext){
        parser = new JsonParser();
        this.serverContext = serverContext;
        this.rsaManager = new RsaManager();
        this.dao = new DataAccessObject();
    }
    @Override
    public JsonObject reaction(JsonObject object){
        this.objectReturn = new JsonObject();
        strData = object.get("data").getAsString();
        data = (JsonObject)parser.parse(strData);
        serverContext.setInfo(dao.getStudent(data.get("id").getAsString()));
        if(serverContext.getInfo() != null) {
            if(serverContext.getInfo().isLoginFlag())  objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_ALREADY);
            else {
                if (object.get("seqType").getAsString().equals(SeqTypeConstants.LOGIN_IOS)) rsaManager.setStringPublicKey(data.get("modulus").getAsString(), data.get("exponent").getAsString());
                else rsaManager.setPublicKey(data.get("key").getAsString());
                objectReturn.addProperty("data", rsaManager.getEncodedString(new Student(serverContext.getInfo())));
                dao.setLoginFlag(serverContext.getInfo().getStudentID(), true);
                objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_OK);
                serverContext.setState(new Qr(serverContext));
            }
        }
        else{
            objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_NO_DATA);
            serverContext.getSocketThread().setAndroidLogoutFlag(false);
            SystemServerSocket.getInstance().removeClient(serverContext.getSocketThread());
        }

        return objectReturn;
    }
}
