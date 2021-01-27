package casino.client.v1;

import casino.api.v1.CardGame;
import casino.api.v1.GameCreate;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

import javax.inject.Singleton;

@Singleton
@Client(id = "cardgame-casino")
public interface CardGameClient {


    @Post("/casino/cardgame/game")
    Single<CardGame> playGame(@Body GameCreate gameCreate);

}
