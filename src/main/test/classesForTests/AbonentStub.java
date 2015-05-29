package classesForTests;

import messageSystem.Abonent;
import messageSystem.Address;

public class AbonentStub implements Abonent {
    final private Address address;

    private boolean isExecuted = false;

    public AbonentStub () {
        this.address = new Address();
    }

    public Address getAddress() {
        return address;
    }

    public void execute() {
        isExecuted = true;
    }

    public boolean getExecuted() { return isExecuted; }
}