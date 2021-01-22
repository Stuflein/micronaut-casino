package casino.api.v1;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
    private UUID userId;
    private String username;
    private String email;
    private long credit;

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }


    public long getCredit() {
        return credit;
    }

    public User() {
    }

    public User(UUID userId, String username, String email, long credit) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getCredit() == user.getCredit() &&
                getEmail() == user.getEmail() &&
                getUserId().equals(user.getUserId()) &&
                getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUsername(), getEmail(), getCredit());
    }
}
