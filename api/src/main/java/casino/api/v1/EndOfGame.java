package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.UUID;

public class EndOfGame implements Serializable {
    private UUID gameId;
    private boolean PlayerWin;

    @JsonCreator
    public EndOfGame() {
    }

    public EndOfGame(UUID gameId, boolean playerWin) {
        this.gameId = gameId;
        PlayerWin = playerWin;
    }

    public UUID getGameId() {
        return gameId;
    }

    public boolean isPlayerWin() {
        return PlayerWin;
    }
}
