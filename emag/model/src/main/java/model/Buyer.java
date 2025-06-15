package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("BUYER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Buyer extends User {

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    // opțional: relația cu orders, după cum ai nevoie
    @OneToMany(mappedBy = "buyer")
    private List<Order> orders;

    public Buyer() {  // Constructor fără parametri, necesar JPA
        super();
    }

    public Buyer(int id, String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(id, username, passwordHash, email, accountStatus);
    }

    public Buyer(String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(-1, username, passwordHash, email, accountStatus);
    }

    public Buyer(String username, String email, String passwordHash) {
        super(-1, username, passwordHash, email, AccountStatus.ACTIVE);
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}