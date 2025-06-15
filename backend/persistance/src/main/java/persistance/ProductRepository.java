package persistance;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import model.Product;
import model.User;
import persistance.utils.HibernateUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    private final SessionFactory sessionFactory;

    public ProductRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Product> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery(
                    "FROM Product p WHERE p.seller.accountStatus <> 'BLOCKED'",
                    Product.class
            );
            return query.list();
        }
    }

    public List<Product> getProductsBySeller(User seller) {
        try (Session session = sessionFactory.openSession()) {
            Query<Product> query = session.createQuery(
                    "FROM Product p WHERE p.seller = :seller AND p.seller.accountStatus <> 'BLOCKED'",
                    Product.class
            );
            query.setParameter("seller", seller);
            return query.list();
        }
    }

    public void save(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(product);
            session.getTransaction().commit();
        }
    }

    public void delete(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(product);
            session.getTransaction().commit();
        }
    }

    public void edit(Product product) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(product);
            session.getTransaction().commit();
        }
    }

    public Product findById(int productId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Product.class, productId);
        }
    }
}
