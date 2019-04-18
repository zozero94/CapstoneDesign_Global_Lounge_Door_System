package control.ServerReaction;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import encryption.RsaManager;
import model.DataAccessObject;
import model.Student;

public class Login implements  State{
    private JsonObject objectReturn;
    private JsonObject data;
    private ServerContext serverContext;
    private JsonParser parser;
    private String strData;

    public Login(ServerContext serverContext){
        parser = new JsonParser();
        this.serverContext = serverContext;
    }
    @Override
    public JsonObject reaction(JsonObject object){
        this.objectReturn = new JsonObject();
        RsaManager.getInstance().newKey();
        strData = object.get("data").getAsString();
        data = (JsonObject)parser.parse(strData);
        serverContext.setInfo(DataAccessObject.getInstance().getStudentInfo(data.get("id").getAsString()));
        serverContext.getInfo().setImages(DataAccessObject.getInstance().getStudentImageByte(data.get("id").getAsString()));
        if(serverContext.getInfo() != null) {
            if (serverContext.getInfo().isLoginFlag() == true) {
                objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_ALREADY);
            }else{
                DataAccessObject.getInstance().setLoginFlag(serverContext.getInfo().getStudentID(), true);
                Student s = new Student(serverContext.getInfo());
                s.setImages(serverContext.getInfo().getImages());

                objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_OK);
                objectReturn.addProperty("data", RsaManager.getInstance().getEncodedString(s, data.get("key").getAsString()));

                serverContext.setState(new Qr(serverContext));
            }
        }
        else            objectReturn.addProperty("seqType", SeqTypeConstants.LOGIN_NO_DATA);

        return objectReturn;
    }
}
