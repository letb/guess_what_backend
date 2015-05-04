package frontend;

import base.AccountService;
import base.DBService;
import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/game"})
public class WebSocketGameServlet extends WebSocketServlet {
    static final Logger logger = LogManager.getLogger(WebSocketGameServlet.class);
    private final static int IDLE_TIME = 10 * 60 * 1000;
    private AccountService accountService;

    public WebSocketGameServlet(Context context) {
        accountService = (AccountService)context.get(AccountService.class);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator());
        logger.info("Socket servlet configured");
    }

}
