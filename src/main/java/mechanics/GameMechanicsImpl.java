package mechanics;

import base.GameMechanics;
import base.GameUser;
import base.WebSocketService;
import com.google.gson.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.JSONObject;
import utils.TimeHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameMechanicsImpl implements GameMechanics {
    private static final int STEP_TIME = 100;
    private static final int gameTime = 1200 * 1000;
    private static final String keyword = "ананас";

    private WebSocketService webSocketService;
    private Map<String, GameSession> nameToGame = new HashMap<>();
    private Set<GameSession> allSessions = new HashSet<>();
    private String waiter;
    private Boolean isAnswered = false;


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

    public void run() {
        while (true) {
            gameStep();
            TimeHelper.sleep(STEP_TIME);
        }
    }

}
