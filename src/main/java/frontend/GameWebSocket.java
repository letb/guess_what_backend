package frontend;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@WebSocket
public class GameWebSocket {
    static final Logger logger = LogManager.getLogger(GameWebSocket.class);
//    private Set<GameWebSocket> users;
    private String myName;
    private String enemyName;
    private boolean isLeader;
    private Session session;
    private GameMechanics gameMechanics;
    private WebSocketService webSocketService;


    public GameWebSocket(String myName, GameMechanics gameMechanics, WebSocketService webSocketService) {
        this.myName = myName;
        this.gameMechanics = gameMechanics;
        this.webSocketService = webSocketService;
    }

    public String getMyName() {
        return myName;
    }

    public void startGame(GameUser user) {
        try {
            JSONObject jsonStart = new JSONObject();
            enemyName = user.getEnemyName();
            jsonStart.put("status", "start");
            jsonStart.put("enemyName", enemyName.toString());
            session.getRemote().sendString(jsonStart.toJSONString());
            logger.info(jsonStart.toJSONString());
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    public void gameOver(GameUser user, boolean win) {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            jsonStart.put("win", win);
            session.getRemote().sendString(jsonStart.toJSONString());
        } catch (Exception e) {
//            logger.catching(e);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            logger.info(data);
            session.getRemote().sendString(data);
            webSocketService.getUserByName(enemyName).session.getRemote().sendString(data);

            JSONObject jsonData = (JSONObject)new JSONParser().parse(data);
            gameMechanics.checkAnswer((String)jsonData.get("message"));
        } catch (Exception e) {
            logger.catching(e);
        }
        logger.info("onMessage: " + data);
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.info("onClose");
    }

}
