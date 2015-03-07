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


public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        if(accountService.getSessions(request.getSession().getId()) != null ) {
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

        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> pageVariables = new HashMap<>();
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            pageVariables.put("signUpStatus", "All fields must be not empty! Try again");
            response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
        } else {
            if (accountService.addUser(name, new UserProfile(name, password, email))) {
                response.sendRedirect("/api/v1/auth/signin");
            } else {
                pageVariables.put("signUpStatus", "User with name: " + name + " already exists");
                response.getWriter().println(PageGenerator.getPage("signup.html", pageVariables));
            }
        }

    }

}
