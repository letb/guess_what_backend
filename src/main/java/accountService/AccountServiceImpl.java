package accountService;

import dbService.DBService;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import resource.ThreadSettings;
import user.dataSets.UserDataSet;
import dbService.DBServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class AccountServiceImpl implements AccountService, Abonent, Runnable {
    private final Map<String, UserDataSet> users = new HashMap<>();
    private final Map<String, UserDataSet> sessions = new HashMap<>();
    private final DBService dbService = new DBServiceImpl();

    private final MessageSystem messageSystem;
    private final Address address = new Address();

    public AccountServiceImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerAccountService(this);
    }

    public boolean addUser(String userName, UserDataSet userDataSet) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userDataSet);
        dbService.save(userDataSet);
        return true;
    }

    public void addSession(String sessionId, UserDataSet userDataSet) {
        sessions.put(sessionId, userDataSet);
    }

    public UserDataSet getUser(String userName) {
        UserDataSet user = users.get(userName);
        if (user == null) {
            user = dbService.readByLogin(userName);
            if (user != null) {
                users.put(userName, user);
            }
        }
        return user;
    }

    public UserDataSet getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

    public String getUserName(String sessionId) {
        return sessions.get(sessionId).getName();
    }

    public UserDataSet removeSessions(String sessionId) { return sessions.remove(sessionId); }

    public String getNumberOfOnlineUsers() { return String.valueOf(sessions.size()); }

    public String getNumberOfUsers() { return String.valueOf(users.size()); }

    public UserDataSet[] getScoreboard () {
        List usersList = dbService.getScoreboard();
        UserDataSet[] users = new UserDataSet[usersList.size()];
        if(usersList.size() != 0) {
            usersList.toArray(users);
        }
        return users;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true){
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(ThreadSettings.SERVICE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
