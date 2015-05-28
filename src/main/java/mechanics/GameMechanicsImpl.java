package mechanics;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.GameSettings;
import resource.ResourceFactory;
import resource.ThreadSettings;
import user.GameUser;
import webSocketService.WebSocketService;
import utils.TimeHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class GameMechanicsImpl implements GameMechanics, Runnable, Abonent {
    static final Logger logger = LogManager.getLogger(GameMechanics.class);
    private static int stepTime;
    private static int gameTime;
    private static String keyword;
    private static int serviceSleepTime;

    private final WebSocketService webSocketService;
    private final MessageSystem messageSystem;
    private final Address address = new Address();

    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private String waiter;
    private Boolean isAnswered = false;


    public GameMechanicsImpl(WebSocketService webSocketService, MessageSystem messageSystem) {
        ResourceFactory resourceFactory = ResourceFactory.instance();

        GameSettings gameSettings = (GameSettings)resourceFactory.getResource("./data/gameSettings");
        if (gameSettings == null) {
            gameSettings = new GameSettings();
        }
        stepTime = gameSettings.getStepTime();
        gameTime = gameSettings.getGameTime();
        keyword = gameSettings.getKeyword();

        ThreadSettings threadSettings = (ThreadSettings)resourceFactory.getResource("./data/threadSettings");
        if (threadSettings == null) {
            threadSettings = new ThreadSettings();
        }
        serviceSleepTime = threadSettings.getServiceSleepTime();



        this.webSocketService = webSocketService;
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);
    }

    // TODO concurrent linkedqueue
    public void addUser(String user) {
        if (waiter != null && waiter != user) {
            startGame(user);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    public void checkAnswer(String userName, String word) {
        GameSession currentGameSession = nameToGame.get(userName);
        String sessionKeyword = currentGameSession.getKeyword();
        if (word.contentEquals(sessionKeyword)) {
            currentGameSession.setAnsweredRight();

            GameUser currentUser = currentGameSession.getGameUser(userName);
            currentUser.setAnsweredRight();
        }
    }


    private void startGame(String first) {
        String second = waiter;
        GameSession gameSession = new GameSession(first, second, keyword);

        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getGameUser(first), gameSession.getKeyword());
        webSocketService.notifyStartGame(gameSession.getGameUser(second), "");
    }


    private void gameStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() >= gameTime || session.isAnsweredRight()) {
                boolean firstWin = session.isFirstWin();
                webSocketService.notifyGameOver(session.getFirst(), firstWin, session.getKeyword());
                webSocketService.notifyGameOver(session.getSecond(), !firstWin, session.getKeyword());
                allSessions.remove(session);
            }
        }
    }

    public Address getAddress() {
        return address;
    }

    public void run() {
        while (true) {
            gameStep();
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(serviceSleepTime);
            } catch (InterruptedException e) {
                logger.catching(e);
            }
        }
    }

}
