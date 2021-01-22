package casino.api.v1;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.util.Objects;

//@Introspected
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

//    public void setUsername(String username) {
//        this.username = username;
//    }

//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

//    public void setCredit(long credit) {
//        this.credit = credit;
//    }

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
