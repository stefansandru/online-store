package model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MODERATOR")
public class Moderator extends User {

    public Moderator() {
        super();
    }

    public Moderator(int id, String username, String passwordHash, String email, AccountStatus accountStatus) {
        super(id, username, passwordHash, email, accountStatus);
    }

    public Moderator(String username, String email, String passwordHash) {
        super(0, username, passwordHash, email, AccountStatus.ACTIVE);
    }
}
