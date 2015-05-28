package resource;

import java.io.Serializable;

public class GameSettings implements Serializable {
    private static int stepTime; // = 100;
    private static int gameTime; // = 1200 * 1000;
    private static String keyword; // = "котик";


    public GameSettings() {
        this.stepTime = 4100;
        this.gameTime = 4120_000;
        this.keyword = "некотик";
    }

    public int getStepTime() {
        return stepTime;
    }

    public int getGameTime() {
        return gameTime;
    }

    public String getKeyword() {
        return keyword;
    }
}
