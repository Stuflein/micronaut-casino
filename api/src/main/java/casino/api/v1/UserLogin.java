package casino.api.v1;

import io.micronaut.core.annotation.Introspected;

import java.io.Serializable;
import java.util.Objects;

@Introspected
public class UserLogin implements Serializable {



    private String username;

    private String password;


    public UserLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLogin userLogin = (UserLogin) o;
        return getUsername().equals(userLogin.getUsername()) &&
                getPassword().equals(userLogin.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword());
    }
}
