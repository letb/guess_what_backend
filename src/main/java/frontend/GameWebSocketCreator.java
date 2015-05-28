package frontend;

import accountService.AccountService;
import mechanics.GameMechanics;
import webSocketService.WebSocketService;
import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.net.ConnectException;

public class GameWebSocketCreator implements WebSocketCreator {
    static final Logger logger = LogManager.getLogger(GameWebSocketCreator.class);

    private AccountService accountService;
    private Context context;

    public GameWebSocketCreator(Context context) {
        this.accountService = (AccountService)context.get(AccountService.class);
        this.context = context;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        String sessionId = request.getHttpServletRequest().getSession().getId();
        String name = accountService.getUserName(sessionId);
        logger.info("Socket created");
        return new GameWebSocket(name, context);
    }
}
