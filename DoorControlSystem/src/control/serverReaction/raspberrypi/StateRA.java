package control.serverReaction.raspberrypi;

import com.google.gson.JsonObject;

public interface StateRA {
    public JsonObject reaction(JsonObject object);
}
