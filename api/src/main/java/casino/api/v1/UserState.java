package casino.api.v1;

public interface UserState {
    String getUsername();

    String getPassword();

    boolean isEnabled();

    boolean isAccountExpired();

    boolean isAccountLocked();

    boolean isPasswordExpired();
}
