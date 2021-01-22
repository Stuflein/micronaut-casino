package casino.cardgame

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.security.token.jwt.render.BearerTokenRenderer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class CardGameControllerTestSpec extends Specification {

    @Shared
    @AutoCleanup
    @Inject
    EmbeddedServer embeddedServer
    @Shared
    @AutoCleanup
    @Inject
    @Client("/")
    RxHttpClient client
    @Shared
    BearerAccessRefreshToken token = BearerAccessRefreshToken.

    def "start game "(){

        when: "play game with random UUID as player"
        def playerId = UUID.randomUUID()
        def endOfGame = HttpRequest.POST("/casino/cardgame/")
        then: "persisted game should have same winner"
        endOfGame.playerWin == controller.getGamePlayerLoose(playerId).blockingGet().playerWin
    }

}