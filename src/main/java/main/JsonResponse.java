package main;

import com.google.gson.JsonObject;


public class JsonResponse {
    public static JsonObject getJsonResponse(int status, JsonObject innerObject) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", status);
        jsonObject.add("body", innerObject);

        return jsonObject;
    }
}
