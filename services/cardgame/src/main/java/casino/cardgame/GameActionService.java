package casino.cardgame;

import casino.api.v1.Card;
import casino.api.v1.Result;
import casino.cardgame.domain.Decks;
import casino.cardgame.domain.MongoCardGame;
import casino.cardgame.domain.MongoHand;
import casino.cardgame.game.InGameAction;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class GameActionService {

    private final InGameAction actionService;
    private final PersistenceService persistenceService;
    Logger logger = LoggerFactory.getLogger(GameActionService.class);

    public GameActionService(InGameAction actionService, PersistenceService persistenceService) {
        this.actionService = actionService;
        this.persistenceService = persistenceService;
    }

    public Single<MongoCardGame> startGame(UUID playerId, long wager) {
        logger.info("GameActionService: ");
        Decks decks = actionService.initCardGameDecks();
        return persistenceService.saveGameStart(playerId, wager).flatMap(game -> playGame(game.getGameId(), decks));
    }

    private Single<MongoCardGame> playGame(UUID gameId, Decks decks) {
        logger.info("GameActionService: playGame:  " + gameId);
        boolean isFinished = false;
        while (!isFinished) {
            actionService.checkAndShuffle(decks);
            persistenceService.save(gameId, playHand(decks), decks);
            isFinished = !actionService.checkIfGameOver(decks);
        }
        boolean playerWin = (decks.getPlayerDrawPile().size() + decks.getPlayerDiscardPile().size()) > 0;
        return persistenceService.endOfGamePersistence(gameId, playerWin);
    }

    private MongoHand playHand(Decks decks) {
        logger.info("GameActionService: play hand number:  " + decks.getHandPlayedInRoundNumber() + "    in round:   " + decks.getRoundNumber());
        int handPlayed = decks.getHandPlayedInRoundNumber() + 1;
        decks.setHandPlayedInRoundNumber(handPlayed);

        Card playerCard = decks.getPlayerDrawPile().remove(0);
        Card botCard = decks.getBotDrawPile().remove(0);
        MongoHand hand;
        Result result = actionService.compareCardsForSwitch(playerCard, botCard);
        if (result == Result.PLAYER_WIN) {
            actionService.changeCards(playerCard, botCard, decks, true);
            hand = new MongoHand(decks.getRoundNumber(), handPlayed, Result.PLAYER_WIN, playerCard, botCard, UUID.randomUUID());
        } else if (result == Result.BOT_WIN) {
            actionService.changeCards(playerCard, botCard, decks, false);
            hand = new MongoHand(decks.getRoundNumber(), handPlayed, Result.BOT_WIN, playerCard, botCard, UUID.randomUUID());
//
        } else {
            decks.getDrawPile().add(playerCard);
            decks.getDrawPile().add(botCard);
            actionService.checkIfEnoughRestCards(decks);
            hand = new MongoHand(decks.getRoundNumber(), handPlayed, Result.DRAW, playerCard, botCard, UUID.randomUUID());
        }
        return hand;
    }

}
