package casino.account.auth;

import casino.account.domain.Account;
import casino.api.v1.UserState;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class CasinoAuthProviderUserPassword implements AuthenticationProvider {
    /**
     * AuthenticationProvider for UsernamePasswordCredentials
     *
     * @param passwordEncoder  Match provided password to password in DB
     * @param authoritiesFetcher  Get CasinoUserRoles for Account name
     * @param userFetcher  Get Account for username
     */


    private final Logger logger = LoggerFactory.getLogger(CasinoAuthProviderUserPassword.class);

    private final PasswordEncoder passwordEncoder;
    private final CasinoAuthoritiesFetcher authoritiesFetcher;
    private final CasinoUserFetcher userFetcher;

    public CasinoAuthProviderUserPassword(PasswordEncoder passwordEncoder, CasinoAuthoritiesFetcher authoritiesFetcher, CasinoUserFetcher userFetcher) {
        this.passwordEncoder = passwordEncoder;
        this.authoritiesFetcher = authoritiesFetcher;
        this.userFetcher = userFetcher;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        final String username = authenticationRequest.getIdentity().toString();
        logger.info("CasinoAuthProviderUserPassword:   authenticate user:   " + username);
        Flowable<UserState> userStateFlowable = Flowable.fromPublisher(userFetcher.findByUsername(username));
        return Flowable.fromPublisher(userStateFlowable
                .switchMap(user -> {
                    Optional<AuthenticationFailed> authFailed = validate(user, authenticationRequest);
                    if (authFailed.isPresent()) {
                        logger.info("CasinoAuthProviderUserPassword:    authenticate() user: " + username + "  : authFailed");
                        return Flowable.just(authFailed.get());
                    } else {
                        logger.info("CasinoAuthProviderUserPassword:    authenticate()  user:  " + username + "  :  authSuccess");
                        return createSuccessfulAuthenticationResponse(authenticationRequest, user);
                    }
                }));
    }

    private Optional<AuthenticationFailed> validate(UserState user, AuthenticationRequest<?, ?> authenticationRequest) {

        AuthenticationFailed authenticationFailed = null;
        if (user == null) {
            logger.info("user == null");
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND);

        } else if (!user.isEnabled()) {
            logger.info("user not enabled:    " + user.getUsername());
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.USER_DISABLED);

        } else if (user.isAccountExpired()) {
            logger.info("Account is expired:    " + user.getUsername());
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_EXPIRED);

        } else if (user.isAccountLocked()) {
            logger.info("Account is locked:   " + user.getUsername());
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.ACCOUNT_LOCKED);

        } else if (user.isPasswordExpired()) {
            logger.info("Password is expired:     " + user.getUsername());
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.PASSWORD_EXPIRED);

        } else if (!passwordEncoder.matches(authenticationRequest.getSecret().toString(), user.getPassword())) {
            logger.info("Password doesnt match:    " + user.getUsername());
            authenticationFailed = new AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
        return Optional.ofNullable(authenticationFailed);
    }

    private Publisher<AuthenticationResponse> createSuccessfulAuthenticationResponse(AuthenticationRequest<?, ?> authenticationRequest, UserState user) {
        logger.info("CasinoAuthProviderUserPassword:   createSuccessfulAuthenticationResponse");
        return Flowable.fromPublisher(authoritiesFetcher.findAuthoritiesByUsername(user.getUsername()))
                .map(authoritiesList -> {
                    Account account = ((AccountUserState) user).getAccount();
                    logger.info("CasinoAuthProviderUserPassword:   " + account.getId());
                    return new CasinoUserDetails(user.getUsername(), authoritiesList, account.getId());
                });
    }
}
