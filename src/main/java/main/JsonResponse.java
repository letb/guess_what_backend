package main;

import com.google.gson.JsonObject;


/**
 * Created by ivan on 07.03.15.
 */
public class JsonResponse {
    public JsonObject getJsonResponce(JsonObject innerObject) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", "200");
        jsonObject.add("body", innerObject);

        return jsonObject;
    }
}
