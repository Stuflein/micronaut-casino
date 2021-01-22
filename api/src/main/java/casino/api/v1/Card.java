package casino.api.v1;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;


public class Card implements Comparable<Card>, Serializable {

    private CardType type;
    private CardColor color;

    public Card(CardType type, CardColor color) {
        this.type = type;
        this.color = color;
    }

    @JsonCreator
    public Card() {
    }

    @JsonCreator
    public static Card createCard(String card){
        String[] cardString = card.split(" ");
        return new Card(cardString[0], cardString[1]);
    }
    public Card(String type, String color) {
        this(CardType.valueOf(type), CardColor.valueOf(color));
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    @Override
    public int compareTo(Card o) {
        return this.getType().compareTo(o.getType());
    }


    @JsonValue
    @Override
    public String toString() {
        return this.type.toString() + " " + this.color.toString();
    }

    @Override
    public int hashCode() {
        return (this.type.hashCode() + this.color.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            if (obj instanceof Card)
                return ((Card) obj).color.hashCode() == this.color.hashCode() && ((Card) obj).type.hashCode() == this.type.hashCode();
        return false;
    }
}
