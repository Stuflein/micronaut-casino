package casino.cardgame.events;

import casino.api.v1.CardsPlayed;
import casino.api.v1.Decks;
import io.micronaut.context.event.ApplicationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

public class PersistMongoHandEvent extends ApplicationEvent {

    /**
     * Event triggered when CardsPlayed gets persisted as MongoHand
     * in MongoDB
     *
     * @param source The CardsPlayed that gets persisted
     * @throws IllegalArgumentException if source is null
     */
    private final UUID gameId;
    private final Decks decks;
    private final Logger logger = LoggerFactory.getLogger(PersistMongoHandEvent.class);


    public PersistMongoHandEvent(CardsPlayed source, UUID gameId, Decks decks) {
        super(source);
        logger.info("PersistMongoHandEvent triggered at :    " + Instant.now());
        this.gameId = gameId;
        this.decks = decks;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Decks getDecks() {
        return decks;
    }
}
