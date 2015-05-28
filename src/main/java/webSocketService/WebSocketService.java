package webSocketService;


import user.GameUser;
import frontend.GameWebSocket;

public interface WebSocketService {

    void addUser(GameWebSocket user);

//    void notifyUserRightAnswer(GameUser user);
    GameWebSocket getUserByName(String name);

    void notifyStartGame(GameUser user, String keyword);

    void notifyGameOver(GameUser user, boolean win, String keyword);
}
