package casino.cardgame.domain;


import casino.api.v1.Card;
import casino.api.v1.Decks;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoDecks implements Serializable {
    @BsonProperty("player_draw_pile")
    @JsonProperty("player_draw_pile")
    private List<String> playerDrawPile;
    @BsonProperty("bot_draw_pile")
    @JsonProperty("bot_draw_pile")
    private List<String> botDrawPile;
    @BsonProperty("player_discard_pile")
    @JsonProperty("player_discard_pile")
    private List<String> playerDiscardPile;
    @BsonProperty("bot_discard_pile")
    @JsonProperty("bot_discard_pile")
    private List<String> botDiscardPile;
    @BsonProperty("draw_pile")
    @JsonProperty("draw_pile")
    private List<String> drawPile;
    @BsonId
    private UUID handId;


    @BsonCreator
    public MongoDecks() {
    }

    public MongoDecks(Decks decks, UUID handId) {
        this.playerDrawPile = decks.getPlayerDrawPile().stream().map(Card::toString).collect(Collectors.toList());
        this.botDrawPile = decks.getBotDrawPile().stream().map(Card::toString).collect(Collectors.toList());
        this.playerDiscardPile = decks.getPlayerDiscardPile().stream().map(Card::toString).collect(Collectors.toList());
        this.botDiscardPile = decks.getBotDiscardPile().stream().map(Card::toString).collect(Collectors.toList());
        this.drawPile = decks.getDrawPile().stream().map(Card::toString).collect(Collectors.toList());
        this.handId = handId;
    }

    public MongoDecks(casino.cardgame.domain.Decks decks, UUID handId) {
        this.playerDrawPile = decks.getPlayerDrawPile().stream().map(Card::toString).collect(Collectors.toList());
        this.botDrawPile = decks.getBotDrawPile().stream().map(Card::toString).collect(Collectors.toList());
        this.playerDiscardPile = decks.getPlayerDiscardPile().stream().map(Card::toString).collect(Collectors.toList());
        this.botDiscardPile = decks.getBotDiscardPile().stream().map(Card::toString).collect(Collectors.toList());
        this.drawPile = decks.getDrawPile().stream().map(Card::toString).collect(Collectors.toList());
        this.handId = handId;
    }

    public UUID getHandId() {
        return handId;
    }

    public List<String> getPlayerDrawPile() {
        return playerDrawPile;
    }

    public List<String> getBotDrawPile() {
        return botDrawPile;
    }

    public List<String> getPlayerDiscardPile() {
        return playerDiscardPile;
    }

    public List<String> getBotDiscardPile() {
        return botDiscardPile;
    }

    public List<String> getDrawPile() {
        return drawPile;
    }

    public void setPlayerDrawPile(List<String> playerDrawPile) {
        this.playerDrawPile = playerDrawPile;
    }

    public void setBotDrawPile(List<String> botDrawPile) {
        this.botDrawPile = botDrawPile;
    }

    public void setPlayerDiscardPile(List<String> playerDiscardPile) {
        this.playerDiscardPile = playerDiscardPile;
    }

    public void setBotDiscardPile(List<String> botDiscardPile) {
        this.botDiscardPile = botDiscardPile;
    }

    public void setDrawPile(List<String> drawPile) {
        this.drawPile = drawPile;
    }


    public Decks toApiDecks() {
        return new Decks(
                cardListFromString(this.getPlayerDrawPile()), cardListFromString(this.getBotDrawPile()), cardListFromString(this.getPlayerDiscardPile()),
                cardListFromString(this.getBotDiscardPile()), cardListFromString(this.getDrawPile()));
    }

    private Card cardFromString(String card) {
        String[] cardString = card.split(" ");
        return new Card(cardString[0], cardString[1]);
    }

    private List<Card> cardListFromString(List<String> cardList) {
        return cardList.stream().map(c -> cardFromString(c)).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoDecks)) return false;
        MongoDecks that = (MongoDecks) o;
        return getPlayerDrawPile().equals(that.getPlayerDrawPile()) && getBotDrawPile().equals(that.getBotDrawPile()) && getPlayerDiscardPile().equals(that.getPlayerDiscardPile()) && getBotDiscardPile().equals(that.getBotDiscardPile()) && Objects.equals(getDrawPile(), that.getDrawPile()) && getHandId().equals(that.getHandId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHandId());
    }
}
