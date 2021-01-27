package casino.gateway;

import casino.api.v1.CardGame;
import casino.api.v1.GameCreate;
import casino.client.v1.CardGameClient;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.tracing.annotation.NewSpan;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("api/cardgame")
public class CardGameGateway {

    private final CardGameClient cgClient;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(CardGameGateway.class);

    /**
     * @param cgClient        client for cardgame module
     * @param securityService Micronaut provided
     */
    public CardGameGateway(CardGameClient cgClient, SecurityService securityService) {
        this.cgClient = cgClient;
        this.securityService = securityService;
    }

    @Post("/game")
    @NewSpan("gateway.cardgame.playGame")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<CardGame> play(@Body GameCreate gameCreate) {

        UUID accountId = UUID.fromString(securityService.getAuthentication().orElseThrow(() -> new AuthenticationException("PlayGame:  player is not authenticated"))
                .getAttributes().get("id").toString());
        if (accountId.equals(gameCreate.getPlayerId())) {
            return cgClient.playGame(gameCreate);
        } else {
            logger.info("PlayGame:  player:  {}    is not Account owner", gameCreate.getPlayerId());
            return Single.error(new AuthenticationException("PlayGame:  player:  " + gameCreate.getPlayerId() + "  is not Account owner"));
        }
    }
}
