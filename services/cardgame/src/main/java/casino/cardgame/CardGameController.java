package casino.cardgame;

import casino.api.v1.CardGame;
import casino.api.v1.GameCreate;
import casino.cardgame.domain.MongoCardGame;
import casino.cardgame.exceptions.NotEnoughFundsException;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller("/casino/cardgame")
@Singleton
public class CardGameController {

    private final CardGameService cardgameService;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(CardGameController.class);

    public CardGameController(CardGameService cardgameService, SecurityService securityService) {
        this.cardgameService = cardgameService;
        this.securityService = securityService;
    }


    @Post("/game")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @ContinueSpan
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<CardGame> play(@Body GameCreate gameCreate) {
        logger.info("CardGameController play at  {} ", LocalDateTime.now());
        UUID accountId = UUID.fromString(securityService.getAuthentication().orElseThrow(() -> new AuthenticationException("Player is not authenticated")).getAttributes().get("id").toString());
        if (accountId.equals(gameCreate.getPlayerId())) {
            return cardgameService.wagerToAccountCredit(gameCreate.getPlayerId(), gameCreate.getWager())
                    .flatMap((Boolean enoughFunds) -> {
                        if (Boolean.TRUE.equals(enoughFunds)) {
                            logger.info("CardgameController:  playGame with player:  {}  game starts", gameCreate.getPlayerId());
                            return cardgameService.startGame(gameCreate).map(MongoCardGame::toApiCardGame);
                        } else {
                            logger.info("CardgameController:  playGame with player:  {}  player has not enough credit for wager:   {}", gameCreate.getPlayerId(), gameCreate.getWager());
                            return Single.error(new NotEnoughFundsException("User: " + gameCreate.getPlayerId() + "   , not enough credit for wager:   " + gameCreate.getWager(), gameCreate.getWager()));
                        }
                    });
        } else {
            logger.info("CardgameController:  playGame with player:  {}  player is not this user", gameCreate.getPlayerId());
            return Single.error(new AuthenticationException("Play game with player :  " + gameCreate.getPlayerId() + " is not this user"));
        }
    }
}

