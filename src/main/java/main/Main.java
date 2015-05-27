package main;

import accountService.AccountServiceImpl;
import dbService.DBService;
import mechanics.GameMechanics;
import messageSystem.MessageSystem;
import webSocketService.WebSocketService;
import dbService.DBServiceImpl;
import frontend.*;
import accountService.AccountService;
import mechanics.GameMechanicsImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import resource.ResourceFactory;
import resource.ServerSettings;
import webSocketService.WebSocketServiceImpl;

import javax.servlet.Servlet;


public class Main {
    static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        final MessageSystem messageSystem = new MessageSystem();
        final ResourceFactory resourceFactory = ResourceFactory.instance();
        ServerSettings serverSettings = (ServerSettings)resourceFactory.getResource("./data/serverSettings");


        if(serverSettings == null) {
            serverSettings = new ServerSettings();
        }
        int port = serverSettings.getPort();
        logger.info("Starting at port: " + port + '\n');

        AccountService accountService = new AccountServiceImpl(messageSystem);
        DBService dbService = new DBServiceImpl();
        WebSocketService webSocketService = new WebSocketServiceImpl();
        GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService, messageSystem);

        Context context = new Context();
        context.add(AccountService.class, accountService);
        context.add(DBService.class, dbService);
        context.add(WebSocketService.class, webSocketService);
        context.add(GameMechanics.class, gameMechanics);


        Servlet signin = new SignInServlet(context);
        Servlet signUp = new SignUpServlet(context);
        Servlet signOut = new SignOutServlet(context);
        Servlet profile = new ProfileServlet(context);
        Servlet admin = new AdminServlet(context);
        Servlet gameplay = new WebSocketGameServlet(context);
        Servlet game = new GameServlet(context);
        Servlet front = new FrontServlet(context);
        Servlet scoreboard = new ScoreboardServlet(context);

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.addServlet(new ServletHolder(signin), "/api/v1/auth/signin");
        servletContext.addServlet(new ServletHolder(signUp), "/api/v1/auth/signup");
        servletContext.addServlet(new ServletHolder(signOut), "/api/v1/auth/signout");
        servletContext.addServlet(new ServletHolder(profile), "/api/v1/auth/current");
        servletContext.addServlet(new ServletHolder(admin), "/api/v1/admin");
        servletContext.addServlet(new ServletHolder(gameplay), "/gameplay");
        servletContext.addServlet(new ServletHolder(game), "/game.html");
        servletContext.addServlet(new ServletHolder(front), "/");
        servletContext.addServlet(new ServletHolder(scoreboard), "/api/v1/scoreboard");


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
