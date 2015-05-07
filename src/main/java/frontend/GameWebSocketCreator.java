package frontend;

import base.AccountService;
import base.GameMechanics;
import base.WebSocketService;
import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameWebSocketCreator implements WebSocketCreator {
    static final Logger logger = LogManager.getLogger(GameWebSocketCreator.class);

    private AccountService accountService;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;

    public GameWebSocketCreator(Context context) {
        this.accountService = (AccountService)context.get(AccountService.class);
        this.gameMechanics = (GameMechanics)context.get(GameMechanics.class);
        this.webSocketService = (WebSocketService)context.get(WebSocketService.class);
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        String sessionId = request.getHttpServletRequest().getSession().getId();
        String name = accountService.getUserName(sessionId);
        logger.info("Socket created");
        return new GameWebSocket(name, gameMechanics, webSocketService);
    }
}
