package persistance;

import model.Seller;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import model.User;
import persistance.utils.HibernateUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public UserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User WHERE username = :uname", User.class
            );
            query.setParameter("uname", username);
            return query.uniqueResult();
        }
    }

    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<String> getAllUsernames() {
        try (Session session = sessionFactory.openSession()) {
            Query<String> query = session.createQuery("SELECT u.username FROM User u", String.class);
            return query.getResultList();
        }
    }

    public List<Seller> getAllSellers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Seller> query = session.createQuery("FROM Seller", Seller.class);
            return query.list();
        }
    }

    public Seller findSellerById(int sellerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Seller> query = session.createQuery("FROM Seller WHERE id = :id", Seller.class);
            query.setParameter("id", sellerId);
            return query.uniqueResult();
        }
    }

    public void edit(Seller seller) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(seller);
            session.getTransaction().commit();
        }
    }
}