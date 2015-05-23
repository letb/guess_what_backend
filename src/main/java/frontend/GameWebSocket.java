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

    public void startGame(GameUser user, String keyword) {
        try {
            JSONObject startObject = new JSONObject();
            JSONObject bodyObject = new JSONObject();
            enemyName = user.getEnemyName();
            isLeader = user.getIsLeader();

            bodyObject.put("enemy", enemyName);
            bodyObject.put("leader", isLeader);
            if (isLeader)
                bodyObject.put("keyword", keyword);

            startObject.put("type", "start");
            startObject.put("body", bodyObject);

            session.getRemote().sendString(startObject.toJSONString());
            logger.info(startObject.toJSONString());
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    public void gameOver(GameUser user, boolean win, String keyword) {
        try {
            JSONObject overObject = new JSONObject();
            JSONObject bodyObject = new JSONObject();

            bodyObject.put("win", win);
            bodyObject.put("answer", keyword);

            overObject.put("type", "finish");
            overObject.put("body", bodyObject);

            session.getRemote().sendString(overObject.toJSONString());
        } catch (Exception e) {
            logger.catching(e);
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
            JSONObject messageObject = (JSONObject)new JSONParser().parse(data);

            if (messageObject.get("type").toString().contentEquals("chat")) {
                session.getRemote().sendString(data);
                webSocketService.getUserByName(enemyName).session.getRemote().sendString(data);

                JSONObject bodyObject = (JSONObject)messageObject.get("body");
                String message = (String)bodyObject.get("message");
                gameMechanics.checkAnswer(myName, message);
            } else if (messageObject.get("type").toString().contentEquals("canvas")) {
                session.getRemote().sendString(data);
                webSocketService.getUserByName(enemyName).session.getRemote().sendString(data);
            }
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
