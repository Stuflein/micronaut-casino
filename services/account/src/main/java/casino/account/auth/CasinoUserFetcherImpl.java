package casino.account.auth;

import casino.account.dao.AccountDao;
import casino.account.dao.PasswordDao;
import casino.account.domain.Account;
import casino.api.v1.UserState;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

@Singleton
public class CasinoUserFetcherImpl implements CasinoUserFetcher {
    @Inject
    PasswordDao passwordDao;
    @Inject
    AccountDao accountDao;


    @Override
    public Publisher<UserState> findByUsername(@NotNull String username) {
        return accountDao.getAccount(username)
                .flatMapPublisher((Account account) -> passwordDao.getPassword(account.getId()).map(password -> {
                    account.setPassword(password);
                    return account.getUserState();
                }).toFlowable());
    }
}
