package frontend;

import main.TimeHelper;
import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;

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

        Map<String, Object> pageVariables = new HashMap<>();

        UserProfile userProfile = accountService.getSessions(request.getSession().getId());
        if(userProfile != null) {
            if (userProfile.getLogin().contentEquals("admin")) {
                String timeString = request.getParameter("shutdown");
                if (timeString != null) {
                    int timeMS = Integer.valueOf(timeString);
                    System.out.print("Server will be down after: " + timeMS + " ms");
                    TimeHelper.sleep(timeMS);
                    System.out.print("\nShutdown");
                    System.exit(0);
                }

                String online = accountService.getNumberOfOnlineUsers();
                String numberOfUsers = accountService.getNumberOfUsers();
                pageVariables.put("online", online);
                pageVariables.put("numberOfUsers", numberOfUsers);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(PageGenerator.getPage("admin.html", pageVariables));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
