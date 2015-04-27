package main;

import base.DBService;
import dbService.DBServiceImpl;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import frontend.ProfileServlet;
import frontend.AdminServlet;
import base.AccountService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];
        int port = 0;
        try {
            port = Integer.valueOf(portString);
        } catch (NumberFormatException e) {
            System.out.append("Use integer port as the first argument");
            System.exit(2);
        }
        System.out.append("Starting at port: ").append(portString).append('\n');

        Context context = new Context();
        AccountService accountService = new AccountServiceImpl();
        context.add(AccountService.class, accountService);
        DBService dbService = new DBServiceImpl();
        context.add(DBService.class, dbService);


        Servlet signin = new SignInServlet(context);
        Servlet signUp = new SignUpServlet(context);
        Servlet profile = new ProfileServlet(context);
        Servlet admin = new AdminServlet(context);

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        servletContext.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        servletContext.addServlet(new ServletHolder(profile), "/api/v1/profile");
        servletContext.addServlet(new ServletHolder(admin), "/api/v1/admin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, servletContext});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
