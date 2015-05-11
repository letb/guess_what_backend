package main;

import base.DBService;
import base.GameMechanics;
import base.WebSocketService;
import dbService.DBServiceImpl;
import frontend.*;
import base.AccountService;
import mechanics.GameMechanicsImpl;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resource.ResourceFactory;
import resource.ServerSettings;

import javax.servlet.Servlet;


public class Main {
    public static void main(String[] args) throws Exception {
        ResourceFactory resourceFactory = ResourceFactory.instance();
        ServerSettings serverSettings = (ServerSettings)resourceFactory.getResource("serverSettings");
        if(serverSettings == null) {
            serverSettings = new ServerSettings();
        }
        int port = serverSettings.getPort();
        System.out.append("Starting at port: ").append("" + port).append('\n');

        Context context = new Context();
        AccountService accountService = new AccountServiceImpl();
        context.add(AccountService.class, accountService);
        DBService dbService = new DBServiceImpl();
        context.add(DBService.class, dbService);
        WebSocketService webSocketService = new WebSocketServiceImpl();
        context.add(WebSocketService.class, webSocketService);
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService);
        context.add(GameMechanics.class, gameMechanics);


        Servlet signin = new SignInServlet(context);
        Servlet signUp = new SignUpServlet(context);
        Servlet profile = new ProfileServlet(context);
        Servlet admin = new AdminServlet(context);
        Servlet gameplay = new WebSocketGameServlet(context);
        Servlet game = new GameServlet(context);
        Servlet front = new FrontServlet(context);

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        servletContext.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        servletContext.addServlet(new ServletHolder(profile), "/api/v1/profile");
        servletContext.addServlet(new ServletHolder(admin), "/api/v1/admin");
        servletContext.addServlet(new ServletHolder(gameplay), "/gameplay");
        servletContext.addServlet(new ServletHolder(game), "/game.html");
        servletContext.addServlet(new ServletHolder(front), "/");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, servletContext});

        Server server = new Server(port);
        server.setHandler(handlers);

        server.start();
//        server.join();

        gameMechanics.run();
    }
}
