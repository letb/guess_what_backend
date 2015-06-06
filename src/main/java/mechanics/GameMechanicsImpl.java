package mechanics;

import main.Context;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import resource.GameSettings;
import resource.ResourceFactory;
import resource.ThreadSettings;
import resource.Words;
import user.GameUser;
import webSocketService.WebSocketService;
import utils.TimeHelper;
import webSocketService.messages.MessageNotifyGameOver;
import webSocketService.messages.MessageNotifyStartGame;

import java.util.*;

public final class GameMechanicsImpl implements GameMechanics {
    static final Logger logger = LogManager.getLogger(GameMechanics.class);
    private GameSettings gameSettings;
    private Words words;
    private static int serviceSleepTime;

    private final MessageSystem messageSystem;
    private final Address address = new Address();

    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private String waiter;
    private Boolean isAnswered = false;


    public GameMechanicsImpl(Context context) {
        ResourceFactory resourceFactory = ResourceFactory.instance();

        gameSettings = (GameSettings)resourceFactory.getResource("./data/gameSettings");
        if (gameSettings == null) {
            gameSettings = new GameSettings();
        }

        ThreadSettings threadSettings = (ThreadSettings)resourceFactory.getResource("./data/threadSettings");
        if (threadSettings == null) {
            threadSettings = new ThreadSettings();
        }
        serviceSleepTime = threadSettings.getServiceSleepTime();

        words = new Words(resourceFactory.getWords("./data/words"));
        if (words == null) {
            words = new Words(new String[]{"котик", "глаз", "телепузик", "Волошин", "vol-o-sheen"});
        }


        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);
    }

    // TODO concurrent linkedqueue
    public void addUser(String user) {
        if (waiter != null && !(waiter.equals(user))) {
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
            List<GameUser> users = currentGameSession.getUsers();
            Address to = messageSystem.getAddressService().getWebSocketService();

            for (GameUser gameUser : users) {
                gameUser.setAnsweredRight();
            }
        }
    }


    private void startGame(String first) {
        String second = waiter;
        GameSession gameSession = new GameSession(first, second, words.getWord());

        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);
        Address to = messageSystem.getAddressService().getWebSocketService();

        System.out.println("I try to send message");
        messageSystem.sendMessage(new MessageNotifyStartGame(address,
                to, gameSession.getGameUser(first), gameSession.getKeyword()));
        messageSystem.sendMessage(new MessageNotifyStartGame(address,
                to, gameSession.getGameUser(second), gameSession.getKeyword()));
    }


    private void gameStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() >= gameSettings.getGameTime() || session.isAnsweredRight()) {
                boolean firstWin = session.isFirstWin();
                Address to = messageSystem.getAddressService().getWebSocketService();
                messageSystem.sendMessage(new MessageNotifyGameOver(address, to, session.getFirst(),
                        firstWin, session.getKeyword()));
                messageSystem.sendMessage(new MessageNotifyGameOver(address, to, session.getSecond(),
                        firstWin, session.getKeyword()));
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
