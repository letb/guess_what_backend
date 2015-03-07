package main;

import com.google.gson.JsonObject;


/**
 * Created by ivan on 07.03.15.
 */
public class JsonResponse {
    public JsonObject getJsonResponse(int status, JsonObject innerObject) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", status);
        jsonObject.add("body", innerObject);

        return jsonObject;
    }
}
