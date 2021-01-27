package casino.api.v1;

public enum CardType {

    ACE("ACE"),
    TEN("TEN"),
    KING("KING"),
    QUEEN("QUEEN"),
    JACK("JACK"),
    NINE("NINE"),
    EIGHT("EIGHT"),
    SEVEN("SEVEN");

    private final String cardType;

    CardType(String cardType) {
        this.cardType = cardType;
    }


    public String getCardType() {
        return cardType;
    }

    @Override
    public String toString() {
        return this.cardType;
    }
}

