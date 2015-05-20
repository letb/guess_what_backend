package utils;

import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JsonResponse {
    public static JsonObject getJsonResponse(int status, JsonObject innerObject) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("status", "" + status);
        jsonObject.add("body", innerObject);

        return jsonObject;
    }

    public static JsonObject badJsonResponse (HttpServletResponse response, JsonObject messages, JsonObject bodyObject,
                                              int status, String property, String value) {
        try { 
            response.sendError(status);
        } catch (IOException e) {}
        messages.addProperty(property, value);
        bodyObject.add("messages", messages);
        return JsonResponse.getJsonResponse(status, bodyObject);
    }

}
