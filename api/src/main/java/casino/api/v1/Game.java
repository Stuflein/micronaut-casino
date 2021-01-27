package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.UUID;

public class Game {
    private UUID gameId;
   private GameType type;
   private UUID playerId;
   private long wager;

    @JsonCreator
    public Game() {
    }

    public Game(GameType type, UUID playerId, long wager) {
        this.type = type;
        this.playerId = playerId;
        this.wager = wager;
    }

    public Game(UUID gameId, GameType type, UUID playerId, long wager) {
        this.gameId = gameId;
        this.type = type;
        this.playerId = playerId;
        this.wager = wager;
    }

    public UUID getGameId() {
        return gameId;
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
