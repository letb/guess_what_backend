package mechanics.messages;

import mechanics.GameMechanics;
import messageSystem.Address;

public class MessageCheckAnswer extends MessageToGameMechanics {
    private String user;
    private String message;

    public MessageCheckAnswer (Address from, Address to, String user, String message) {
        super(from, to);
        this.user = user;
        this.message = message;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        gameMechanics.checkAnswer(user, message);
    }
}
