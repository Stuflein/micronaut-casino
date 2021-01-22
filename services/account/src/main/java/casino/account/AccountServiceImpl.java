package casino.account;

import casino.account.auth.PasswordEncoder;
import casino.account.dao.AccountDao;
import casino.account.dao.PasswordDao;
import casino.account.domain.Account;
import casino.api.v1.UserCreate;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public class AccountServiceImpl implements AccountService {
    /**
     * Implemented Link between Controller and Database operations
     *
     * @param accountDao   the AccountDatabse
     * @param passwordDao     PasswordDB
     * @param passwordEncoder encode/match password
     */

    private final AccountDao accountDao;
    private final PasswordDao passwordDao;
    private final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(AccountDao accountDao, PasswordDao passwordDao, PasswordEncoder passwordEncoder) {
        this.accountDao = accountDao;
        this.passwordDao = passwordDao;
        this.passwordEncoder = passwordEncoder;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Single<Account> createAccount(UserCreate userCreate) {
        UUID id = UUID.randomUUID();
        return accountDao.createAccount(id, userCreate.getUsername(), userCreate.getEmail(), userCreate.getCredit(), true)
                .flatMap(account -> passwordDao.addPassword(account.getId(), encodePassword(userCreate.getPassword()))
                        .flatMap(success -> {
                            if (!success) {
                                accountDao.delete(account.getId());
                                logger.info("AccountServiceImpl: Couldn´t create password for user: " + account.getId());
                                return Single.error(new Exception("Couldn´t create password for user: " + account.getId()));
                            }
                            logger.info("AccountServiceImpl: Password and Account created for user id:  " + account.getId());
                            return Single.just(account);
                        }));
    }

    @Override
    public Single<Account> getAccount(UUID id) {
        logger.info("AccountServiceImpl: getAccount for id:  " + id);
        return accountDao.getAccount(id);
    }

    @Override
    public Single<Account> getByUsername(String username) {
        logger.info("AccountServiceImpl: getByUsername for username:  " + username);
        return accountDao.getAccount(username);
    }

    @Override
    public Single<Account> updateAccount(UUID id, String username, String email, long credit) {
        logger.info("AccountServiceImpl: updateAccount for id:  " + id);
        return accountDao.updateAccount(id, username, email, credit);
    }

    @Override
    public Single<Boolean> deleteAccount(UUID id) {
        logger.info("AccountServiceImpl: deleteAccount for id:  " + id);

        return accountDao.delete(id).flatMap(isAccountDeleted -> {
            if (isAccountDeleted) {
                logger.info("AccountServiceImpl:  Account with id: " + id + "   Account IS deleted");
                return passwordDao.deletePassword(id);
            } else {
                logger.error("AccountServiceImpl:  Account with id: " + id + "   Account NOT deleted");
                return Single.just(false);
            }
        });
    }

    @Override
    public Single<List<Account>> getAll() {
        logger.info("AccountServiceImpl: getAll Accounts");
        return accountDao.getAll();
    }

    @Override
    public Single<Boolean> isNotAccount(String username, String email) {
        logger.info("AccountServiceImpl: isNotAccount check if username:  " + username + "  and email:  " + email + "  is taken");
        return accountDao.isNotAccount(username, email);
    }
}
