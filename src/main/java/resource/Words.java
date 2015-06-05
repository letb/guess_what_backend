package resource;

import java.io.Serializable;
import java.util.Random;

public class Words implements Serializable {
    private String[] words;
    private Random rand = new Random();

    public Words(String[] words) {
        this.words = words;
    }

    public String getWord() {
        return words[rand.nextInt(words.length)];
    }
}
