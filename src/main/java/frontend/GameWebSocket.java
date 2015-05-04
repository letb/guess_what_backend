package frontend;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebSocket
public class GameWebSocket {
    static final Logger logger = LogManager.getLogger(GameWebSocket.class);
    private Set<GameWebSocket> users;
    private String userName;
    private Session session;

    public GameWebSocket(Set<GameWebSocket> users) { this.users = users; }

    @OnWebSocketMessage
    public void onMessage(String data) {
        for (GameWebSocket user : users) {
            try {
                user.getSession().getRemote().sendString(data);
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.info("onMessage: " + data);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        users.add(this);
        setSession(session);
        logger.info("onOpen");
    }
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        users.remove(this);
        logger.info("onClose");
    }

}
