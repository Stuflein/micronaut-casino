package casino.data.entity;

import casino.api.v1.GameType;
import io.micronaut.data.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedEntity
public class Game {
    @AutoPopulated
    @Id
    @GeneratedValue(GeneratedValue.Type.UUID)
    private UUID id;
    private GameType gameType;
    private UUID player;
    private long wager;
    private boolean playerWin;
    @DateCreated
    private LocalDateTime createdAt;
    @DateUpdated
    private LocalDateTime finishedAt;

    public boolean isPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(boolean playerWin) {
        this.playerWin = playerWin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public long getWager() {
        return wager;
    }

    public void setWager(long wager) {
        this.wager = wager;
    }

    public casino.data.domain.Game toDomainGame() {
        return new casino.data.domain.Game(this.getId(), this.getGameType(), this.getPlayer(), this.getWager());
    }
}
