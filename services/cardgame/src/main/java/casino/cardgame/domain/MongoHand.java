package casino.cardgame.domain;


import casino.api.v1.Card;
import casino.api.v1.CardsPlayed;
import casino.api.v1.Result;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MongoHand implements Serializable {

    @BsonProperty("round_number")
    @JsonProperty("round_number")
    private long roundNumber;
    @BsonProperty("hand_played_in_round")
    @JsonProperty("hand_played_in_round")
    private int handPlayedInRound;
    @BsonProperty("result")
    @JsonProperty("result")
    private Result result;
    @BsonProperty("player_card_played")
    @JsonProperty("player_card_played")
    private String playerCardPlayed;
    @BsonProperty("bot_card_played")
    @JsonProperty("bot_card_played")
    private String botCardPlayed;
    @BsonProperty("hand_id")
    private UUID handId;

    public MongoHand(long roundNumber, int handPlayedInRound, Result result, Card playerCardPlayed, Card botCardPlayed, UUID handId) {
        this.roundNumber = roundNumber;
        this.handPlayedInRound = handPlayedInRound;
        this.result = result;
        this.playerCardPlayed = playerCardPlayed.toString();
        this.botCardPlayed = botCardPlayed.toString();
        this.handId = handId;
    }

    public MongoHand(long roundNumber, int handPlayedInRound, Result result, String playerCardPlayed, String botCardPlayed, UUID handId) {
        this.roundNumber = roundNumber;
        this.handPlayedInRound = handPlayedInRound;
        this.result = result;
        this.playerCardPlayed = playerCardPlayed;
        this.botCardPlayed = botCardPlayed;
        this.handId = handId;
    }

    @BsonCreator
    public MongoHand() {
    }

    public MongoHand(CardsPlayed cardsPlayed) {
        this(cardsPlayed.getRoundNumber(), cardsPlayed.getHandPlayedInRoundNumber(), cardsPlayed.getResult(), cardsPlayed.getPlayerCardPlayed(), cardsPlayed.getBotCardPlayed(), UUID.randomUUID());

    }

    public UUID getHandId() {
        return handId;
    }

    public long getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(long roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getHandPlayedInRound() {
        return handPlayedInRound;
    }

    public void setHandPlayedInRound(int handPlayedInRound) {
        this.handPlayedInRound = handPlayedInRound;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setHandId(UUID handId) {
        this.handId = handId;
    }

    public String getPlayerCardPlayed() {
        return playerCardPlayed;
    }

    public void setPlayerCardPlayed(String playerCardPlayed) {
        this.playerCardPlayed = playerCardPlayed;
    }

    public String getBotCardPlayed() {
        return botCardPlayed;
    }

    public void setBotCardPlayed(String botCardPlayed) {
        this.botCardPlayed = botCardPlayed;
    }

    public CardsPlayed toApiHand() {
        String[] playerCardString = this.getPlayerCardPlayed().split(" ");
        String[] botCardString = this.getBotCardPlayed().split(" ");
        Card playerCard = new Card(playerCardString[0], playerCardString[1]);
        Card botCard = new Card(botCardString[0], botCardString[1]);
        return new CardsPlayed(this.getRoundNumber(), this.getHandPlayedInRound(), this.getResult(), playerCard, botCard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoHand)) return false;
        MongoHand mongoHand = (MongoHand) o;
        return getRoundNumber() == mongoHand.getRoundNumber() && getHandPlayedInRound() == mongoHand.getHandPlayedInRound() && getResult() == mongoHand.getResult() && getPlayerCardPlayed().equals(mongoHand.getPlayerCardPlayed()) && getBotCardPlayed().equals(mongoHand.getBotCardPlayed()) && getHandId().equals(mongoHand.getHandId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoundNumber(), getHandPlayedInRound(), getHandId());
    }
}
