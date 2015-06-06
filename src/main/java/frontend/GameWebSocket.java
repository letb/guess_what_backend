package frontend;

import main.Context;
import mechanics.messages.MessageAddUser;
import mechanics.messages.MessageCheckAnswer;
import messageSystem.Address;
import messageSystem.MessageSystem;
import user.GameUser;
import webSocketService.WebSocketService;
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
    private String myDesktopName;
    private String myName;
    private String enemyName;
    private boolean isLeader = false;
    private boolean isJoystickExists = false;
    private Session session;
    private WebSocketService webSocketService;
    private MessageSystem messageSystem;
    private Address address;

    public GameWebSocket(String myName, Context context) {
        this.myName = myName;
        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        this.webSocketService = (WebSocketService) context.get(WebSocketService.class);
        this.address = messageSystem.getAddressService().getGameMechanics();
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

    public void gameOverByDisconnect() {
        try {
            JSONObject overObject = new JSONObject();
            JSONObject bodyObject = new JSONObject();

            bodyObject.put("message", "your enemy leave");

            overObject.put("type", "disconnect");
            overObject.put("body", bodyObject);


            webSocketService.getUserByName(enemyName).session.getRemote().sendString(overObject.toString());
        } catch (Exception e) {
            logger.catching(e);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            JSONObject messageObject = (JSONObject)new JSONParser().parse(data);

            if (messageObject.get("type").toString().contentEquals("chat")) {
                if (isJoystickExists) {
                    webSocketService.getUserByName(myDesktopName).session.getRemote().sendString(data);
                }
                webSocketService.getUserByName(myName).session.getRemote().sendString(data);
                webSocketService.getUserByName(enemyName).session.getRemote().sendString(data);

                JSONObject bodyObject = (JSONObject)messageObject.get("body");
                String message = (String)bodyObject.get("message");
                if (isJoystickExists) {
                    messageSystem.sendMessage(new MessageCheckAnswer(webSocketService.getAddress(),
                            address, myDesktopName, message));
                } else {
                    messageSystem.sendMessage(new MessageCheckAnswer(webSocketService.getAddress(),
                            address, myName, message));
                }
            } else if (messageObject.get("type").toString().contentEquals("init:desktop")) {
                webSocketService.addUser(this);
                messageSystem.sendMessage(new MessageAddUser(webSocketService.getAddress(),
                        address, myName));
            } else if (messageObject.get("type").toString().contentEquals("init:joystick")) {
                webSocketService.addUser(this);
                myDesktopName = myName;
                myName = myName + "_mobile";
                enemyName = webSocketService.getUserByName(myDesktopName).enemyName;

                isJoystickExists = true;
            } else {
                webSocketService.getUserByName(myName).session.getRemote().sendString(data);
                webSocketService.getUserByName(enemyName).session.getRemote().sendString(data);
                if (isJoystickExists)
                    webSocketService.getUserByName(myDesktopName).session.getRemote().sendString(data);
            }
        } catch (Exception e) {
            logger.catching(e);
        }
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        gameOverByDisconnect();
        logger.info("onClose");
    }

    public Address getAddress() {
        return address;
    }
}
