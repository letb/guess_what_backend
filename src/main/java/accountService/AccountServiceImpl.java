package accountService;

import dbService.DBService;
import main.Context;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import resource.ResourceFactory;
import resource.ThreadSettings;
import user.dataSets.UserDataSet;
import dbService.DBServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class AccountServiceImpl implements AccountService {
    private final Map<String, UserDataSet> users = new HashMap<>();
    private final Map<String, UserDataSet> sessions = new HashMap<>();
    private final DBService dbService = new DBServiceImpl();

    private final MessageSystem messageSystem;
    private final Address address = new Address();

    private static int serviceSleepTime;

    public AccountServiceImpl(Context context) {
        ResourceFactory resourceFactory = ResourceFactory.instance();
        ThreadSettings threadSettings = (ThreadSettings)resourceFactory.getResource("./data/threadSettings");
        if (threadSettings == null) {
            threadSettings = new ThreadSettings();
        }
        serviceSleepTime = threadSettings.getServiceSleepTime();

        this.messageSystem = (MessageSystem)context.get(MessageSystem.class);
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
                Thread.sleep(serviceSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
