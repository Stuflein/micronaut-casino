package casino.account.domain;

import casino.account.security.UserState;
import casino.api.v1.User;
import casino.api.v1.UserRole;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Account implements Serializable, UserState {
    private UUID id;
    private String username;
    private String email;
    private long credit;
    private @Nullable String password;
    private boolean enabled;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean passwordExpired;
    private long last_login;
    private long created_at;
    private List<UserRole> role;


    public Account(UUID id, String username, String email, long credit, @Nullable String password, boolean enabled, boolean accountExpired, boolean accountLocked, boolean passwordExpired, long last_login, long created_at, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.credit = credit;
        this.password = password;
        this.enabled = enabled;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.passwordExpired = passwordExpired;
        this.last_login = last_login;
        this.created_at = created_at;
        this.role = new ArrayList<UserRole>(Collections.singleton(role));
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public long getGuthaben() {
        return credit;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public long getLast_login() {
        return last_login;
    }

    public long getCreated_at() {
        return created_at;
    }

    public UserRole getRole() {
        return role.get(0);
    }

   public User getApiUser(){
        return new User(this.getId(), this.getUsername(), this.getEmail(), this.getGuthaben());
   }
}
