package user;

public class GameUser {
    private final String myName;
    private String enemyName;
    private boolean isLeader = false;
    private boolean isAnsweredRight = false;
    public GameUser(String myName) {
        this.myName = myName;
    }

    public String getMyName() {
        return myName;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setAnsweredRight() {
        isAnsweredRight = true;
    }

    public boolean getAnsweredRight() {
        return isAnsweredRight;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader() {
        this.isLeader = true;
    }
}
