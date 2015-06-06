package webSocketService;

import main.Context;
import mechanics.GameMechanics;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.GameSettings;
import resource.ResourceFactory;
import resource.ThreadSettings;
import user.GameUser;
import frontend.GameWebSocket;

import java.util.HashMap;
import java.util.Map;

//TODO вместо переменных исп. объект

public class WebSocketServiceImpl implements WebSocketService {
    static final Logger logger = LogManager.getLogger(WebSocketService.class);
    private Map<String, GameWebSocket> userSockets = new HashMap<>();
    private static int stepTime;
    private static int gameTime;
    private static int serviceSleepTime;
    private MessageSystem messageSystem;
    private final Address address = new Address();

    public WebSocketServiceImpl(Context context) {
        ResourceFactory resourceFactory = ResourceFactory.instance();

        GameSettings gameSettings = (GameSettings)resourceFactory.getResource("./data/gameSettings");
        if (gameSettings == null) {
            gameSettings = new GameSettings();
        }
        stepTime = gameSettings.getStepTime();
        gameTime = gameSettings.getGameTime();
        ThreadSettings threadSettings = (ThreadSettings)resourceFactory.getResource("./data/threadSettings");
        if (threadSettings == null) {
            threadSettings = new ThreadSettings();
        }
        serviceSleepTime = threadSettings.getServiceSleepTime();

        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerWebSocketService(this);
    }

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    public GameWebSocket getUserByName(String name) {
        return userSockets.get(name);
    }


    public void notifyStartGame(GameUser user, String keyword) {
        System.out.println(user.getMyName());
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.startGame(user, keyword);
    }

    public void notifyGameOver(GameUser user, boolean win, String keyword) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.gameOver(user, win, keyword);
    }

    public Address getAddress() {
        return address;
    }

    public void run() {
        while (true) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(serviceSleepTime/10);
            } catch (InterruptedException e) {
                logger.catching(e);
            }
        }
    }
}
