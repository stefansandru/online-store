package ro.emag.model;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false, unique = true)
    private Buyer owner;

    public Cart() {}

    public Cart(Buyer owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public Buyer getOwner() {
        return owner;
    }

    public void setOwner(Buyer owner) {
        this.owner = owner;
    }

}