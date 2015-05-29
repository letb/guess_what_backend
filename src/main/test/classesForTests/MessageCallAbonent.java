package classesForTests;


import messageSystem.Address;

public class MessageCallAbonent extends MessageToAbonentStub {

    public MessageCallAbonent(Address from, Address to) {
        super(from, to);
    }

    @Override
    protected void exec(AbonentStub abonentStub) {
        abonentStub.execute();
    }
}
