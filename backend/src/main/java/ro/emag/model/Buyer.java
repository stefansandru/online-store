package ro.emag.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("BUYER")
public class Buyer extends User {

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

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

    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}