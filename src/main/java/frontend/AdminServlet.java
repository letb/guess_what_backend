package frontend;

import com.google.gson.JsonObject;
import main.Context;
import utils.JsonResponse;
import utils.TimeHelper;
import base.AccountService;
import base.dataSets.UserDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ivan on 01.03.15.
 */

public class AdminServlet extends HttpServlet{
    private AccountService accountService;

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public AdminServlet(Context context) {
        this.accountService = (AccountService)context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        JsonObject bodyObject = new JsonObject();
        JsonObject outerObject;
        JsonObject messages = new JsonObject();

        UserDataSet userDataSet = accountService.getSessions(request.getSession().getId());
        if(userDataSet != null) {
            if (userDataSet.getLogin().contentEquals("admin")) {
                String timeString = request.getParameter("shutdown");
                if (timeString != null) {
                    try {
                        int timeMS = Integer.valueOf(timeString);
                        System.out.print("Server will be down after: " + timeMS + " ms");
                        TimeHelper.sleep(timeMS);
                        System.out.print("\nShutdown");
                        System.exit(0);
                    } catch (NumberFormatException e) {
                        outerObject = JsonResponse.badJsonResponse(response, messages, bodyObject,
                                HttpServletResponse.SC_BAD_REQUEST, "timer", "use numbers");
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
                outerObject = JsonResponse.getJsonResponse(HttpServletResponse.SC_OK, bodyObject);

            } else {
                outerObject = JsonResponse.badJsonResponse(response, messages, bodyObject,
                        HttpServletResponse.SC_UNAUTHORIZED, "user", "not admin");
            }
        } else {
            outerObject = JsonResponse.badJsonResponse(response, messages, bodyObject,
                    HttpServletResponse.SC_UNAUTHORIZED, "user", "not admin");
        }
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}
