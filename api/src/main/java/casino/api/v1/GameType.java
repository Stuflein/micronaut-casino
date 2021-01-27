package casino.api.v1;

public enum GameType {
    CARDGAME("CARDGAME");


    GameType( String type) {
        this.type = type;
    }

    private final String type;


    public String getType() {
        return type;
    }
}
