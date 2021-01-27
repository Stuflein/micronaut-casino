package casino.data;

import casino.api.v1.Game;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;

@Controller("casino/data")
@Singleton
public class DataController {
    @Inject
    protected GameService gameService;

    @Post("/game")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<Game> createGame(@Body casino.api.v1.Game game){
        return Single.just(gameService.createGame(game.getType(), game.getPlayerId(), game.getWager()));
    }
    @Post("/game/{id}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void updateResultToGame(@PathVariable UUID id, boolean playerWin){
        gameService.setGameFinishedAndPlayerWin(id, playerWin);
    }
}
