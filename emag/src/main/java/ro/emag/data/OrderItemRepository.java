package ro.emag.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ro.emag.model.OrderItem;
import java.util.List;

public class OrderItemRepository {
    private final SessionFactory sessionFactory;

    public OrderItemRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<OrderItem> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<OrderItem> query = session.createQuery("FROM OrderItem", OrderItem.class);
            return query.list();
        }
    }

    public void save(OrderItem item) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(item);
            session.getTransaction().commit();
        }
    }

    public void delete(OrderItem item) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(item);
            session.getTransaction().commit();
        }
    }
}