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

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class PersistenceService {

    /**
     * @param inMemoRepo InMemo redis in memo db service
     * @param dao        MongoDB persistence service
     */
    public PersistenceService(InMemo inMemoRepo, MongoDao dao) {
        this.inMemoRepo = inMemoRepo;
        this.dao = dao;
    }

    private final InMemo inMemoRepo;
    private final MongoDao dao;

    Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    public Single<MongoCardGame> saveGameStart(UUID gameId, UUID playerId, long wager) {
        logger.info("PersistenceService:  saveGameStart for user:  {}", playerId);
        return inMemoRepo.save(new MongoCardGame(gameId, playerId, wager));
    }

    public void save(UUID gameId, MongoHand hand, Decks decks) {
        logger.info("PersistenceService:  save hand:    {}   in game:    {}", hand.getHandId(), gameId);
        inMemoRepo.save(gameId, hand, new MongoDecks(decks, hand.getHandId()));
    }

    public Single<MongoCardGame> endOfGamePersistence(UUID gameId, boolean playerWin) {
        logger.info("PersistenceService:  endOfGamePersistence for game:    {}     player won:   {}", gameId, playerWin);
        return inMemoRepo.saveEndOfGame(gameId, playerWin).flatMap((Boolean success) -> {
            if (Boolean.TRUE.equals(success)) {
                return inMemoRepo.getGame(gameId)
                        .flatMap(game -> dao.persist(game)
                                .flatMap((Boolean bool) -> {
                                    if (Boolean.TRUE.equals(bool)) return Single.just(game);
                                    else
                                        return Single.error(new Exception("Failed to persist game in MongoDB  game:   " + gameId));
                                }));
            } else {
                return Single.error(new Exception("End of game persistence failed in Redis for game:   " + gameId + "  it was playerWin:  " + playerWin));
            }
        });
    }
}
