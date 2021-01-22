package casino.account.auth;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.config.SecurityConfigurationProperties;
import io.micronaut.security.handlers.LoginHandler;
import io.micronaut.security.token.jwt.bearer.AccessRefreshTokenLoginHandler;
import io.micronaut.security.token.jwt.generator.AccessRefreshTokenGenerator;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;

import javax.inject.Singleton;
import java.util.Optional;


    /**
     * Implementation of {@link LoginHandler} for Token Based Authentication.
     *
     * @author Sergio del Amo
     * @since 1.0
//     */
//    @Requires(property = SecurityConfigurationProperties.PREFIX + ".authentication", value = "bearer")
//    @Singleton
//    public class CustomAccessRefreshTokenLoginHandler extends AccessRefreshTokenLoginHandler {
//
//        /**
//         * @param accessRefreshTokenGenerator AccessRefresh Token generator
//         */
//
//        public CustomAccessRefreshTokenLoginHandler(AccessRefreshTokenGenerator accessRefreshTokenGenerator) {
//            super(accessRefreshTokenGenerator);
//        }
//
//        @Override
//        public MutableHttpResponse<?> loginSuccess(UserDetails userDetails, HttpRequest<?> request) {
//            if (userDetails instanceof CasinoUserDetails){
//                Optional<AccessRefreshToken> accessRefreshTokenOptional = accessRefreshTokenGenerator.generate(userDetails);
//            }
//            Optional<AccessRefreshToken> accessRefreshTokenOptional = accessRefreshTokenGenerator.generate(userDetails);
//            if (accessRefreshTokenOptional.isPresent()) {
//                return HttpResponse.ok(accessRefreshTokenOptional.get());
//            }
//            return HttpResponse.serverError();
//        }
//
//        @Override
//        public MutableHttpResponse<?> loginRefresh(UserDetails userDetails, String refreshToken, HttpRequest<?> request) {
//            Optional<AccessRefreshToken> accessRefreshToken = accessRefreshTokenGenerator.generate(refreshToken, userDetails);
//            if (accessRefreshToken.isPresent()) {
//                return HttpResponse.ok(accessRefreshToken.get());
//            }
//            return HttpResponse.serverError();
//        }
//
//        @Override
//        public MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
//            throw new AuthenticationException(authenticationFailed.getMessage().orElse(null));
//        }
//    }
//
//}
