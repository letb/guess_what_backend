package frontend;

import accountService.AccountService;
import main.Context;
import user.dataSets.UserDataSet;
import utils.JsonResponse;

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

    public ProfileServlet(Context context) {
        this.accountService = (AccountService)context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {


        UserDataSet userDataSet = accountService.getSessions(request.getSession().getId());

        JsonObject outerObject;
        JsonObject bodyObject = new JsonObject();
        JsonObject messages = new JsonObject();
        if(userDataSet == null ) {
            outerObject = JsonResponse.badJsonResponse(response, messages, bodyObject,
                    HttpServletResponse.SC_UNAUTHORIZED, "user", "not authorized");

        } else {
            response.setStatus(HttpServletResponse.SC_OK);

            bodyObject = userDataSet.getJson();
            outerObject = JsonResponse.getJsonResponse(200, bodyObject);

        }
        response.setContentType("application/json");
        response.getWriter().write(outerObject.toString());
    }
}
