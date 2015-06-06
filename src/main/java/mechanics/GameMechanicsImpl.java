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
import java.util.concurrent.ConcurrentLinkedQueue;

public final class GameMechanicsImpl implements GameMechanics {
    static final Logger logger = LogManager.getLogger(GameMechanics.class);
    private GameSettings gameSettings;
    private Words words;
    private static int serviceSleepTime;

    private final MessageSystem messageSystem;
    private final Address address = new Address();

    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private ConcurrentLinkedQueue<String> waiters = new ConcurrentLinkedQueue<String>();

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
            words = new Words(new String[]{"котик", "телепузик", "глаз", "Волошин", "vol-o-sheen"});
        }


        this.messageSystem = (MessageSystem) context.get(MessageSystem.class);
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);
    }

    public void addUser(String user) {
        if (!waiters.contains(user)) {
            waiters.add(user);
        }

        if (waiters.size() == 2) {
            startGame(waiters);    // TODO: get from resources
            System.out.println("Starting game");
            waiters.clear(); // TODO: ??
        }
    }

    public void checkAnswer(String userName, String word) {
        GameSession currentGameSession = nameToGame.get(userName);
        String sessionKeyword = currentGameSession.getKeyword();
        if (word.contentEquals(sessionKeyword)) {
            currentGameSession.setAnsweredRight();
            currentGameSession.setAnswerer(userName);

            GameUser currentUser = currentGameSession.getGameUser(userName);
            currentUser.setAnsweredRight();
        }
    }


    private void startGame(ConcurrentLinkedQueue<String> waiters) {
        GameSession gameSession = new GameSession(waiters, words.getWord());

        allSessions.add(gameSession);
        for (String userName : waiters) {
            nameToGame.put(userName, gameSession);
        }
        Address to = messageSystem.getAddressService().getWebSocketService();

        System.out.println("I try to send message");
        for (String userName : waiters) {
            messageSystem.sendMessage(new MessageNotifyStartGame(address,
                    to, gameSession.getGameUser(userName), gameSession.getKeyword()));
        }
    }


    private void gameStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() >= gameSettings.getGameTime() || session.isAnsweredRight()) {
                Address to = messageSystem.getAddressService().getWebSocketService();

                for (GameUser gameUser : session.getUsers()) {
                    messageSystem.sendMessage(new MessageNotifyGameOver(address, to, gameUser,
                            gameUser.getAnsweredRight(), session.getKeyword()));
                }

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
