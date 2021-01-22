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

public interface InMemo {
    Single<MongoCardGame> getGame(UUID gameId);

    List<MongoCardGame> getAllForUser(UUID userId);

    Single<MongoCardGame> save(MongoCardGame game);

    void save(UUID gameId, MongoHand hand, MongoDecks decks);

    Single<Boolean> saveEndOfGame(UUID gameId, boolean playerWin);

}
