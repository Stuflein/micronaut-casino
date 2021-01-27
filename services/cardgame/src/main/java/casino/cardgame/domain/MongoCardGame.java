package casino.cardgame.domain;

import casino.api.v1.CardGame;
import casino.api.v1.CardsPlayed;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Introspected
public class MongoCardGame implements Serializable {
    @BsonId
    private UUID gameId;
    @BsonProperty("user")
    @JsonProperty("user")
    private UUID user;
    @BsonProperty("wager")
    @JsonProperty("wager")
    private long wager;
    @BsonProperty("rounds")
    @JsonProperty("rounds")
    private List<MongoHand> rounds;
    @BsonProperty("player_win")
    @JsonProperty("player_win")
    private boolean playerWin;
    @BsonProperty("finished")
    @JsonProperty("finished")
    private boolean finished;


    public MongoCardGame(UUID gameId, UUID user, long wager, List<MongoHand> rounds, boolean finished) {
        this.gameId = gameId;
        this.user = user;
        this.wager = wager;
        this.rounds = rounds;
        this.finished = finished;
    }

    public MongoCardGame(UUID gameId, UUID user, long wager) {
        this(gameId, user, wager, new ArrayList<>(), false);
    }

    public MongoCardGame(UUID gameId, UUID user, long wager, List<MongoHand> rounds, boolean playerWin, boolean finished) {
        this.gameId = gameId;
        this.user = user;
        this.wager = wager;
        this.rounds = rounds;
        this.playerWin = playerWin;
        this.finished = finished;
    }

    @BsonCreator
    public MongoCardGame() {

    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public long getWager() {
        return wager;
    }

    public void setWager(long wager) {
        this.wager = wager;
    }

    public List<MongoHand> getRounds() {
        return rounds;
    }

    public void setRounds(List<MongoHand> rounds) {
        this.rounds = rounds;
    }

    public boolean isPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(boolean playerWin) {
        this.playerWin = playerWin;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public CardGame toApiCardGame() {
        Map<Long, List<CardsPlayed>> roundsAsMap = this.getRounds()
                .stream().map(MongoHand::toApiHand)
                .sorted(Comparator.comparingLong(CardsPlayed::getRoundNumber))
                .collect(Collectors.groupingBy(
                        CardsPlayed::getRoundNumber,
                        Collectors.collectingAndThen(Collectors.toList(),
                                cardsPlayedList -> cardsPlayedList.stream().sorted(Comparator.comparingInt(CardsPlayed::getHandPlayedInRoundNumber)).collect(Collectors.toList()))
                ));
        return new CardGame(this.getGameId(), this.isFinished(), this.isPlayerWin(),
                this.getUser(), this.getWager(), roundsAsMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MongoCardGame)) return false;
        MongoCardGame cardGame = (MongoCardGame) o;
        return getWager() == cardGame.getWager() && getGameId().equals(cardGame.getGameId()) && getUser().equals(cardGame.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameId(), getUser(), getWager());
    }
}
