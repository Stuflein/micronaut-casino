package casino.client.v1;

import casino.api.v1.CardGame;
import casino.api.v1.EndOfGame;
import casino.api.v1.Player;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
@Client(id = "cardgame-casino")
public interface CardGameClient {


    @Post("/casino/cardgame/")  //TODO:hier aendern
    Single<EndOfGame> playGame(@Body Player player);


    @Get("/casino/cardgame/{gameId}/win")
    Single<CardGame> getGamePlayerWin(@PathVariable UUID gameId);


    @Get("/casino/cardgame/{gameId}/loose")
    Single<CardGame> getGamePlayerLoose(@PathVariable UUID gameId);

    @Post("/casino/cardgame/shitty")
    Single<CardGame> play(@Body Player player);

}
