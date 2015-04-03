package tests.classesForTests;

import base.AccountService;
import main.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 03.04.15.
 */
public class AccountServiceStubExist implements AccountService{
    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();

    public boolean addUser(String userName, UserProfile userProfile) {
        return false;
    }

    public UserProfile getUser(String userName) {
        return new UserProfile("name", "password", "email");
    }

    public void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public UserProfile getSessions(String sessionId) {
        return new UserProfile("test", "123", "test@test");
    }

    public UserProfile removeSessions(String sessionId) { return new UserProfile("test", "123", "test@test"); }

    public String getNumberOfOnlineUsers() { return String.valueOf(sessions.size()); }

    public String getNumberOfUsers() { return String.valueOf(users.size()); }
}
