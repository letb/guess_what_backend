package frontend;

import main.Context;
import utils.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet (urlPatterns = {"/signup", "/signin", "/game", "/scoreboard"})
public class FrontServlet extends HttpServlet {

    public FrontServlet(Context context) {

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.getPage("index.html", null));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.getPage("index.html", null));

    }
}
