package casino.client.v1;

import casino.api.v1.Operations;
import casino.api.v1.User;
import casino.api.v1.UserCreate;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Client(id = "account-casino")
public interface AccountClient extends Operations<User, UserCreate> {

    @Post("/casino/account/")
    Single<User> create(@Body UserCreate createEntity);

    @Get("/casino/account/{id}")
    Single<User> get(@NotBlank UUID id);

    @Put("/casino/account/{id}")
    Single<User> update(@NotBlank UUID id, @Body User updateEntity);

    @Delete("/casino/account/{id}")
    Single<HttpStatus> delete(UUID id);
}
