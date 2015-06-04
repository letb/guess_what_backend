package messageSystem;

import classesForTests.AbonentStub;
import classesForTests.MessageCallAbonent;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class MessageSystemTest {
    MessageSystem messageSystem;

    @Before
    public void start() throws Exception {
        messageSystem = new MessageSystem();
    }

    @Test
    public void testSendMessage() throws Exception {
        AbonentStub abonent1 = new AbonentStub();
        AbonentStub abonent2 = new AbonentStub();
        messageSystem.addService(abonent1);
        messageSystem.addService(abonent2);

        assertEquals(false, abonent2.getExecuted());
        assertEquals(false, abonent1.getExecuted());

        messageSystem.sendMessage(new MessageCallAbonent(abonent1.getAddress(), abonent2.getAddress()));
        messageSystem.sendMessage(new MessageCallAbonent(abonent2.getAddress(), abonent1.getAddress()));

        messageSystem.execForAbonent(abonent1);
        messageSystem.execForAbonent(abonent2);

        assertEquals(true, abonent2.getExecuted());
        assertEquals(true, abonent1.getExecuted());
    }


    @Test
    public void testSendMessageToNotExistingAbonent() throws Exception {
        AbonentStub abonent1 = new AbonentStub();
        AbonentStub abonent2 = new AbonentStub();
        messageSystem.addService(abonent1);
        assertEquals(false, abonent2.getExecuted());

        messageSystem.sendMessage(new MessageCallAbonent(abonent1.getAddress(), abonent2.getAddress()));
        assertEquals(false, abonent2.getExecuted());
    }
}
