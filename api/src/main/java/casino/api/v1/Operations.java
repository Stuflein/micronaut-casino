package casino.api.v1;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.reactivex.Single;

import java.util.UUID;

public interface Operations<T, U> {

    @Post("/")
    Single<T> create(U createEntity);

    @Get("/{id}")
    Single<T> get(UUID id);

    @Put("/{id}")
    Single update(UUID id, T updateEntity);

    @Delete("/{id}")
    Single<HttpStatus> delete(UUID id);

}
