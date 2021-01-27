package casino.account


import casino.api.v1.User
import casino.api.v1.UserCreate
import casino.api.v1.UserLogin
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class AccountControllerTestSpec extends Specification {

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
    UserCreate userCreate = null
    @Shared
    String username = "testName"
    @Shared
    String password = "testPw"
    @Shared
    String email = "testMail"
    @Shared
    long credit = 1
    @Shared
    User registeredUser = null
    @Shared
    def accessToken = null


    def setupSpec() {
        userCreate = new UserCreate(username, email, password, credit)
    }


    def "Create new User "() {
        given: "Create the new User"
        UserCreate newUser = new UserCreate(username, email, password, credit)
        def registerRequest = HttpRequest.POST("/casino/account", newUser)
        when: "Register new User"
        registeredUser = client.toBlocking().exchange(registerRequest, User.class).body()
        then: "User returned should match created User"
        with(registeredUser) {
            it != null
            it.username == newUser.username
            it.email == newUser.email
            it.userId instanceof UUID
        }

    }

    def "Login existing User, get JWT Token"() {
        given: "Create Login-Credentials"
        def loginUser = new UserLogin(username, password)
        def loginReq = HttpRequest.POST("/aplogin", loginUser)
        when: "Try to Login User"

        def response = client.toBlocking().exchange(loginReq, BearerAccessRefreshToken.class)
        accessToken = response.body().getAccessToken()
        then: "HttpStatus should be OK, got JWT Token"

        response.status() == HttpStatus.OK
        response.body().accessToken != null

    }

    def "Create User with username or email already existing"() {
        given: "Create User"
        def newUser1 = new UserCreate(username, "other", password, credit)
        def registerRequest1 = HttpRequest.POST("/casino/account", newUser1)
        when: "Register User"
        client.toBlocking().exchange(registerRequest1)
        then: "Should trigger GlobalErrorHandler and throw HttpClientResponseException"
        thrown(HttpClientResponseException)
    }

    def "Get User info"() {
        when: "Registered User requests info with access Token"
        def infoRequest = HttpRequest.GET("casino/account/$registeredUser.userId").header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        def userInfo = client.toBlocking().exchange(infoRequest, User.class).body()
        then: "Info should match created User"
        userInfo.email == email
        userInfo.username == username
    }

    def "Update Registered Users email and username"() {
        when: "Request Change name and email"
        def updatedUser = new User(registeredUser.userId, "newName", "newEmail", 1)
        def updateRequest = HttpRequest.PUT("/casino/account/$registeredUser.userId", updatedUser).header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
        def returnedUpdatedUser = client.toBlocking().exchange(updateRequest, User.class)
        then:
        returnedUpdatedUser.body().username == updatedUser.username
        returnedUpdatedUser.body().email == updatedUser.email
        returnedUpdatedUser.body().credit == updatedUser.credit
        client.toBlocking().exchange(HttpRequest.GET("casino/account/$registeredUser.userId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken"), User.class)
                .body().username == updatedUser.username

    }

    def "delete User"() {
        given: "Request to delete actual User"
        def deleteRequest = HttpRequest.DELETE("/casino/account/$registeredUser.userId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")

        when: "delete previously created User"
        def successDeleted = client.toBlocking().exchange(deleteRequest)
        then: "User should be deleted"
        successDeleted
    }

}
