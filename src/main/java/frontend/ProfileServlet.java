package frontend;

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

public class ProfileServlet extends HttpServlet{
    private AccountService accountService;

    public ProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        UserProfile userProfile = accountService.getSessions(request.getSession().getId());
        if(userProfile == null ) {
            response.sendRedirect("/signin");
        } else {
            String name = userProfile.getLogin();
            String email = userProfile.getEmail();
            pageVariables.put("name", name);
            pageVariables.put("email", email);
            response.getWriter().println(PageGenerator.getPage("profile.html", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        accountService.removeSessions(request.getSession().getId());
        response.sendRedirect("/signin");
    }
}