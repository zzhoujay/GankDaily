package zhou.app.gankdaily.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zhou.app.gankdaily.model.Gank;
import zhou.app.gankdaily.model.GankDaily;
import zhou.app.gankdaily.model.Result;

class JsonKit {

    public static Result generate(String json, Gson gson) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonObject root = element.getAsJsonObject();
        JsonArray category = root.getAsJsonArray("category");
        Iterator<JsonElement> iterator = category.iterator();
        JsonObject results = root.getAsJsonObject("results");
        boolean error = root.get("error").getAsBoolean();
        ArrayList<String> types = new ArrayList<>(category.size());
        ArrayList<List<Gank>> ghs = new ArrayList<>(category.size());
        while (iterator.hasNext()) {
            JsonElement type = iterator.next();
            String t = type.getAsString();
            types.add(t);
            String e = results.get(t).toString();
            ghs.add(gson.fromJson(e, new TypeToken<List<Gank>>() {
            }.getType()));
        }

        return new Result(error, new GankDaily(types, ghs));
    }

}