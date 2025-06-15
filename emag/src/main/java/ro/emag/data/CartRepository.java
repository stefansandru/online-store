package ro.emag.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ro.emag.model.Cart;
import java.util.List;

public class CartRepository {
    private final SessionFactory sessionFactory;

    public CartRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Cart> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Cart> query = session.createQuery("FROM Cart", Cart.class);
            return query.list();
        }
    }

    public void save(Cart cart) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(cart);
            session.getTransaction().commit();
        }
    }

    public void delete(Cart cart) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(cart);
            session.getTransaction().commit();
        }
    }
}