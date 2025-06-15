package ro.emag.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ro.emag.model.Shop;
import java.util.List;

public class ShopRepository {
    private final SessionFactory sessionFactory;

    public ShopRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Shop> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Shop> query = session.createQuery("FROM Shop", Shop.class);
            return query.list();
        }
    }

    public void save(Shop shop) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(shop);
            session.getTransaction().commit();
        }
    }

    public void delete(Shop shop) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(shop);
            session.getTransaction().commit();
        }
    }
}