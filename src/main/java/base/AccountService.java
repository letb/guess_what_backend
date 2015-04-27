package base;

import base.dataSets.UserDataSet;

/**
 * Created by ivan on 29.03.15.
 */

public interface AccountService {
    boolean addUser(String userName, UserDataSet userDataSet);
    void addSessions(String sessionId, UserDataSet userDataSet);
    UserDataSet getUser(String userName);
    UserDataSet getSessions(String sessionId);
    UserDataSet removeSessions(String sessionId);
    String getNumberOfOnlineUsers();
    String getNumberOfUsers();
}
