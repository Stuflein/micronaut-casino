package casino.api.v1;

public enum Result {
    PLAYER_WIN("player"),
    BOT_WIN("bot"),
    DRAW("draw");

    Result(String result) {
        this.result = result;
    }

    private final String result;

    public String getResult() {
        return result;
    }
}
