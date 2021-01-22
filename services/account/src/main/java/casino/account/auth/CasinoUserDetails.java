package casino.account.auth;

import io.micronaut.security.authentication.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class CasinoUserDetails extends UserDetails {
    private final UUID userId;

    public CasinoUserDetails(String username, Collection<String> roles, UUID userId) {
        super(username, roles);
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
