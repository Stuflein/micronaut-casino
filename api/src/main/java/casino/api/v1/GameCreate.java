package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.UUID;

public class GameCreate implements Serializable {
    private GameType type;
    private UUID playerId;
    private long wager;

    @JsonCreator
    public GameCreate() {
    }



    public GameCreate(GameType type, UUID playerId, long wager) {
        this.type = type;
        this.playerId = playerId;
        this.wager = wager;
    }

    public GameType getType() {
        return type;
    }


    public UUID getPlayerId() {
        return playerId;
    }


    public long getWager() {
        return wager;
    }

}
