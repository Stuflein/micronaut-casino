package casino.account.auth;

import org.reactivestreams.Publisher;

import java.util.List;

public interface CasinoAuthoritiesFetcher {
    Publisher<List<String>> findAuthoritiesByUsername(String username);
}
