package casino.cardgame;

import casino.api.v1.CardGame;
import casino.api.v1.User;
import casino.cardgame.dao.MongoDao;
import casino.cardgame.domain.MongoCardGame;
import casino.client.v1.AccountClient;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class CardGameServiceImpl implements CardGameService {

    private final MongoDao dao;
    private final AccountClient accountClient;
    private final GameActionService gameActionService;
    private final Logger logger = LoggerFactory.getLogger(CardGameServiceImpl.class);

    public CardGameServiceImpl(MongoDao dao, AccountClient accountClient, GameActionService gameActionService) {
        this.dao = dao;
        this.accountClient = accountClient;
        this.gameActionService = gameActionService;
    }

    @Override
    public Single<CardGame> getCardGame(UUID gameId) {
        logger.info("CardgameServiceImpl:  getCardgame   " + gameId);
        return dao.get(gameId).map(MongoCardGame::toApiCardGame);
    }

    @Override
    public Single<Boolean> wagerToAccountCredit(UUID accountId, long wager) {
        logger.info("CardgameServiceImpl:  wagerToAccountCredit for User " + accountId);
        return accountClient.get(accountId).flatMap(user -> {
            if (user.getCredit() < wager) {
                logger.info("User:  " + accountId + "    Not enough credit:  " + user.getCredit() + " for wager:  " + wager);
                return Single.just(false);
//                return Single.error(new NotEnoughFundsException(user.getCredit(), wager, "Not enough credit:  " + user.getCredit() + " for wager:  " + wager));
            }
            logger.info("CardgameServiceImpl:  wagerToAccountCredit for User " + accountId + "  , user has sufficient funds");
            return accountClient.update(accountId, new User(accountId, user.getUsername(), user.getEmail(), user.getCredit() - wager)).map(updatedUser -> (updatedUser.getCredit() == user.getCredit() - wager));
        });
    }

    public Single<MongoCardGame> startGame(UUID accountId, long wager) {
        logger.info("CardgameServiceImpl:  start game with user:   " + accountId   +  "    for amount of:   " + wager+"$");
        return gameActionService.startGame(accountId, wager);
    }
}
