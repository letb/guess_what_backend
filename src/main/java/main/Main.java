package main;

import frontend.SignInServlet;
import frontend.SignUpServlet;
import frontend.ProfileServlet;
import frontend.AdminServlet;
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

        AccountService accountService = new AccountService();
        accountService.addUser("admin", new UserProfile("admin", "123", "admin@admin"));
        accountService.addUser("test", new UserProfile("test", "123", "test@test"));

        Servlet signin = new SignInServlet(accountService);
        Servlet signUp = new SignUpServlet(accountService);
        Servlet profile = new ProfileServlet(accountService);
        Servlet admin = new AdminServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        context.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        context.addServlet(new ServletHolder(profile), "/api/v1/profile");
        context.addServlet(new ServletHolder(admin), "/api/v1/admin");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
