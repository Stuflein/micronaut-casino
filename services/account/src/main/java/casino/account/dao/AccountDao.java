package casino.account.dao;

import casino.account.domain.Account;
import io.reactivex.Single;

import java.util.List;
import java.util.UUID;

public interface AccountDao {
    Single<Account> createAccount(String username, String email, long credit, boolean player);
    Single<Account> getAccount(UUID id);
    Single<Account> getAccount(String username);
    Single<List<Account>> getAll();
    Single<Account> updateAccount(UUID id, String username, String email, long credit);
    Single<Boolean> delete(UUID id);
}
