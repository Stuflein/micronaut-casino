package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {

    private UUID playerId;
    private long wager;

    public Player(UUID playerId, long wager) {
        this.playerId = playerId;
        this.wager = wager;
    }

    @JsonCreator
    public Player() {
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getWager() {
        return wager;
    }
}
