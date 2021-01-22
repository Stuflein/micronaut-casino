package casino.account.exceptions;

public class LastLoginNotUpdatedException extends RuntimeException {
    public LastLoginNotUpdatedException(String message) {
        super(message);
    }
}
