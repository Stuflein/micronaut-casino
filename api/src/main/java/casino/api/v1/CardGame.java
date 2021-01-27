package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CardGame implements Serializable {


    private UUID gameId;
    private boolean isFinished;
    private boolean playerWin;
    private UUID user;
    private long wager;
    private Map<Long, List<CardsPlayed>> rounds;

    @JsonCreator
    public CardGame() {
    }

    public CardGame(UUID gameId, boolean isFinished, boolean playerWin, UUID user, long wager, Map<Long, List<CardsPlayed>> rounds) {
        this.gameId = gameId;
        this.isFinished = isFinished;
        this.playerWin = playerWin;
        this.user = user;
        this.wager = wager;
        this.rounds = rounds;
    }

    public UUID getGameId() {
        return gameId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public UUID getUser() {
        return user;
    }

    public long getWager() {
        return wager;
    }

    public Map<Long, List<CardsPlayed>> getRounds() {
        return rounds;
    }

    public boolean isPlayerWin() {
        return playerWin;
    }
}
