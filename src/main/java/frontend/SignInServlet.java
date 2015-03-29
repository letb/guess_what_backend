package frontend;

import com.google.gson.JsonObject;
import interfaces.AccountService;
import main.JsonResponse;
import main.UserProfile;
import templater.PageGenerator;

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

        UserProfile userProfile = accountService.getUser(name);
        if(userProfile != null) {
            if(password.contentEquals(userProfile.getPassword())) {
                accountService.addSessions(request.getSession().getId(), userProfile);
                response.setStatus(HttpServletResponse.SC_OK);
                bodyObject = userProfile.getJson();
                outerObject = JsonResponse.getJsonResponse(200, bodyObject);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                messages.addProperty("user", "wrong login or password");
                bodyObject.add("messages", messages);
                outerObject = JsonResponse.getJsonResponse(401, bodyObject);
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
