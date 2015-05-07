package base;


import frontend.GameWebSocket;

public interface WebSocketService {

    void addUser(GameWebSocket user);

//    void notifyUserRightAnswer(GameUser user);

    void notifyStartGame(GameUser user);

    void notifyGameOver(GameUser user, boolean win);
}
