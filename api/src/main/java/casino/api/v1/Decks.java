package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;
import java.util.List;

public class Decks implements Serializable {
    private List<Card> playerDrawPile;
    private List<Card> botDrawPile;
    private List<Card> playerDiscardPile;
    private List<Card> botDiscardPile;
    private List<Card> drawPile;

    public Decks(List<Card> playerDrawPile, List<Card> botDrawPile, List<Card> playerDiscardPile, List<Card> botDiscardPile, List<Card> drawPile) {
        this.playerDrawPile = playerDrawPile;
        this.botDrawPile = botDrawPile;
        this.playerDiscardPile = playerDiscardPile;
        this.botDiscardPile = botDiscardPile;
        this.drawPile = drawPile;
    }

    @JsonCreator
    public Decks() {
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

}
