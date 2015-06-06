package frontend;

import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/gameplay"})
public class WebSocketGameServlet extends WebSocketServlet {
    static final Logger logger = LogManager.getLogger(WebSocketGameServlet.class);
    private final static int IDLE_TIME = 10 * 60 * 1000;
    private Context context;

    public WebSocketGameServlet(Context context) {
        this.context = context;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(context));
//        logger.info("Socket servlet configured");
    }

}
