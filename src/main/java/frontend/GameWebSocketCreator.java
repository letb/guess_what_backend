package frontend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameWebSocketCreator implements WebSocketCreator{
    static final Logger logger = LogManager.getLogger(GameWebSocketCreator.class);
    private Set<GameWebSocket> users;

    public GameWebSocketCreator() {
        this.users = Collections.newSetFromMap(new ConcurrentHashMap<GameWebSocket, Boolean>());
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        GameWebSocket socket = new GameWebSocket(users);
        logger.info("Socket created");
        return socket;
    }
}
