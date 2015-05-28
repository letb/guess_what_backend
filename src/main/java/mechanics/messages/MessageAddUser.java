package mechanics.messages;

import mechanics.GameMechanics;
import messageSystem.Address;

public class MessageAddUser extends MessageToGameMechanics {
    private String user;

    public MessageAddUser (Address from, Address to, String user) {
        super(from, to);
        this.user = user;
    }

    @Override
    protected void exec(GameMechanics gameMechanics) {
        gameMechanics.addUser(user);
    }
}
