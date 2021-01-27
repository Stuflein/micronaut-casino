package casino.data;


import casino.api.v1.GameType;
import casino.data.entity.Game;

import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public casino.data.domain.Game saveGame(Game game){
        return gameRepository.save(game).toDomainGame();
    }
    List<casino.data.domain.Game> getAllWonGames(UUID playerId){
        return gameRepository.findByPlayerAndPlayerWinTrue(playerId).stream().map(Game::toDomainGame).collect(Collectors.toList());
    }
    List<casino.data.domain.Game> getAllForPlayer(UUID playerId){
        return gameRepository.findByPlayer(playerId).stream().map(Game::toDomainGame).collect(Collectors.toList());
    }
    public void setGameFinishedAndPlayerWin(UUID gameId,  boolean playerWin){
        gameRepository.update(gameId,  playerWin);
    }
    public casino.api.v1.Game createGame(GameType type, UUID playerId, long wager){
        Game newGame = new Game() ;
        newGame.setGameType(type);
        newGame.setPlayer(playerId);
        newGame.setWager(wager);
        casino.data.domain.Game persistedGame = gameRepository.save(newGame).toDomainGame();
        return new casino.api.v1.Game(persistedGame.getId(), persistedGame.getGameType(), persistedGame.getPlayer(), persistedGame.getWager());
    }

}
