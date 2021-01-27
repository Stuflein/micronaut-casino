package casino.cardgame;

import casino.api.v1.CardGame;
import casino.api.v1.GameCreate;
import casino.api.v1.User;
import casino.cardgame.dao.MongoDao;
import casino.cardgame.domain.MongoCardGame;
import casino.client.v1.AccountClient;
import casino.client.v1.InternalDataClient;
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
    private final InternalDataClient internalDataClient;
    private final Logger logger = LoggerFactory.getLogger(CardGameServiceImpl.class);

    /**
     * @param dao                service for cardgame in MongoDB
     * @param accountClient      client for Account module
     * @param gameActionService  service to start and play game
     * @param internalDataClient client to communicate game and result to data module
     */

    public CardGameServiceImpl(MongoDao dao, AccountClient accountClient, GameActionService gameActionService, InternalDataClient internalDataClient) {
        this.dao = dao;
        this.accountClient = accountClient;
        this.gameActionService = gameActionService;
        this.internalDataClient = internalDataClient;
    }

    @Override
    public Single<CardGame> getCardGame(UUID gameId) {
        logger.info("CardgameServiceImpl:  getCardgame:  {}", gameId);
        return dao.get(gameId).map(MongoCardGame::toApiCardGame);
    }

    @Override
    public Single<Boolean> wagerToAccountCredit(UUID accountId, long wager) {
        logger.info("CardgameServiceImpl:  wagerToAccountCredit for User:  {} ", accountId);
        return accountClient.get(accountId).flatMap(user -> {
            if (user.getCredit() < wager) {
                logger.info("CardgameServiceImpl:  User:  {}   Not enough credit:  {}   for wager:    {}", accountId, user.getCredit(), wager);
                return Single.just(false);
            }
            logger.info("CardgameServiceImpl:  wagerToAccountCredit for User :   {}  , user has sufficient funds", accountId);
            return accountClient.update(accountId, new User(accountId, user.getUsername(), user.getEmail(), user.getCredit() - wager)).map(updatedUser -> (updatedUser.getCredit() == user.getCredit() - wager));
        });
    }

    public Single<MongoCardGame> startGame(GameCreate gameCreate) {
        logger.info("CardgameServiceImpl:  start game with user:   {}     for amount of:    {}$", gameCreate.getPlayerId(), gameCreate.getWager());

        return internalDataClient.createGame(gameCreate)
                .flatMap(createdGame ->
                        gameActionService.startGame(createdGame.getGameId(), createdGame.getPlayerId(), createdGame.getWager()))
                .doOnSuccess(game -> internalDataClient.updateResultToGame(game.getGameId(), game.isPlayerWin()));
    }
}
