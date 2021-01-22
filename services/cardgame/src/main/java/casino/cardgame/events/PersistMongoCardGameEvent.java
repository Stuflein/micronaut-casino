package casino.cardgame.events;

import io.micronaut.context.event.ApplicationEvent;

public class PersistMongoCardGameEvent extends ApplicationEvent {
    /**
     * Event triggered when CardGame gets persisted
     * in MongoDB
     *
     * @param source The CardGame that gets persisted
     * @throws IllegalArgumentException if source is null
     */


    public PersistMongoCardGameEvent(Object source) {
        super(source);
    }
}
