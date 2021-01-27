package casino.cardgame.dao;

import casino.api.v1.Card;
import casino.api.v1.Decks;
import casino.api.v1.Result;
import casino.cardgame.domain.MongoCardGame;
import casino.cardgame.domain.MongoDecks;
import casino.cardgame.domain.MongoHand;
import casino.cardgame.exceptions.PersistMongoHandException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class MongoDaoImpl implements MongoDao {


    private final MongoClient gameClient;

    private final MongoClient decksClient;

    private final Logger logger = LoggerFactory.getLogger(MongoDaoImpl.class);

    public MongoDaoImpl(@Named("game") MongoClient gameClient, @Named("decks") MongoClient decksClient) {
        this.gameClient = gameClient;
        this.decksClient = decksClient;
    }

    //start of game
    @Override
    public Single<Boolean> persist(MongoCardGame game) {
        logger.info("MongoDaoImpl: persist game:  {}", game.getGameId());
        MongoCollection<MongoCardGame> collection = getGameCollection();
        return Single.fromPublisher(collection.insertOne(game)).map(InsertOneResult::wasAcknowledged);
    }

    //update after each hand
    @Override
    public Single<Boolean> persist(UUID gameId, MongoHand cardsPlayed, MongoDecks decks) {
        logger.info("MongoDaoImpl: persist in game:  {}    played hand number:  {}   in round:   {}", gameId, cardsPlayed.getHandPlayedInRound(), cardsPlayed.getRoundNumber());
        return Single.fromPublisher(getGameCollection()
                .updateOne(Filters.eq("_id", gameId),
                        Updates.push("rounds", cardsPlayed)))
                .flatMap(updateResult -> {
                    if (updateResult.wasAcknowledged()) {
                        return Single.fromPublisher(getDecksCollection().insertOne(decks)).map(InsertOneResult::wasAcknowledged);
                    } else
                        return Single.error(new PersistMongoHandException(cardsPlayed.getHandId(), "UpdateResult for cardsplayed in game: " + gameId + "  not acknowledged  -> round: " + cardsPlayed.getRoundNumber() + "  handplayed in round:  " + cardsPlayed.getHandPlayedInRound()));
                });
    }

    public Single<Boolean> persistDecks(List<MongoDecks> decks) {
        return Single.fromPublisher(getDecksCollection().insertMany(decks)).map(InsertManyResult::wasAcknowledged);
    }

    @Override
    public Single<Boolean> persist(long round, int cardsPlayedInRound, Result result, Card playerCard, Card botCard, UUID gameId, Decks decks) {
        UUID handId = UUID.randomUUID();
        logger.info("MongoDaoImpl: persist in game:  {}   played hand number:  {}     in round:   {}    with result:  {} ", gameId, cardsPlayedInRound, round, result);
        MongoHand hand = new MongoHand(round, cardsPlayedInRound, result, playerCard, botCard, handId);
        MongoDecks mongoDecks = new MongoDecks(decks, handId);
        return Single.fromPublisher(getGameCollection().updateOne(Filters.eq("_id", gameId),
                Updates.push("rounds", hand))).flatMap(updateResult -> {
            if (updateResult.wasAcknowledged()) {
                return Single.fromPublisher(getDecksCollection().insertOne(mongoDecks)).map(InsertOneResult::wasAcknowledged);
            } else
                return Single.error(new PersistMongoHandException(handId, "UpdateResult for cardsplayed in game: " + gameId + "  not acknowledged  -> round: " + round + "  handplayed in round:  " + cardsPlayedInRound));
        });
    }

    //update end of game
    @Override
    public Single<MongoCardGame> persistAndGet(UUID gameId, boolean playerWin) {
        logger.info("MongoDaoImpl:   persistAndGet  game:  {}", gameId);
        return Single.fromPublisher(getGameCollection().findOneAndUpdate(
                Filters.eq("_id", gameId),
                Updates.combine(Updates.set("finished", true), Updates.set("player_win", playerWin)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER))
        );
    }

    @Override
    public Single<Boolean> persist(UUID gameId, boolean playerWin) {
        logger.info("MongoDaoImpl:   persist  game:  {}   playerWin:    {}", gameId, playerWin);

        return Single.fromPublisher(
                getGameCollection().updateOne(
                        Filters.eq("_id", gameId), Updates.combine(Updates.set("finished", true), Updates.set("player_win", playerWin))
                )
        ).flatMap(updateResult -> {
            if (updateResult.getModifiedCount() != 1) { //TODO: wahrscheinlich besser woanders den wall zu setzen
                return Single.error(new Exception("Cardgame finished not persisted"));
            } else return Single.just(playerWin);
        });
    }

    @Override
    public Single<MongoCardGame> get(UUID gameId) {
        return Single.fromPublisher(getGameCollection().find(Filters.eq("_id", gameId), MongoCardGame.class));
    }

    public Single<List<MongoCardGame>> getAllForPlayer(UUID playerId) {
        return Observable.fromPublisher(getGameCollection().find(Filters.eq("user", playerId))).toList();
    }

    //    private MongoCollection<MongoCardGame> getCollection() {
//        return gameClient.getDatabase("cardgameDB").getCollection("cardgame", MongoCardGame.class);
//    }
//    private <T> MongoClientSettings initClientSettings(Class<T> mongoClass) {
//        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
//                CodecRegistries.fromProviders(PojoCodecProvider.builder().register(mongoClass).automatic(true).build()));
//        ConnectionString conn = new ConnectionString("mongodb://root:dev@localhost:27017/cardgameDB?authSource=admin");
//        return MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
//                .applyConnectionString(conn)
//                .codecRegistry(pojoCodecRegistry).build();
//    }
    private MongoCollection<MongoCardGame> getGameCollection() {
        return gameClient.getDatabase("cardgameDB").getCollection("cardgame", MongoCardGame.class);
    }

    private MongoCollection<MongoDecks> getDecksCollection() {
        return decksClient.getDatabase("cardgameDB").getCollection("decks", MongoDecks.class);
    }

}
