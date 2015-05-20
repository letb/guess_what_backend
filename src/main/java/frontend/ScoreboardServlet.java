package frontend;

import base.AccountService;
import base.dataSets.UserDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import main.Context;
import utils.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ScoreboardServlet extends HttpServlet {
    private AccountService accountService;

    public ScoreboardServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public ScoreboardServlet(Context context) {
        this.accountService = (AccountService)context.get(AccountService.class);
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        UserDataSet[] users = accountService.getScoreboard();
        JsonArray arrayObject = new JsonArray();
        JsonObject bodyObject = new JsonObject();
        JsonObject outerObject;
        for (int i = 0; i < users.length; i++) {
            JsonObject arrayElem = new JsonObject();
            arrayElem.addProperty("login", users[i].getName());
            arrayElem.addProperty("score", users[i].getScore());
            arrayObject.add(arrayElem);
        }
        bodyObject.add("users", arrayObject);
        outerObject = JsonResponse.getJsonResponse(200, bodyObject);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}
