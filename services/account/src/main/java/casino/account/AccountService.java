package casino.account;

import casino.account.dao.AccountDao;
import casino.account.domain.Account;
import casino.api.v1.UserCreate;
import io.reactivex.Maybe;
import io.reactivex.Single;

import javax.inject.Inject;
import java.util.UUID;

public class AccountService {
    @Inject
    AccountDao dao;

    Single<Account> createAccount(UserCreate userCreate){
        return dao.createAccount(userCreate.getUsername(), userCreate.getEmail(), userCreate.getCredit(), true);
    }
    Single<Account> getAccount(UUID id){
        return dao.getAccount(id);
    }
    public Single<Account> getByUsername(String username) {
        return dao.getAccount(username);
    }

    public Single<Account> updateAccount(UUID id, String username, String email, long credit){
        return dao.updateAccount(id, username, email, credit);
    }

    public Single<Boolean> deleteAccount(UUID id) {
        return dao.delete(id);
    }
}
