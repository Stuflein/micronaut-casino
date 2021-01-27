package casino.api.v1;

import java.io.Serializable;
import java.util.Objects;


public class UserCreate implements Serializable {

    private String username;
    private String email;
    private String password;
    private long credit;


    public UserCreate() {
    }

    public UserCreate(String username, String email, String password, long credit) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.credit = credit;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getCredit() {
        return credit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCreate)) return false;
        UserCreate that = (UserCreate) o;
        return getCredit() == that.getCredit() &&
                getUsername().equals(that.getUsername()) &&
                getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmail(), getCredit());
    }
}
