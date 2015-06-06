package mechanics;

import user.GameUser;

import java.util.*;

public class GameSession {
    private final long startTime;
    private final GameUser first;
    private final GameUser second;
    private boolean isAnswered;
    private String keyword;

    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(String user1, String user2, String keyword) {
        startTime = new Date().getTime();
        GameUser gameUser1 = new GameUser(user1);
        gameUser1.setEnemyName(user2);
        gameUser1.setIsLeader();

        GameUser gameUser2 = new GameUser(user2);
        gameUser2.setEnemyName(user1);

        users.put(user1, gameUser1);
        users.put(user2, gameUser2);

        this.first = gameUser1;
        this.second = gameUser2;

        this.keyword = keyword;
    }

    public GameUser getEnemy(String user) {
        String enemyName = users.get(user).getEnemyName();
        return users.get(enemyName);
    }

    public GameUser getGameUser (String user) {
        return users.get(user);
    }

    public long getSessionTime() {
        return new Date().getTime() - startTime;
    }

    public GameUser getFirst() {
        return first;
    }

    public GameUser getSecond() {
        return second;
    }

    public boolean isFirstWin() {
        return first.getAnsweredRight();
    }

    public boolean isAnsweredRight() {
        return isAnswered;
    }

    public void setAnsweredRight() {
        this.isAnswered = true;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<GameUser> getUsers() {
        List<GameUser> users = new ArrayList<GameUser>();
        users.add(first);
        users.add(second);
        return users;
    }

}
