package casino.account.dao;

import io.reactivex.Single;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface PasswordDao {

    Single<Boolean> addPassword(@NotNull UUID id, @NotNull String password);

    Single<String> getPassword(UUID id);

    Single<Boolean> setPassword(UUID id, String password);

    Single<Boolean> deletePassword(UUID id);
}
