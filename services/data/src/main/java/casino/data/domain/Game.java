package casino.data.domain;

import casino.api.v1.GameType;

import java.util.UUID;

public class Game {
    private UUID id;
    private GameType gameType;
    private UUID player;
    private long wager;

    public Game(UUID id, GameType gameType, UUID player, long wager) {
        this.id = id;
        this.gameType = gameType;
        this.player = player;
        this.wager = wager;
    }

    public UUID getId() {
        return id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public UUID getPlayer() {
        return player;
    }

    public long getWager() {
        return wager;
    }

}
