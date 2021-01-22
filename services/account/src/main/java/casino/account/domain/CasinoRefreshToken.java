package casino.account.domain;

import java.util.UUID;

public class CasinoRefreshToken {
    private UUID userId;
    private String refreshToken;
    private boolean revoked;
}
