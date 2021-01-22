package casino.account.auth;

import casino.api.v1.UserState;
import org.reactivestreams.Publisher;

import javax.validation.constraints.NotNull;

public interface CasinoUserFetcher {
    Publisher<UserState> findByUsername(@NotNull String username);
}
