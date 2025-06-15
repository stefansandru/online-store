package ro.emag.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MODERATOR")
public class Moderator extends User {

    public Moderator() {
        super();
    }

    public Moderator(int id, String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(id, username, passwordHash, email, accountStatus);
    }

}
