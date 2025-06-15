package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // buyer_id face referire la un User cu rol Buyer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Presupun relație cu OrderItem, nu există în tabel dar se păstrează dacă folosești
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    public Order() {}

    public Order(int id, Buyer buyer, LocalDateTime createdAt, List<OrderItem> items) {
        this.id = id;
        this.buyer = buyer;
        this.createdAt = createdAt;
        this.items = items;
    }

    // Getters și setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Buyer getBuyer() { return buyer; }
    public void setBuyer(Buyer buyer) { this.buyer = buyer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}