package casino.account.auth;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.jwt.encryption.secret.SecretEncryptionConfiguration;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;
import io.micronaut.security.token.jwt.render.TokenRenderer;

import javax.inject.Singleton;
/*
* um as ganze custom zu gestalten:
* CustomAccessRefreshToken bauen, da einen wert rein
* public class CustomBearerAccessRefreshToken extends BearerAccessRefreshToken {
    private String meinWert;
    public CustomBearerAccessRefreshToken() {
    }
    public CustomBearerAccessRefreshToken(String username,
                                    Collection<String> roles,
                                    Integer expiresIn,
                                    String accessToken,
                                    String refreshToken,
                                    String tokenType,
                                          UUID meinWert) {
        super(username, roles, expiresIn, accessToken, refreshToken);
        this.meinWert = meinWert;

    }

    public String getMeinWert() {
        return meinWert;
    }

    public void setMeinWert(String avatar) {
        this.meinWert = meinWert;
    }
}
* Und dann
*@Singleton
@Replaces(bean = BearerTokenRenderer.class)
public class CustomBearerTokenRenderer extends BearerTokenRenderer {

    private final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

    @Override
    public AccessRefreshToken render(Integer expiresIn, String accessToken, @Nullable String refreshToken) {

        return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn);
    }

    @Override
    public AccessRefreshToken render(UserDetails userDetails, Integer expiresIn, String accessToken, @Nullable String refreshToken) {
        if (userDetails instanceof CasinoUserDetails){
            return new CustomBearerAccessRefreshToken(userDetails.getUsername(), userDetails.getRoles(), expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE, ((CasinoUserDetails)userDetails).getUserId());
        }
        return new BearerAccessRefreshToken(userDetails.getUsername(), userDetails.getRoles(), expiresIn, accessToken, refreshToken, BEARER_TOKEN_TYPE);
    }
}
*
*
*
* */