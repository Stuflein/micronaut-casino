package casino.client.v1;

import casino.api.v1.Game;
import casino.api.v1.GameCreate;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Client(id = "data-casino")
public interface InternalDataClient {

    @Post("/casino/data/game")
    Single<Game> createGame(@Body GameCreate gameCreate);

    @Post("/casino/data/game/{id}")
    void updateResultToGame(@PathVariable UUID id, boolean playerWin);

}
