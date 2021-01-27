package casino.cardgame;

import casino.api.v1.CardGame;
import casino.api.v1.GameCreate;
import casino.cardgame.domain.MongoCardGame;
import io.reactivex.Single;

import java.util.UUID;

public interface CardGameService {

    Single<CardGame> getCardGame(UUID gameId);

    Single<Boolean> wagerToAccountCredit(UUID accountId, long wager);

    Single<MongoCardGame> startGame(GameCreate gameCreate);
}
