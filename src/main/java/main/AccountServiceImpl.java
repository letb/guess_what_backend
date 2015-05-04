package main;

import base.AccountService;
import base.DBService;
import base.dataSets.UserDataSet;
import dbService.DBServiceImpl;

import java.util.HashMap;
import java.util.Map;


public class AccountServiceImpl implements AccountService {
    private Map<String, UserDataSet> users = new HashMap<>();
    private Map<String, UserDataSet> sessions = new HashMap<>();
    private DBService dbService = new DBServiceImpl();

    public boolean addUser(String userName, UserDataSet userDataSet) {
        if (users.containsKey(userName))
            return false;
        users.put(userName, userDataSet);
        dbService.save(userDataSet);
        return true;
    }

    public void addSessions(String sessionId, UserDataSet userDataSet) {
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

    public UserDataSet removeSessions(String sessionId) { return sessions.remove(sessionId); }

    public String getNumberOfOnlineUsers() { return String.valueOf(sessions.size()); }

    public String getNumberOfUsers() { return String.valueOf(users.size()); }
}