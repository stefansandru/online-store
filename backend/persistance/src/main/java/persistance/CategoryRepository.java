package persistance;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import model.Category;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    private final SessionFactory sessionFactory;

    public CategoryRepository() {
        this.sessionFactory = persistance.utils.HibernateUtil.getSessionFactory();
    }

    public List<Category> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Category> query = session.createQuery("FROM Category", Category.class);
            return query.list();
        }
    }

    public void save(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(category);
            session.getTransaction().commit();
        }
    }

    public void delete(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(category);
            session.getTransaction().commit();
        }
    }

    public void edit(Category category) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(category);
            session.getTransaction().commit();
        }
    }

    public boolean existsByName(String categoryName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(c) FROM Category c WHERE c.name = :name", Long.class
            );
            query.setParameter("name", categoryName);
            return query.uniqueResult() > 0;
        }
    }

    public Category findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Category.class, id);
        }
    }
}
