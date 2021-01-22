package casino.cardgame.events;

import io.micronaut.context.event.ApplicationEvent;

import java.util.UUID;

public class PersistNewRoundInMongoCardGame extends ApplicationEvent {
    /**
     * Event triggered when MongoCardGame gets updated
     * withe new MongoCardsPlayed List in rounds
     * in MongoDB
     *
     * @param source PersistMongoRound object
     * @throws IllegalArgumentException if source is null
     */

    public PersistNewRoundInMongoCardGame(Object source) {
        super(source);
    }
}
