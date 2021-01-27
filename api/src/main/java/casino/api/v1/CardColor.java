package casino.api.v1;

public enum CardColor {
    DIAMONDS("DIAMONDS"), HEARTS("HEARTS"), SPADES("SPADES"), CLUBS("CLUBS");

    private final String color;

    CardColor(String color) {
        this.color = color;
    }


    public String getCardColor() {
        return color;
    }

    @Override
    public String toString() {
        return this.color;
    }
}

