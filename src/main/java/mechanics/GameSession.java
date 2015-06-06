package mechanics;

import user.GameUser;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameSession {
    private final long startTime;
    List<GameUser> gameUsers;
    List<String> userNames;
    private boolean isAnswered;
    private String answerer;
    private String keyword;

    private Map<String, GameUser> users = new HashMap<>();

    public GameSession(ConcurrentLinkedQueue<String> waiters, String keyword) {
        startTime = new Date().getTime();
        gameUsers = new ArrayList<>();
        userNames = new ArrayList<>();
        System.out.println("waiters size: " + waiters.size());

        int i = 0;
        while (!waiters.isEmpty()) {
            String tmp = waiters.poll();
            if (tmp != null)
                userNames.add(tmp);
        }

        System.out.println("userNames size: " + userNames.size());

        for (String currUserName: userNames) {
            GameUser newGameUser = new GameUser(currUserName);
            gameUsers.add(newGameUser);
            users.put(currUserName, newGameUser);
        }
        System.out.println("users size: " + users.size());
        for (GameUser currGameUser: gameUsers) {
            List<String> enemiesUserNames = new ArrayList<>();
            enemiesUserNames.remove(currGameUser.getMyName());
            currGameUser.setEnemiesNames(enemiesUserNames);
        }

        this.keyword = keyword;
    }

    public List<String> getEnemies(String user) {
        return users.get(user).getEnemiesNames();
    }

    public GameUser getGameUser (String user) {
        return users.get(user);
    }

    public long getSessionTime() {
        return new Date().getTime() - startTime;
    }

    public List<GameUser> getUsers() {
        return gameUsers;
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

    public void setAnswerer(String user) {
        answerer = user;
    }

    public String getAnswerer() {
        return answerer;
    }
}
