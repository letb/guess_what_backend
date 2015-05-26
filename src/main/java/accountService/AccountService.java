package accountService;

import user.dataSets.UserDataSet;

/**
 * Created by ivan on 29.03.15.
 */

public interface AccountService {
    boolean addUser(String userName, UserDataSet userDataSet);

    void addSession(String sessionId, UserDataSet userDataSet);

    UserDataSet getUser(String userName);

    String getUserName(String sessionId);

    UserDataSet getSessions(String sessionId);

    UserDataSet removeSessions(String sessionId);

    String getNumberOfOnlineUsers();

    String getNumberOfUsers();

    UserDataSet[] getScoreboard();
}
