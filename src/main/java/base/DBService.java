package base;

import base.dataSets.UserDataSet;

/**
 * Created by ivan on 27.04.15.
 */
public interface DBService {
    void save(UserDataSet dataSet);

    UserDataSet readByLogin(String login);
}
