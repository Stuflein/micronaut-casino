package casino.account

import casino.account.AccountService
import casino.account.AccountServiceImpl
import casino.account.auth.PasswordEncoder
import casino.account.dao.AccountDao
import casino.account.dao.AccountDaoMySql
import casino.account.dao.PasswordDao
import casino.account.dao.PasswordDaoMySql
import casino.account.domain.Account
import casino.api.v1.UserCreate
import io.micronaut.test.extensions.spock.annotation.MicronautTest

//import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class AccountDaoMySqlTest extends Specification {


    @Inject
    AccountService accDao
    @Inject
    PasswordEncoder encoder;
    @Inject
    PasswordDao passwordDao
    @Shared String username = "eins"
    @Shared String password = "eins"
    @Shared String email = "eins"
    @Shared long credit = 1


    def "New User is persisted as Account in account and with userId and password in security"() {
        given: "Create UserCreate"
        UserCreate createdUser = new UserCreate(username, email, password, credit)
        when: "Insert User as Account into account DB and security DB"
        def result = accDao.createAccount(createdUser).blockingGet()
        then: "check if returned Account is Account"
        result instanceof Account
        expect: "Inserted Account in DB matches User created"
        result.username == username
        result.email == email
        when: "Get same Account with new Query"
        Account getAccount = accDao.getAccount(result.id).blockingGet()
        then: "Should match created Account"
        getAccount instanceof Account
        getAccount.username == username
        getAccount.email == email
        when: "Get encrypted password"
        String passwordFromDb = passwordDao.getPassword(result.id).blockingGet()
        then: "Match Encrypted password with PasswordEncoder"
        encoder.matches(password, passwordFromDb)
    }
    def "Username or Email already taken"(){
        given: "Create 2 Users, one has Username, other email already taken"
        UserCreate createdUser1 = new UserCreate("other", email, password, credit)
        UserCreate createdUser2 = new UserCreate(username, "other", password, credit)
        when: "try insert in DB"
        def resultUser1 = accDao.createAccount(createdUser1).blockingGet()
        def resultUser2 = accDao.createAccount(createdUser2).blockingGet()
        then: "Both should fail"
        resultUser1 instanceof Exception
        resultUser2 instanceof Exception
    }
}
