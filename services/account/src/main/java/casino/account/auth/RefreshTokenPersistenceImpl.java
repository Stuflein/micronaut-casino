package casino.account.auth;

import casino.account.dao.AccountDao;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
//
//@Singleton
//public class RefreshTokenPersistenceImpl implements RefreshTokenPersistence {
//
//    private final Logger logger = LoggerFactory.getLogger(RefreshTokenPersistenceImpl.class);
//    private final AccountDao accountDao;
//
//    public RefreshTokenPersistenceImpl(AccountDao accountDao) {
//        this.accountDao = accountDao;
//    }
//
//
//    @EventListener
//    @Override
//    public void persistToken(RefreshTokenGeneratedEvent event) {
//        logger.info("RefreshTokenPersistenceImpl:   "  +  event.getUserDetails().getUsername() +  "loginEvent received");
//        accountDao.setRefreshTokenForUsername(event.getUserDetails().getUsername(), event.getRefreshToken()).subscribe();
//    }
//
//    @Override
//    public Publisher<UserDetails> getUserDetails(String refreshToken) {
//        return accountDao.getAccountForRefreshToken(refreshToken)
//                .flatMapPublisher(account -> {
//                    List<String> userRoleList = new ArrayList<>();
//                    userRoleList.add(account.getRole().toString());
//                    return Flowable.just(new CasinoUserDetails(account.getUsername(), userRoleList, account.getId()));
//                });
//    }
//}
