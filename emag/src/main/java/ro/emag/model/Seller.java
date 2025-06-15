package ro.emag.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SELLER")
public class Seller extends User{

    public Seller() {
        super();
    }

    public Seller(int id, String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(id, username, passwordHash, email, accountStatus);
    }

    public Seller(String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(-1, username, passwordHash, email, accountStatus);
    }
}
