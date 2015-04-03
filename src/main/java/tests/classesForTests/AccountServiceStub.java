package tests.classesForTests;

import base.AccountService;
import main.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 03.04.15.
 */
public class AccountServiceStub implements AccountService {

    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        return true;
    }

    public UserProfile getUser(String userName) {
        return new UserProfile("test", "123", "test@test");
    }

    public void addSessions(String sessionId, UserProfile userProfile) {

    }

    public UserProfile getSessions(String sessionId) {
        return null;
    }

    public UserProfile removeSessions(String sessionId) { return sessions.remove(sessionId); }

    public String getNumberOfOnlineUsers() { return String.valueOf(sessions.size()); }

    public String getNumberOfUsers() { return String.valueOf(users.size()); }
}
