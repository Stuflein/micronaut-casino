package casino.account.auth;

import casino.account.domain.Account;
import casino.api.v1.UserState;

public class AccountUserState implements UserState {
    /**
     * Get the AccountUserState for Security
     *
     *  account  The Account to get the AccountUserState for
     */

    private final Account account;

    public AccountUserState(Account account) {
        this.account = account;
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }

    @Override
    public boolean isAccountExpired() {
        return account.isAccountExpired();
    }

    @Override
    public boolean isAccountLocked() {
        return account.isAccountLocked();
    }

    @Override
    public boolean isPasswordExpired() {
        return account.isPasswordExpired();
    }

    public Account getAccount() {
        return account;
    }
}
