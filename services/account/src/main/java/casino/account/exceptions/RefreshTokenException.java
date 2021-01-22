package casino.account.exceptions;

import java.util.UUID;

public class RefreshTokenException extends Exception {
    private UUID id;
    private String refreshToken;
    public RefreshTokenException(String reason, UUID id) {
        super(reason);
        this.id = id;
    }

    public RefreshTokenException(String message, String refreshToken) {
        super(message);
        this.refreshToken = refreshToken;
    }

    public UUID getId() {
        return id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
