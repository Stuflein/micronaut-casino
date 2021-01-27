package casino.data;


import casino.api.v1.GameType;
import casino.data.entity.Game;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface GameRepository extends CrudRepository<Game, UUID> {
    long findSumWagerByPlayer(UUID player);
    long findSumWagerByPlayerAndPlayerWin(UUID player, boolean playerWin);
    List<Game> findByPlayerAndGameType(UUID player, GameType type);
    List<Game> findByPlayerAndPlayerWinTrue(UUID player);
    List<Game> findByPlayerAndPlayerWinFalse(UUID player);
    void update(@Id UUID id, boolean playerWin);
    List<Game> findByPlayer(UUID player);
}
