package control.ServerReaction;

import com.google.gson.JsonObject;

public interface State {
    public JsonObject reaction(JsonObject object);
}
