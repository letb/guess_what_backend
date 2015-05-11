package resource;

import org.hibernate.cfg.Configuration;

public class DBSettings {
    private Configuration configuration;
    private String dialect;
    private String driverClass;
    private String url;
    private String name;
    private String password;
    private String show;
    private String auto;

    public DBSettings () {
    }

    public Configuration getConfiguration() {
        configuration = new Configuration();

        configuration.setProperty("hibernate.dialect", dialect);
        configuration.setProperty("hibernate.connection.driver_class", driverClass);
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.username", name);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.show_sql", show);
        configuration.setProperty("hibernate.hbm2ddl.auto", auto);

        return  configuration;
    }
}
