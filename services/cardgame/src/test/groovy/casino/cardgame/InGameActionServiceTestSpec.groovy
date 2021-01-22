package casino.cardgame

import casino.cardgame.dao.InMemoRedis
import casino.cardgame.dao.MongoDao
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class InGameActionServiceTestSpec extends Specification {
    @Shared
    @Inject
    GameActionService shittyTwo;
    @Shared
    @Inject
    MongoDao dao;
    @Shared
    @Inject
    InMemoRedis inMemo;

    def "play shitty game"(){
        when: "create new game"
        def mongoCardGame = shittyTwo.startGame(UUID.randomUUID(), 10).blockingGet()
        then: "game should be persisted"
        def persistedGame = dao.get(mongoCardGame.gameId).blockingGet()
        mongoCardGame.toApiCardGame().finished == persistedGame.finished
        mongoCardGame.toApiCardGame().playerWin == persistedGame.playerWin
        mongoCardGame.gameId == persistedGame.gameId
        and: "InMemo game should match persisted game"
        def inMemoGame = inMemo.getGame(mongoCardGame.gameId).blockingGet()
        inMemoGame.rounds.size() == persistedGame.rounds.size()
        def apiFromInMemo = inMemoGame.toApiCardGame()
        def apiFromEnOfRound = mongoCardGame.toApiCardGame()
        apiFromInMemo.rounds.entrySet().size() == apiFromEnOfRound.rounds.entrySet().size()
        println "${inMemoGame.gameId}"
    }

}