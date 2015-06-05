package user;

import java.util.ArrayList;
import java.util.List;

public class GameUser {
    private final String myName;
    private List<String> enemiesNames;
    private boolean isLeader = false;
    private boolean isAnsweredRight = false;
    public GameUser(String myName) {
        this.myName = myName;
    }

    public String getMyName() {
        return myName;
    }

    public List<String> getEnemiesNames() {
        return enemiesNames;
    }

    public void setAnsweredRight() {
        isAnsweredRight = true;
    }

    public boolean getAnsweredRight() {
        return isAnsweredRight;
    }

    public void setEnemiesNames(List<String> enemiesNames) {
        this.enemiesNames = enemiesNames;
    }

    public boolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader() {
        this.isLeader = true;
    }
}
