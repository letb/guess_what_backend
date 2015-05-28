package frontend;

import accountService.AccountService;
import com.google.gson.JsonObject;
import main.Context;
import utils.JsonResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignOutServlet extends HttpServlet {
    private AccountService accountService;

    public SignOutServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public SignOutServlet(Context context) {
        this.accountService = (AccountService)context.get(AccountService.class);
    }

    public void doGet(HttpServletRequest request,
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
