package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SELLER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Seller extends User {

    public Seller() {
        super();
    }

    public Seller(int id, String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(id, username, passwordHash, email, accountStatus);
    }

    public Seller(String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(-1, username, passwordHash, email, accountStatus);
    }

    public Seller(String username, String email, String passwordHash) {
        super(-1, username, passwordHash, email, AccountStatus.ACTIVE);
    }
}
