package casino.account;

import casino.account.auth.CasinoUserDetails;
import casino.account.dao.AccountDao;
import casino.account.exceptions.LastLoginNotUpdatedException;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.security.event.LoginSuccessfulEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
        logger.info("LoginEventListener triggered:    eventSource:  " + event.getSource().toString() + "   event:  " + event.toString() + "  at:  " + LocalDateTime.now());
        UUID userId = ((CasinoUserDetails) event.getSource()).getUserId();
        logger.info("LoginEventListener:  userId:   " + userId);
        long epochSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        accountDao.setLastLogin(userId, epochSeconds).map(success -> {
            if (!success) {
                logger.error("LoginEventListener:   LastLogin for UserId:  " + userId + "on epochSeconds: " + epochSeconds + " not updated");
                throw new LastLoginNotUpdatedException("LastLogin for UserId:  " + userId + "on epochSeconds: " + epochSeconds + " not updated");
            } else {
                logger.info("LoginEventListener:   LastLogin for UserId:  " + userId + "on epochSeconds: " + epochSeconds + " IS updated");
                return true;
            }
        }).subscribe();
    }

}
