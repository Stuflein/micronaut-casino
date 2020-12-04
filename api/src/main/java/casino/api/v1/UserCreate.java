package casino.api.v1;

import java.util.Objects;

public class UserCreate {
    private String username;
    private String email;
    private String password;
    private long credit;

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
        if (o == null || getClass() != o.getClass()) return false;
        UserCreate that = (UserCreate) o;
        return credit == that.credit &&
                getUsername().equals(that.getUsername()) &&
                getEmail().equals(that.getEmail()) &&
                getPassword().equals(that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getEmail(), getCredit());
    }
}
