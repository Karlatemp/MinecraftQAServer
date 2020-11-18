package io.github.karlatemp.mqaserver.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class PlayerInfoSerializer implements JsonSerializer<ServerPing.PlayerInfo>, JsonDeserializer<ServerPing.PlayerInfo> {

    @Override
    public ServerPing.PlayerInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject js = json.getAsJsonObject();
        ServerPing.PlayerInfo info = new ServerPing.PlayerInfo(js.get("name").getAsString(), (UUID) null);
        String id = js.get("id").getAsString();
        if (!id.contains("-")) {
            info.setId(id);
        } else {
            info.setUniqueId(UUID.fromString(id));
        }
        return info;
    }

    @Override
    public JsonElement serialize(ServerPing.PlayerInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject out = new JsonObject();
        out.addProperty("name", src.getName());
        out.addProperty("id", src.getUniqueId().toString());
        return out;
    }
}
