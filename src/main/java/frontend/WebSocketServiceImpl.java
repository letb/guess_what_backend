package frontend;

import base.GameUser;
import base.WebSocketService;

import java.util.HashMap;
import java.util.Map;

public class WebSocketServiceImpl implements WebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public void addUser(GameWebSocket user) {
        userSockets.put(user.getMyName(), user);
    }

    public GameWebSocket getUserByName(String name) {
        return userSockets.get(name);
    }


    public void notifyStartGame(GameUser user, String keyword) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.startGame(user, keyword);
    }

    public void notifyGameOver(GameUser user, boolean win, String keyword) {
        GameWebSocket gameWebSocket = userSockets.get(user.getMyName());
        gameWebSocket.gameOver(user, win, keyword);
    }
}
