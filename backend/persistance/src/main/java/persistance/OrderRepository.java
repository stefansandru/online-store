package persistance;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import model.Order;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final SessionFactory sessionFactory;

    public OrderRepository() {
        this.sessionFactory = persistance.utils.HibernateUtil.getSessionFactory();
    }

    public List<Order> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Order> query = session.createQuery("FROM Order", Order.class);
            return query.list();
        }
    }

    public void save(Order order) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(order);
            session.getTransaction().commit();
        }
    }

    public void delete(Order order) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(order);
            session.getTransaction().commit();
        }
    }
}
