package classesForTests;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

public abstract class MessageToAbonentStub extends Message {
    public MessageToAbonentStub(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AbonentStub) {
            exec((AbonentStub) abonent);
        }
    }

    protected abstract void exec(AbonentStub frontEnd);
}