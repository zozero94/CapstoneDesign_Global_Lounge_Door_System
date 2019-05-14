package control.serverReaction.aplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import control.encryption.RsaManager;
import model.DataAccessObject;
import model.dto.Student;

public class Login implements StateAP {
    private JsonObject objectReturn;
    private JsonObject data;
    private ServerContextAP serverContext;
    private JsonParser parser;
    private String strData;

    // private RsaManager rsaManager;

    public Login(ServerContextAP serverContext){
        parser = new JsonParser();
        this.serverContext = serverContext;
        //rsaManager;
    }
    @Override
    public JsonObject reaction(JsonObject object){
        this.objectReturn = new JsonObject();
        strData = object.get("data").getAsString();
        data = (JsonObject)parser.parse(strData);
        serverContext.setInfo(DataAccessObject.getInstance().getStudentInfo(data.get("id").getAsString()));

        if(serverContext.getInfo() != null) {
            if(object.get("seqType").getAsInt() == 105){
                DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), true);
                objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_OK);
                objectReturn.addProperty("data", RsaManager.getInstance().getEncodedString(new Student(serverContext.getInfo()),RsaManager.getInstance().getStringPublicKey( data.get("modulus").getAsString(),data.get("exponent").getAsString() )));
                serverContext.setState(new Qr(serverContext));
            }
            else if (serverContext.getInfo().isLoginFlag() == true)  objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_ALREADY);
            else{
                DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), true);
                objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_OK);
                objectReturn.addProperty("data", RsaManager.getInstance().getEncodedString(new Student(serverContext.getInfo()), data.get("key").getAsString()));
                serverContext.setState(new Qr(serverContext));
            }
        }
        else    objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_NO_DATA);

        return objectReturn;
    }
}
