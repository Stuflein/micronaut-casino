package casino.cardgame.exceptions;

public class NotEnoughFundsException extends Exception{
    private long funds;
    private long wager;

    public NotEnoughFundsException(String message, long wager) {
        super(message);
        this.wager = wager;
    }

    public NotEnoughFundsException(long wager, long funds, String message) {
        super(message);
        this.wager = wager;
        this.funds = funds;
    }

    public long getFunds() {
        return funds;
    }

    public long getWager() {
        return wager;
    }
}
