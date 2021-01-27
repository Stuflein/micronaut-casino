package casino.account;

import casino.account.auth.CasinoUserDetails;
import casino.account.dao.AccountDao;
import casino.account.exceptions.LastLoginNotUpdatedException;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.security.event.LoginSuccessfulEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Singleton
public class LoginEventListener {

    /**
     * EventListener for LoginEvent
     * updates property last_login of User in DB
     */


    private final Logger logger = LoggerFactory.getLogger(LoginEventListener.class);

    private final AccountDao accountDao;

    public LoginEventListener(AccountDao accountDao) {
        logger.info("LoginEventListener CREATED");
        this.accountDao = accountDao;
    }

    @EventListener
    public void onLoginEvent(LoginSuccessfulEvent event) {
        logger.info("LoginEventListener triggered:    eventSource:   {}     event:   {}    at:    {}", event.getSource(), event, LocalDateTime.now());
        UUID userId = ((CasinoUserDetails) event.getSource()).getUserId();
        logger.info("LoginEventListener:  userId:   {}", userId);
        long epochSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        accountDao.setLastLogin(userId, epochSeconds).map((Boolean success) -> {
            if (Boolean.TRUE.equals(success)) {
                logger.info("LoginEventListener:   LastLogin for UserId:  {}    on epochSeconds:   {}   IS updated", userId, epochSeconds);
                return true;
            } else {
                logger.error("LoginEventListener:   LastLogin for UserId:  {}   on epochSeconds: {} not updated", userId, epochSeconds);
                throw new LastLoginNotUpdatedException("LastLogin for UserId:  " + userId + "on epochSeconds: " + epochSeconds + " not updated");
            }
        }).subscribe();
    }

}
