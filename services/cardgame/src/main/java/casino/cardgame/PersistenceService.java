package casino.cardgame;

import casino.cardgame.dao.InMemo;
import casino.cardgame.dao.MongoDao;
import casino.cardgame.domain.Decks;
import casino.cardgame.domain.MongoCardGame;
import casino.cardgame.domain.MongoDecks;
import casino.cardgame.domain.MongoHand;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class PersistenceService {
    @Inject
    InMemo inMemoRepo;
    @Inject
    MongoDao dao;

    Logger logger = LoggerFactory.getLogger(PersistenceService.class);

   public Single<MongoCardGame> saveGameStart(UUID playerId, long wager) {
       logger.info("PersistenceService:  saveGameStart for user:  " + playerId);
        return inMemoRepo.save(new MongoCardGame(playerId, wager));
    }

    public void save(UUID gameId, MongoHand hand, Decks decks) {
        logger.info("PersistenceService:  save hand:  " + hand.getHandId()+ "   in game:  " + gameId);
        inMemoRepo.save(gameId, hand, new MongoDecks(decks, hand.getHandId()));
    }

    public Single<MongoCardGame> endOfGamePersistence(UUID gameId, boolean playerWin) {
        logger.info("PersistenceService:  endOfGamePersistence for game:  " + gameId+ "   player won:  " + playerWin);
        return inMemoRepo.saveEndOfGame(gameId, playerWin).flatMap(success -> {
            if (!success)
               return Single.error(new Exception("End of game persistence failed in Redis for game:   " + gameId + "  it was playerWin:  " + playerWin));
            else {
                return inMemoRepo.getGame(gameId)
                        .flatMap(game -> dao.persist(game)
                                .flatMap(bool -> {
                                    if (!bool)
                                        return Single.error(new Exception("Failed to persist game in MongoDB  game:   " + gameId));
                                    else return Single.just(game);
                                }));
            }
        });
    }
}
