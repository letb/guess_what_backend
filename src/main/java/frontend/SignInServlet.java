package frontend;

import com.google.gson.JsonObject;
import base.AccountService;
import main.Context;
import utils.JsonResponse;
import base.dataSets.UserDataSet;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SignInServlet extends HttpServlet {
    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public SignInServlet(Context context) {
        this.accountService = (AccountService) context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        if(accountService.getSessions(request.getSession().getId()) != null ) {
            response.sendRedirect("/api/v1/profile");
        } else {
            pageVariables.put("signInStatus", "Time to login!");
            response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        JsonObject bodyObject = new JsonObject();
        JsonObject outerObject;
        JsonObject messages = new JsonObject();

        UserDataSet userDataSet = accountService.getUser(name);
        if(userDataSet != null) {
            if(password.contentEquals(userDataSet.getPassword())) {
                accountService.addSessions(request.getSession().getId(), userDataSet);
                response.setStatus(HttpServletResponse.SC_OK);
                bodyObject = userDataSet.getJson();
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
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}
