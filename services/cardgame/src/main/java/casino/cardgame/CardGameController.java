package casino.cardgame;

import casino.api.v1.CardGame;
import casino.api.v1.Player;
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

    public CardGameController(
            GameActionService gameActionService, CardGameService cardgameService, SecurityService securityService) {
        this.cardgameService = cardgameService;
        this.securityService = securityService;
    }


    @Post("/shitty")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @ContinueSpan
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<CardGame> play(@Body Player player) {
        logger.info("CardGameController play at  + " + LocalDateTime.now());
        if (securityService.getAuthentication().isPresent()) {
            UUID accountId = UUID.fromString(securityService.getAuthentication().get().getAttributes().get("id").toString());
            if (accountId.equals(player.getPlayerId())) {
                return cardgameService.wagerToAccountCredit(player.getPlayerId(), player.getWager()).flatMap(success -> {
                    if (success) {
                        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  game starts");
                        return cardgameService.startGame(accountId, player.getWager()).map(MongoCardGame::toApiCardGame);
                    } else {
                        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player has not enough credit for wager:  " + player.getWager());
                        return Single.error(new NotEnoughFundsException("User: " + player.getPlayerId() + "   , not enough credit for wager:   " + player.getWager(), player.getWager()));
                    }
                });
            } else {
                logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player is not this user");
                return Single.error(new AuthenticationException("Play game with player :  " + player.getPlayerId() + " is not this user"));
            }
        } else {
            logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player is not authenticated");
            return Single.error(new AuthenticationException("Player is not authenticated"));
        }
    }
}


    //    @Post("/game")
//    @Secured(SecurityRule.IS_AUTHENTICATED)
//    @ContinueSpan
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Single<CardGame> play(@Body Player player){
//        if (securityService.getAuthentication().isPresent()) {
//            UUID accountId = UUID.fromString(securityService.getAuthentication().get().getAttributes().get("id").toString());
//            if (accountId.equals(player.getPlayerId())) {
//                return cardgameService.wagerToAccountCredit(player.getPlayerId(), player.getWager()).flatMap(success -> {
//                    if (success) {
//                        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId()+ "  game starts");
//                        return shitty.startGame(accountId, player.getWager()).map(MongoCardGame::toApiCardGame);
//                    } else {
//                        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player has not enough credit for wager:  " + player.getWager());
//                        return Single.error(new NotEnoughFundsException("User: " + player.getPlayerId() + "   , not enough credit for wager:   " + player.getWager(), player.getWager()));
//                    }
//                });
//            } else {
//                logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player is not this user");
//                return Single.error(new AuthenticationException("Play game with player :  " + player.getPlayerId() + " is not this user"));
//            }
//        } else {
//            logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player is not authenticated");
//            return Single.error(new AuthenticationException("Player is not authenticated"));
//        }
//    }
//    @Post("/")
//    @Secured(SecurityRule.IS_AUTHENTICATED)
//    @ContinueSpan
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Single<EndOfGame> playGame(@SpanTag("gateway.cardgame.playGame.player") @Body Player player) {
//        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId());
//        if (securityService.getAuthentication().isPresent()) {
//            UUID accountId = UUID.fromString(securityService.getAuthentication().get().getAttributes().get("id").toString());
//            if (accountId.equals(player.getPlayerId())) {
//                return cardgameService.wagerToAccountCredit(player.getPlayerId(), player.getWager()).flatMap(success -> {
//                    if (success) {
//                        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId()+ "  game starts");
//                        return controller.startGame(accountId, player.getWager());
//                    } else {
//                        logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player has not enough credit for wager:  " + player.getWager());
//                        return Single.error(new NotEnoughFundsException("User: " + player.getPlayerId() + "   , not enough credit for wager:   " + player.getWager(), player.getWager()));
//                    }
//                });
//            } else {
//                logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player is not this user");
//                return Single.error(new AuthenticationException("Play game with player :  " + player.getPlayerId() + " is not this user"));
//            }
//        } else {
//            logger.info("CardgameController:  playGame with player:  " + player.getPlayerId() + "  player is not authenticated");
//            return Single.error(new AuthenticationException("Player is not authenticated"));
//        }
//    }
//
//    @Get("/{id}/win")
//    @ContinueSpan
//    @Produces(MediaType.APPLICATION_JSON)
//    @Secured(SecurityRule.IS_ANONYMOUS)
//    public Single<CardGame> getGamePlayerWin(@SpanTag("gateway.cardgame.playGame.player") @PathVariable UUID id) {
//        logger.info("CardgameController: getGamePlayerWin  game:  " + id + "  at:  " + LocalDateTime.now());
//        return cardgameService.getCardGame(id);
//
//    }
//
//    @Get("/{id}/loose")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Secured(SecurityRule.IS_ANONYMOUS)
//    public Single<CardGame> getGamePlayerLoose(@SpanTag("gateway.cardgame.playGame.player") @PathVariable UUID id) {
//        logger.info("CardgameController: getGamePlayerLoose  game: " + id + "  at:  " + LocalDateTime.now());
//        return cardgameService.getCardGame(id);
//
//    }
//}
