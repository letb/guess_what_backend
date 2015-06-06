package dbService.dao;

import mechanics.GameMechanics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import user.dataSets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 27.04.15.
 */
public class UserDataSetDAO {
    static final Logger logger = LogManager.getLogger(UserDataSetDAO.class);
    private Session session;

    public UserDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(UserDataSet dataSet) {
        session.save(dataSet);
        session.close();
    }

    public UserDataSet readByLogin(String login) {
        Criteria criteria = session.createCriteria(UserDataSet.class);
        UserDataSet resultUserDataSet = new UserDataSet();

        try {
            resultUserDataSet = (UserDataSet) criteria.add(Restrictions.eq("name", login)).uniqueResult();
        } catch (HibernateException e) {
            logger.catching(e);
        }
        return resultUserDataSet;
    }

    public List getScoreboard() {
        Criteria criteria = session.createCriteria(UserDataSet.class);
        List resultList = new ArrayList<>();

        try {
            resultList = criteria.addOrder(Order.desc("score")).setMaxResults(10).list();
        } catch (HibernateException e) {
            logger.catching(e);
        }
        return resultList;
    }

    public void update(UserDataSet dataSet) {
        session.update(dataSet);
        session.flush();
        session.close();
    }
}
