package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonFormat;


public enum CardColor {
    DIAMONDS("DIAMONDS"), HEARTS("HEARTS"), SPADES("SPADES"), CLUBS("CLUBS");

    private final String cardColor;

    CardColor(String color) {
        this.cardColor = color;
    }

    //        @JsonValue
    public String getCardColor() {
        return cardColor;
    }

    @Override
    public String toString() {
        return this.cardColor;
    }
}

