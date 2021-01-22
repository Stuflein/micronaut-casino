package casino.cardgame.exceptions;

import java.util.UUID;

public class PersistMongoHandException extends Exception{
    private final UUID handId;
    public PersistMongoHandException(UUID handId, String message, Throwable cause) {
        super(message, cause);
        this.handId = handId;
    }

    public PersistMongoHandException(UUID handId, String message) {
        super(message);
        this.handId = handId;
    }
}
