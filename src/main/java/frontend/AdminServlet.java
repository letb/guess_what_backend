package frontend;

import com.google.gson.JsonObject;
import utils.JsonResponse;
import utils.TimeHelper;
import base.AccountService;
import main.UserProfile;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 01.03.15.
 */

public class AdminServlet extends HttpServlet{
    private AccountService accountService;

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        JsonObject bodyObject = new JsonObject();
        JsonObject outerObject;
        JsonObject messages = new JsonObject();

        UserProfile userProfile = accountService.getSessions(request.getSession().getId());
        if(userProfile != null) {
            if (userProfile.getLogin().contentEquals("admin")) {
                String timeString = request.getParameter("shutdown");
                if (timeString != null) {
                    try {
                        int timeMS = Integer.valueOf(timeString);
                        System.out.print("Server will be down after: " + timeMS + " ms");
                        TimeHelper.sleep(timeMS);
                        System.out.print("\nShutdown");
                        System.exit(0);
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        messages.addProperty("timer", "use numbers");
                        bodyObject.add("messages", messages);
                        outerObject = JsonResponse.getJsonResponse(400, bodyObject);
                        response.setContentType("application/json");
                        response.getWriter().write(outerObject.toString());
                        return;
                    }
                }
                String online = accountService.getNumberOfOnlineUsers();
                String numberOfUsers = accountService.getNumberOfUsers();

                response.setStatus(HttpServletResponse.SC_OK);
                bodyObject.addProperty("number_of_users", numberOfUsers);
                bodyObject.addProperty("online", online);
                outerObject = JsonResponse.getJsonResponse(200, bodyObject);

            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                messages.addProperty("user", "not admin");
                bodyObject.add("messages", messages);
                outerObject = JsonResponse.getJsonResponse(401, bodyObject);
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            messages.addProperty("user", "not admin");
            bodyObject.add("messages", messages);
            outerObject = JsonResponse.getJsonResponse(401, bodyObject);
        }
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}
