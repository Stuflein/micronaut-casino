package casino.account.auth;

import com.nimbusds.jwt.JWTClaimsSet;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.config.TokenConfiguration;
import io.micronaut.security.token.jwt.generator.claims.ClaimsAudienceProvider;
import io.micronaut.security.token.jwt.generator.claims.JWTClaimsSetGenerator;
import io.micronaut.security.token.jwt.generator.claims.JwtIdGenerator;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Replaces(bean = JWTClaimsSetGenerator.class)
public class CustomJWTClaimSetGenerator extends JWTClaimsSetGenerator {

    /**
     * Custom ClaimSet to include userId in JWT
     * so it can be read by securityService for validating User is registered
     * and easy propagation for later Usage in Services
     */


    @Inject
    public CustomJWTClaimSetGenerator(TokenConfiguration tokenConfiguration,
                                      @Nullable JwtIdGenerator jwtIdGenerator,
                                      @Nullable ClaimsAudienceProvider claimsAudienceProvider,
                                      @Nullable ApplicationConfiguration applicationConfiguration) {
        super(tokenConfiguration, jwtIdGenerator, claimsAudienceProvider, applicationConfiguration);
    }

    /**
     * Populates Claims with UserDetails object,
     * if UserDetails is CasinoUserDetail id is
     * added to JWT claim
     *
     * @param builder     the Claims Builder
     * @param userDetails Authenticated user's representation.
     */
    @Override
    protected void populateWithUserDetails(JWTClaimsSet.Builder builder, UserDetails userDetails) {
        super.populateWithUserDetails(builder, userDetails);
        if (userDetails instanceof CasinoUserDetails) {
            builder.claim("id", ((CasinoUserDetails) userDetails).getUserId().toString());
        }
    }
}
