package casino.account.auth;

import casino.api.v1.UserLogin;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.event.LoginFailedEvent;
import io.micronaut.security.event.LoginSuccessfulEvent;
import io.micronaut.security.handlers.LoginHandler;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.Valid;

@Singleton
@Controller("/aplogin")
@Secured(SecurityRule.IS_ANONYMOUS)
@Validated
public class CustomLoginController {
    /**
     * Custom implementation of LoginController
     * for UsernamePasswordCredentials in Body of Request
     *
     * @param authenticator   Custom AuthenticationProvider for UsernamePasswordCredentials
     * @param eventPublisher   Publish LoginResult
     * @param loginHandler    Provided Loginhandler from Micronaut
     */


    private final LoginHandler loginHandler;
    private final ApplicationEventPublisher eventPublisher;
    private final CasinoAuthProviderUserPassword authenticator;
    private final Logger logger = LoggerFactory.getLogger(CustomLoginController.class);


    public CustomLoginController(LoginHandler loginHandler, ApplicationEventPublisher eventPublisher, CasinoAuthProviderUserPassword authenticator) {
        this.loginHandler = loginHandler;
        this.eventPublisher = eventPublisher;
        this.authenticator = authenticator;
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @Post()
    public Single<MutableHttpResponse<?>> login(@Valid @Body UserLogin userLogin, HttpRequest<?> request) {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userLogin.getUsername(), userLogin.getPassword());
        Flowable<AuthenticationResponse> responseFlowable = Flowable.fromPublisher(
                authenticator.authenticate(request, creds));
        logger.info("CustomLoginController:      login ");
        return responseFlowable.map(authResp -> {
            if (authResp.isAuthenticated() && authResp.getUserDetails().isPresent()) {
                CasinoUserDetails details = (CasinoUserDetails) authResp.getUserDetails().get();
                logger.info("CustomLoginController:      LoginSuccessful for User:   " + details.getUsername());
                eventPublisher.publishEvent(new LoginSuccessfulEvent(details));
                return loginHandler.loginSuccess(details, request);
            } else {
                logger.info("CustomLoginController:      LoginFailed ");
                eventPublisher.publishEvent(new LoginFailedEvent(authResp));
                return loginHandler.loginFailed(authResp, request);
            }
        })
                .first(HttpResponse.status(HttpStatus.UNAUTHORIZED));
    }

}
