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
 * @author v.chibrikov
 */
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
            response.sendRedirect("/api/v1/auth/profile");
        } else {
            pageVariables.put("signInStatus", "Time to login!");
            response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile userProfile = accountService.getUser(name);
        if(userProfile != null) {
            if(password.contentEquals(userProfile.getPassword())) {
                accountService.addSessions(request.getSession().getId(), userProfile);
                response.sendRedirect("/api/v1/auth/profile");
            } else {
                pageVariables.put("signInStatus", "Wrong password");
            }
        } else {
            pageVariables.put("signInStatus", "Wrong login");
        }


        response.getWriter().println(PageGenerator.getPage("signin.html", pageVariables));
    }
}
