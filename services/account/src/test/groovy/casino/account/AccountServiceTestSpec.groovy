package casino.account

import casino.account.AccountService
import casino.account.auth.PasswordEncoder
import casino.account.dao.AccountDao
import casino.account.dao.PasswordDao
import casino.account.domain.Account
import casino.api.v1.UserCreate
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class AccountServiceTestSpec extends Specification {


    @Inject
    AccountService service
    @Inject
    AccountDao accDao
    @Inject
    PasswordEncoder encoder;
    @Inject
    PasswordDao passwordDao
    @Shared
    String username = "daoTest"
    @Shared
    String password = "daoTest"
    @Shared
    String email = "daoTest"
    @Shared
    long credit = 1
    @Shared
    Account persistedAccount = null


    def "New User is persisted as Account in account and with userId and password in security"() {
        given: "Create UserCreate"
        def createdUser = new UserCreate(username, email, password, credit)
        when: "Insert User as Account into account DB and security DB"
        persistedAccount = service.createAccount(createdUser).blockingGet()
        then: "check if returned Account is Account"
        persistedAccount instanceof Account
        expect: "Inserted Account in DB matches User created"
        persistedAccount.username == username
        persistedAccount.email == email
        when: "Get same Account with new Query"
        Account createdAccount = accDao.getAccount(persistedAccount.id).blockingGet()
        then: "Should match created Account"
        createdAccount instanceof Account
        createdAccount.username == username
        createdAccount.email == email
        createdAccount.id == persistedAccount.id
        when: "Get encrypted password"
        String passwordFromDb = passwordDao.getPassword(persistedAccount.id).blockingGet()
        then: "Match Encrypted password with PasswordEncoder"
        encoder.matches(password, passwordFromDb)
    }

    def "delete created account"() {
        given: "id of account to delete"
        def id = persistedAccount.id
        when: "delete account from account DB"
        def deleteResult = service.deleteAccount(persistedAccount.id).blockingGet()
        then: "account should be deleted from account DB"
        deleteResult
        when: "password should be deleted from password DB"
        passwordDao.getPassword(id).blockingGet()
        then: "Exception should be thrown"
        thrown(RuntimeException)
    }
}
