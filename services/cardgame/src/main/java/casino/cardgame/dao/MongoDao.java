package casino.cardgame.dao;

import casino.api.v1.Card;
import casino.api.v1.Decks;
import casino.api.v1.Result;
import casino.cardgame.domain.MongoCardGame;
import casino.cardgame.domain.MongoDecks;
import casino.cardgame.domain.MongoHand;
import io.reactivex.Single;

import java.util.List;
import java.util.UUID;

public interface MongoDao {

    Single<Boolean> persist(UUID gameId, boolean playerWin);

    Single<Boolean> persist(MongoCardGame game);

    Single<Boolean> persist(UUID gameId, MongoHand cardsPlayed, MongoDecks decks);

    Single<Boolean> persist(long round, int cardsPlayedInRound, Result result, Card playerCard, Card botCard, UUID gameId, Decks decks);

    Single<MongoCardGame> persistAndGet(UUID gameId, boolean playerWin);

    Single<MongoCardGame> get(UUID gameId);
    Single<List<MongoCardGame>> getAllForPlayer(UUID playerId);

    Single<Boolean> persistDecks(List<MongoDecks> decks);
}
