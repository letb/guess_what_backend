package webSocketService;


import messageSystem.Abonent;
import user.GameUser;
import frontend.GameWebSocket;

public interface WebSocketService extends Abonent, Runnable {

    void addUser(GameWebSocket user);

    GameWebSocket getUserByName(String name);

    void notifyStartGame(GameUser user, String keyword);

    void notifyGameOver(GameUser user, boolean win, String keyword);
}
