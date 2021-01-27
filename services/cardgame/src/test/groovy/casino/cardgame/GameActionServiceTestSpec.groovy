package casino.cardgame

import casino.cardgame.dao.InMemoRedis
import casino.cardgame.dao.MongoDao
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class GameActionServiceTestSpec extends Specification {
    @Shared
    @Inject
    GameActionService actionService
    @Shared
    @Inject
    MongoDao dao
    @Shared
    @Inject
    InMemoRedis inMemo

    def "play game"(){
        when: "create new game"
        def mongoCardGame = actionService.startGame(UUID.randomUUID(), UUID.randomUUID(), 10).blockingGet()
        then: "game should be persisted"
        def persistedGame = dao.get(mongoCardGame.gameId).blockingGet()
        mongoCardGame.toApiCardGame().finished == persistedGame.finished
        mongoCardGame.toApiCardGame().playerWin == persistedGame.playerWin
        mongoCardGame.gameId == persistedGame.gameId
        and: "InMemo game should match persisted game"
        with(persistedGame) {
            def inMemoGame = inMemo.getGame(it.gameId).blockingGet()
            inMemoGame.rounds.size() == it.rounds.size()
            def apiFromInMemo = inMemoGame.toApiCardGame()
            def apiFromEndOfRound = it.toApiCardGame()
            apiFromInMemo.rounds.entrySet().size() == apiFromEndOfRound.rounds.entrySet().size()
        }
    }

}