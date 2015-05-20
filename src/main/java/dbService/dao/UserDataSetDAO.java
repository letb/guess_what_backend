package dbService.dao;

import base.dataSets.UserDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by ivan on 27.04.15.
 */
public class UserDataSetDAO {
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
        return (UserDataSet) criteria.add(Restrictions.eq("name", login)).uniqueResult();
    }

    public List getScoreboard() {
        Criteria criteria = session.createCriteria(UserDataSet.class);
        return criteria.addOrder(Order.desc("score")).setMaxResults(10).list();
    }
}
