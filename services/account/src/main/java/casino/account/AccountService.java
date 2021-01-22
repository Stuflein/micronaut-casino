package casino.account;

import casino.account.domain.Account;
import casino.api.v1.UserCreate;
import io.reactivex.Single;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    /**
     * Interface for all methods for Account
     */
    Single<Account> createAccount(UserCreate userCreate);

    Single<Account> getAccount(UUID id);

    Single<Account> getByUsername(String username);

    Single<Account> updateAccount(UUID id, String username, String email, long credit);

    Single<Boolean> deleteAccount(UUID id);

    Single<List<Account>> getAll();

    Single<Boolean> isNotAccount(String username, String email);
}
