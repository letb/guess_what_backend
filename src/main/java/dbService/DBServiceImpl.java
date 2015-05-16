package dbService;

import base.DBService;
import base.dataSets.UserDataSet;
import dbService.dao.UserDataSetDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import resource.DBSettings;
import resource.ResourceFactory;

/**
 * Created by ivan on 27.04.15.
 */
public class DBServiceImpl implements DBService {
   private SessionFactory sessionFactory;
   static final Logger logger = LogManager.getLogger(DBServiceImpl.class);

    public DBServiceImpl() {
        ResourceFactory resourceFactory = ResourceFactory.instance();
        DBSettings dbSettings = (DBSettings)resourceFactory.getResource("dbSettings");
        if(dbSettings == null) {
            logger.info("No db settings. I give up");
            System.exit(3);
        }
        Configuration configuration = dbSettings.getConfiguration();
        configuration.addAnnotatedClass(UserDataSet.class);

        logger.info("Database service configured");
        sessionFactory = createSessionFactory(configuration);
    }

    public void save(UserDataSet dataSet) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        dao.save(dataSet);
        transaction.commit();
        logger.info("Session created");
    }

    public UserDataSet readByLogin(String login) {
        Session session = sessionFactory.openSession();
        UserDataSetDAO dao = new UserDataSetDAO(session);
        return dao.readByLogin(login);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
