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

        Context serviceContext = new Context();
        serviceContext.add(MessageSystem.class, messageSystem);

        AccountService accountService = new AccountServiceImpl(serviceContext);
        serviceContext.add(AccountService.class, accountService);
        DBService dbService = new DBServiceImpl();
        serviceContext.add(DBService.class, dbService);
        WebSocketService webSocketService = new WebSocketServiceImpl(serviceContext);
        serviceContext.add(WebSocketService.class, webSocketService);
        GameMechanics gameMechanics = new GameMechanicsImpl(serviceContext);
        serviceContext.add(GameMechanics.class, gameMechanics);


        Servlet signin = new SignInServlet(serviceContext);
        Servlet signUp = new SignUpServlet(serviceContext);
        Servlet signOut = new SignOutServlet(serviceContext);
        Servlet profile = new ProfileServlet(serviceContext);
        Servlet admin = new AdminServlet(serviceContext);
        Servlet gameplay = new WebSocketGameServlet(serviceContext);
        Servlet game = new GameServlet(serviceContext);
        Servlet front = new FrontServlet(serviceContext);
        Servlet scoreboard = new ScoreboardServlet(serviceContext);

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

        Thread gameMechanicsThread = new Thread(gameMechanics);
        Thread webSocketServiceThread = new Thread(webSocketService);

        gameMechanicsThread.setDaemon(false);
        webSocketServiceThread.setDaemon(false);

        gameMechanicsThread.start();
        webSocketServiceThread.start();

        server.join();
    }
}
