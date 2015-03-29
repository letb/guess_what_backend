package frontend;

import main.AccountService;
import main.UserProfile;
import main.JsonResponse;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by ivan on 01.03.15.
 */

public class ProfileServlet extends HttpServlet{
    private AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {


        UserProfile userProfile = accountService.getSessions(request.getSession().getId());

        JsonObject outerObject;
        JsonObject bodyObject = new JsonObject();
        JsonObject messages = new JsonObject();
        if(userProfile == null ) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            messages.addProperty("user", "not authorized");
            bodyObject.add("messages", messages);
            outerObject = JsonResponse.getJsonResponse(401, bodyObject);

        } else {
            response.setStatus(HttpServletResponse.SC_OK);

            bodyObject = userProfile.getJson();
            outerObject = JsonResponse.getJsonResponse(200, bodyObject);

        }
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        accountService.removeSessions(request.getSession().getId());

        JsonObject outerObject;
        JsonObject bodyObject = new JsonObject();

        outerObject = JsonResponse.getJsonResponse(200, bodyObject);
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}
