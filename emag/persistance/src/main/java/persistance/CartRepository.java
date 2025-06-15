package persistance;

import model.Buyer;
import model.CartItem;

import model.Product;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;


import java.util.List;

public class CartRepository {
    // This class will handle cart-related operations such as adding items, removing items, and checking out.
    // It will interact with the database to manage the buyer's cart.

    private final SessionFactory sessionFactory;

    public CartRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Methods for adding, removing, and checking out items

     public void addProductToCart(CartItem cartItem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(cartItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
     }

    public List<CartItem> getCartItems(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<CartItem> query = session.createQuery("FROM CartItem WHERE buyer.id = :buyerId", CartItem.class);
            query.setParameter("buyerId", id);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeProductFromCart(Buyer buyer, int productId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<CartItem> query = session.createQuery("FROM CartItem WHERE buyer.id = :buyerId AND product.id = :productId", CartItem.class);
            query.setParameter("buyerId", buyer.getId());
            query.setParameter("productId", productId);
            CartItem cartItem = query.uniqueResult();
            if (cartItem != null) {
                session.remove(cartItem);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CartItem getCartItem(Buyer buyer, int productId) {
        try (Session session = sessionFactory.openSession()) {
            Query<CartItem> query = session.createQuery("FROM CartItem WHERE buyer.id = :buyerId AND product.id = :productId", CartItem.class);
            query.setParameter("buyerId", buyer.getId());
            query.setParameter("productId", productId);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void modifyCartItem(CartItem isExistentCartItem) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(isExistentCartItem);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCart(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<?> query = session.createQuery("DELETE FROM CartItem WHERE buyer.id = :buyerId");
            query.setParameter("buyerId", id);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
