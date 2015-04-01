package base;

import main.UserProfile;

/**
 * Created by ivan on 29.03.15.
 */
public interface AccountService {
    boolean addUser(String userName, UserProfile userProfile);
    void addSessions(String sessionId, UserProfile userProfile);
    UserProfile getUser(String userName);
    UserProfile getSessions(String sessionId);
    UserProfile removeSessions(String sessionId);
    String getNumberOfOnlineUsers();
    String getNumberOfUsers();
}
