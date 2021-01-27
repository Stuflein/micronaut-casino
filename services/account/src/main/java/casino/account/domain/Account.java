package casino.account.domain;

import casino.account.auth.AccountUserState;
import casino.api.v1.User;
import casino.api.v1.UserRole;
import casino.api.v1.UserState;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Account implements Serializable {
    private final UUID id;
    private final String username;
    private final String email;
    private final long credit;
    @Nullable
    private String password;
    private final boolean enabled;
    private final boolean accountExpired;
    private final boolean accountLocked;
    private final boolean passwordExpired;
    private final long lastLogin;
    private final long createdAt;
    private final List<UserRole> role;


    public Account(UUID id, String username,
                   String email, long credit,
                   @Nullable String password, boolean enabled,
                   boolean accountExpired, boolean accountLocked,
                   boolean passwordExpired, long lastLogin,
                   long createdAt, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.credit = credit;
        this.password = password;
        this.enabled = enabled;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.passwordExpired = passwordExpired;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.role = new ArrayList<>(Collections.singleton(role));
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

    public long getCredit() {
        return credit;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    public UserState getUserState() {
        return new AccountUserState(this);
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

    public long getLastLogin() {
        return lastLogin;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public UserRole getRole() {
        return role.get(0);
    }

    public User getApiUser() {
        return new User(this.getId(), this.getUsername(), this.getEmail(), this.getCredit());
    }
}
