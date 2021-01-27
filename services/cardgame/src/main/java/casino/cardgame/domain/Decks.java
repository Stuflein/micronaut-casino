package casino.cardgame.domain;

import casino.api.v1.Card;

import java.util.List;

public class Decks {
    private final List<Card> playerDrawPile;
    private final List<Card> botDrawPile;
    private final List<Card> playerDiscardPile;
    private final List<Card> botDiscardPile;
    private final List<Card> drawPile;
    private long roundNumber;
    private int handPlayedInRoundNumber;


    public Decks(List<Card> playerDrawPile, List<Card> botDrawPile, List<Card> playerDiscardPile, List<Card> botDiscardPile, List<Card> drawPile, long roundNumber, int handPlayedInRoundNumber) {
        this.playerDrawPile = playerDrawPile;
        this.botDrawPile = botDrawPile;
        this.playerDiscardPile = playerDiscardPile;
        this.botDiscardPile = botDiscardPile;
        this.drawPile = drawPile;
        this.roundNumber = roundNumber;
        this.handPlayedInRoundNumber = handPlayedInRoundNumber;
    }

    public void setRoundNumber(long roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setHandPlayedInRoundNumber(int handPlayedInRoundNumber) {
        this.handPlayedInRoundNumber = handPlayedInRoundNumber;
    }

    public List<Card> getPlayerDrawPile() {
        return playerDrawPile;
    }

    public List<Card> getBotDrawPile() {
        return botDrawPile;
    }

    public List<Card> getPlayerDiscardPile() {
        return playerDiscardPile;
    }

    public List<Card> getBotDiscardPile() {
        return botDiscardPile;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public long getRoundNumber() {
        return roundNumber;
    }

    public int getHandPlayedInRoundNumber() {
        return handPlayedInRoundNumber;
    }
}
