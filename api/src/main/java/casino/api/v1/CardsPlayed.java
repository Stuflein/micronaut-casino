package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public class CardsPlayed implements Serializable {

    private long roundNumber;
    private int handPlayedInRoundNumber;
    private Result result;
    private Card playerCardPlayed;
    private Card botCardPlayed;

    public CardsPlayed(long roundNumber, int handPlayedInRoundNumber, Result result, Card playerCardPlayed, Card botCardPlayed) {
        this.roundNumber = roundNumber;
        this.handPlayedInRoundNumber = handPlayedInRoundNumber;
        this.result = result;
        this.playerCardPlayed = playerCardPlayed;
        this.botCardPlayed = botCardPlayed;
    }

    @JsonCreator
    public CardsPlayed() {
    }

    public long getRoundNumber() {
        return roundNumber;
    }

    public int getHandPlayedInRoundNumber() {
        return handPlayedInRoundNumber;
    }

    public Result getResult() {
        return result;
    }

    public Card getPlayerCardPlayed() {
        return playerCardPlayed;
    }


    public Card getBotCardPlayed() {
        return botCardPlayed;
    }

}
