package casino.account;

import casino.account.domain.Account;
import casino.api.v1.Operations;
import casino.api.v1.User;
import casino.api.v1.UserCreate;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Controller("casino/account")
public class AccountController implements Operations<User, UserCreate> {

    @Inject
    AccountService accountService;

    @Override
    @Post("/")
    public Single<User> create(@Body UserCreate createEntity) {
        return accountService.createAccount(createEntity).map(acc -> acc.getApiUser());
    }

    @Override
    @Get("/{id}")
    public Single<User> get(@PathVariable UUID id) {

        return accountService.getAccount(id).map(Account::getApiUser);
    }

    @Override
    public Single<User> update(UUID id, User updateEntity) {

        return accountService.updateAccount(id, updateEntity.getUsername(), updateEntity.getEmail(), updateEntity.getCredit())
                .map(account -> account.getApiUser());
    }

    @Override
    public Single<HttpStatus> delete(UUID id) {

        return accountService.deleteAccount(id).map(b -> {
            if (b) return HttpStatus.OK;
            else return HttpStatus.UNAUTHORIZED;
        });
    }
}
