package casino.api.v1;

import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID userId;
    private String username;
    private long credit;

    public UUID getUserId() {
        return userId;
    }


    public String getUsername() {
        return username;
    }


    public long getCredit() {
        return credit;
    }


    public User(UUID userId, String username, long credit) {
        this.userId = userId;
        this.username = username;
        this.credit = credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getCredit() == user.getCredit() &&
                getUserId().equals(user.getUserId()) &&
                getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUsername(), getCredit());
    }
}
