package tests.classesForTests;

import base.AccountService;
import base.dataSets.UserDataSet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 03.04.15.
 */
public class AccountServiceStub implements AccountService {

    private Map<String, UserDataSet> users = new HashMap<>();
    private Map<String, UserDataSet> sessions = new HashMap<>();

    public boolean addUser(String userName, UserDataSet userDataSet) {
        return true;
    }

    public UserDataSet getUser(String userName) {
        return new UserDataSet("test", "123", "test@test");
    }

    public void addSessions(String sessionId, UserDataSet userDataSet) {

    }

    public UserDataSet getSessions(String sessionId) {
        return null;
    }

    public UserDataSet removeSessions(String sessionId) { return sessions.remove(sessionId); }

    public String getNumberOfOnlineUsers() { return String.valueOf(sessions.size()); }

    public String getNumberOfUsers() { return String.valueOf(users.size()); }
}
