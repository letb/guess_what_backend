package frontend;

import base.AccountService;
import base.GameMechanics;
import base.dataSets.UserDataSet;
import com.google.gson.JsonObject;
import main.Context;
import utils.JsonResponse;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServlet extends HttpServlet {

    private GameMechanics gameMechanics;
    private AccountService accountService;

    public GameServlet(Context context) {
        this.gameMechanics = (GameMechanics)context.get(GameMechanics.class);
        this.accountService = (AccountService)context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        if(accountService.getSessions(request.getSession().getId()) != null ) {
            pageVariables.put("myName", accountService.getUserName(request.getSession().getId()));
            response.getWriter().println(PageGenerator.getPage("game.html", pageVariables));
        } else {
            pageVariables.put("signInStatus", "Time to login!");
            response.getWriter().println(PageGenerator.getPage("index.html", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        Map<String, Object> pageVariables = new HashMap<>();

        JsonObject bodyObject = new JsonObject();
        JsonObject outerObject;
        JsonObject messages = new JsonObject();

        UserDataSet userDataSet = accountService.getUser(name);
        if(userDataSet != null) {
            if(password.contentEquals(userDataSet.getPassword())) {
                accountService.addSession(request.getSession().getId(), userDataSet);
                response.setStatus(HttpServletResponse.SC_OK);
                bodyObject = userDataSet.getJson();
                pageVariables.put("myName", userDataSet.getName());
                outerObject = JsonResponse.getJsonResponse(200, bodyObject);
            } else {
                outerObject = JsonResponse.badJsonResponse(response, messages, bodyObject,
                        HttpServletResponse.SC_UNAUTHORIZED, "user", "wrong login or password");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            messages.addProperty("user", "wrong login or password");
            bodyObject.add("messages", messages);
            outerObject = JsonResponse.getJsonResponse(401, bodyObject);
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.getPage("game.html", pageVariables));
    }
}
