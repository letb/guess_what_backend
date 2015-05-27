package mechanics;

import messageSystem.Abonent;

public interface GameMechanics {

    public void addUser(String user);

    public void checkAnswer(String userName, String word);

    public void run();
}
