package database;

import dbService.DBService;
import user.dataSets.UserDataSet;
import dbService.DBServiceImpl;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class DBServiceImplTest {
    DBService dbService;

    @Before
    public void configurateDB() throws Exception {
        Configuration configuration = new Configuration();

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:java_blackjack_test");
        configuration.setProperty("hibernate.connection.username", "");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        dbService = new DBServiceImpl(configuration);
    }

    @Test
    public void saveUserAndGetHimTest() {
        UserDataSet user = new UserDataSet("first","123","first@first");
        dbService.save(user);
        UserDataSet userFromDB = dbService.readByLogin("first");
        assertEquals(user.getJson(), userFromDB.getJson());
    }

    @Test
    public void getScoreboardTest() {
        for (int i = 0; i < 12; i++) {
            dbService.save(new UserDataSet("user" + i, "1", "mail@mail"));
        }
        List users = dbService.getScoreboard();
        assertEquals(10, users.size());
    }
}
