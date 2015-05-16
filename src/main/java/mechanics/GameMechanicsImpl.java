package mechanics;

import base.GameMechanics;
import base.WebSocketService;
import utils.TimeHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameMechanicsImpl implements GameMechanics {
    private static final int STEP_TIME = 100;
    private static final int gameTime = 15 * 1000;
    private static final String keyword = "ananas";


    private WebSocketService webSocketService;
    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private String waiter;

    public GameMechanicsImpl(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void addUser(String user) {
        if (waiter != null) {
            startGame(user);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    public boolean isAnswer(String word) {
        return word.contentEquals(keyword);
    }

    private void startGame(String first) {
        String second = waiter;
        GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(first));
        webSocketService.notifyStartGame(gameSession.getSelf(second));
    }

    private void gameStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() >= gameTime) {
                boolean firstWin = session.isFirstWin();
                webSocketService.notifyGameOver(session.getFirst(), firstWin);
                webSocketService.notifyGameOver(session.getSecond(), !firstWin);
            }
        }
    }

    public void run() {
        while (true) {
            gameStep();
            TimeHelper.sleep(STEP_TIME);
        }
    }

}
