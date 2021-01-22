package casino.gateway;

import casino.api.v1.CardGame;
import casino.api.v1.Player;
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
import io.micronaut.tracing.annotation.SpanTag;
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

    public CardGameGateway(CardGameClient cgClient, SecurityService securityService) {
        this.cgClient = cgClient;
        this.securityService = securityService;
    }

    @Post("/")
    @NewSpan("gateway.cardgame.playGame")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<CardGame> playGame(@SpanTag("gateway.cardgame.playGame.player") @Body Player player) {
        logger.info("CardGameGateway:  playGame with player:  " + player.getPlayerId() + "  for wager:  " + player.getWager());
        if (securityService.getAuthentication().isPresent()) {
            UUID accountId = UUID.fromString(securityService.getAuthentication().get().getAttributes().get("id").toString());
            if (accountId.equals(player.getPlayerId())) {
                return cgClient.playGame(player).flatMap(endOfGame -> {
                    logger.info("CardGameGateway:  endOfGame isPlayerWin:  " + endOfGame.isPlayerWin());
                    if (endOfGame.isPlayerWin()) {
                        return cgClient.getGamePlayerWin(endOfGame.getGameId());
                    } else return cgClient.getGamePlayerLoose(endOfGame.getGameId());
                });
            } else {
                logger.info("PlayGame:  player:  " + player.getPlayerId() + "  is not Account owner");
                return Single.error(new AuthenticationException("PlayGame:  player:  " + player.getPlayerId() + "  is not Account owner"));
            }
        } else {
            logger.info("PlayGame:  player is not authenticated");
            return Single.error(new AuthenticationException("PlayGame:  player is not authenticated"));
        }

//        return cgClient.playGame(player).flatMap(endOfGame -> {
//            if (endOfGame.isPlayerWin()) return cgClient.getGamePlayerWin(endOfGame.getGameId());
//            else return cgClient.getGamePlayerLoose(endOfGame.getGameId());
//        });

    }
    @Post("/game")
    @NewSpan("gateway.cardgame.playGame")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<CardGame> play(@Body Player player) {
        if (securityService.getAuthentication().isPresent()) {
            UUID accountId = UUID.fromString(securityService.getAuthentication().get().getAttributes().get("id").toString());
            if (accountId.equals(player.getPlayerId())) {
                return cgClient.play(player);
            } else {
                logger.info("PlayGame:  player:  " + player.getPlayerId() + "  is not Account owner");
                return Single.error(new AuthenticationException("PlayGame:  player:  " + player.getPlayerId() + "  is not Account owner"));
            }
        } else {
            logger.info("PlayGame:  player is not authenticated");
            return Single.error(new AuthenticationException("PlayGame:  player is not authenticated"));
        }
    }
}
