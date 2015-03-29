package frontend;

import com.google.gson.JsonObject;
import interfaces.AccountService;
import main.UserProfile;
import templater.PageGenerator;
import main.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        if (accountService.getSessions(request.getSession().getId()) != null) {
            response.sendRedirect("/api/v1/profile");
        } else {
            pageVariables.put("signUpStatus", "Let's try sign up!");
            response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        JsonObject bodyObject = new JsonObject();
        JsonObject outerObject;
        JsonObject messages = new JsonObject();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            if (name.isEmpty()) messages.addProperty("login", "shouldn't be empty");
            if (email.isEmpty()) messages.addProperty("email", "shouldn't be empty");
            if (password.isEmpty()) messages.addProperty("password", "shouldn't be empty");
            bodyObject.add("messages", messages);
            outerObject = JsonResponse.getJsonResponse(403, bodyObject);
        } else {
            if (accountService.addUser(name, new UserProfile(name, password, email))) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                UserProfile userProfile = accountService.getUser(name);
                bodyObject = userProfile.getJson();
                outerObject = JsonResponse.getJsonResponse(201, bodyObject);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                messages.addProperty("user", "already exist");
                bodyObject.add("messages", messages);
                outerObject = JsonResponse.getJsonResponse(401, bodyObject);
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}