package casino.account.dao;

import casino.account.domain.Account;
import casino.api.v1.UserRole;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Singleton
public class AccountDaoMySql implements AccountDao {

    /**
     * Class for Database Operations for Accounts
     *
     * @param client  vertx.reactivex MySQLPool
     */
    public AccountDaoMySql(MySQLPool client) {
        this.client = client;
    }




  private final  MySQLPool client;

    private final Logger logger = LoggerFactory.getLogger(AccountDaoMySql.class);


    @Override
    public Single<Account> createAccount(UUID id, String username, String email, long credit, boolean player) {
        logger.info("AccountDaoMySql:   createAccount():  username: {}   and email:   {}", username, email);
        String uuid = id.toString();
        boolean guest = !player;
        String sql = "INSERT INTO account (id, username, email, credit, " +
                "player, guest) VALUES (?, ?, ?, ?, ?, ?)";
        logger.info("User with username:  {}   and id:   {}     Try insert Into DB", username, id);
        Tuple tuple = Tuple.of(uuid, username, email, credit, player, guest);
        return client.preparedQuery(sql).rxExecute(tuple)
                .flatMap(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        logger.info("INSERT Account: {}   and id:   {}      COMPLETE", username, id);
                        return getAccount(UUID.fromString(uuid));
                    } else {
                        logger.info("INSERT ACCOUNT FAILED for username   {}   for id:    {} ", username, uuid);
                        return Single.error(new SQLException());
                    }
                });
    }

    @Override
    public Single<Account> getAccount(UUID id) {
        logger.info("AccountDaoMySql:   getAccount() for id:   {}", id);

        String sql = "SELECT * FROM account WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(id))
                .map(RowSet::iterator).flatMap(rowIterator -> {
                    if (rowIterator.hasNext()) {
                        return Single.just(fromSql(rowIterator.next()));
                    } else {
                        return Single.error(new SQLException("Account not in DB"));
                    }
                });
    }

    @Override
    public Single<Account> getAccount(String username) {
        logger.info("AccountDaoMySql:   getAccount() for username:  {}", username);
        String sql = "SELECT * FROM account WHERE username = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(username))
                .map(RowSet::iterator).flatMap(rowIterator -> {
                    if (rowIterator.hasNext()) {
                        return Single.just(fromSql(rowIterator.next()));
                    } else {
                        logger.info("AccountDaoMySql:  Account not in DB:   {}", username);
                        return Single.error(new SQLException("Account not in DB"));
                    }
                });
    }

    @Override
    public Single<Boolean> isNotAccount(String username, String email) {
        logger.info("AccountDaoMySql: isNotAccount()  Check if username:  {}    and/or email:  {}  already in DB", username, email);
        String sql = "SELECT * FROM account WHERE username = ? OR email = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(username, email))
                .map(rowSet -> {
                    logger.info("AccountDaoMySql:  is username and/or email already in DB:   {}", rowSet.iterator().hasNext());
                    return !rowSet.iterator().hasNext();
                });
    }

    @Override
    public Single<List<Account>> getAll() {
        logger.info("AccountDaoMySql:    get All Accounts");
        String sql = "SELECT * FROM account";
        return client.preparedQuery(sql).rxExecute().flatMap(rows -> Observable.fromIterable(rows).map(this::fromSql).toList());
    }


    @Override
    public Single<Account> updateAccount(UUID id, String username, String email, long credit) {
        logger.info("AccountDaoMySql:   updateAccount: new username: {}  new email:  {}   new credit:   {}", username, email, credit);
        String sql = "UPDATE account SET username = ?, email = ?, credit = ? WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(username, email, credit, id.toString()))
                .flatMap(rows -> {
                    if (rows.rowCount() == 1) {
                        return getAccount(id);
                    } else {
                        return Single.error(new SQLException("UPDATE account for User: " + username + "  failed"));
                    }
                });
    }

    @Override
    public Single<Boolean> delete(UUID id) {
        logger.info("AccountDaoMysql:  delete account:  {}", id);
        String sql = "DELETE FROM account WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(id.toString())).map(rows -> rows.rowCount() == 1);
    }

    @Override
    public Single<Boolean> setLastLogin(UUID id, long instance) {
        LocalDateTime lastLogin = LocalDateTime.ofInstant(Instant.ofEpochSecond(instance), ZoneId.of("UTC"));
        logger.info("AccountDaoMysql:  setLastLogin  for account:  :  {}   to:   {}", id, lastLogin);
        String sql = "UPDATE account SET last_login = ? WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(lastLogin, id))
                .map(rowSet -> rowSet.rowCount() == 1);
    }


    private Account fromSql(Row result) {
        UserRole role = null;
        for (UserRole userRole : UserRole.values()) {
            if (Boolean.TRUE.equals(result.getBoolean(userRole.name().toLowerCase()))) {
                role = userRole;
            }
        }
        return new Account(
                UUID.fromString(result.getString("id")),
                result.getString("username"),
                result.getString("email"),
                result.getLong("credit"),
                "",
                result.getBoolean("enabled"),
                result.getBoolean("account_expired"),
                result.getBoolean("account_locked"),
                result.getBoolean("password_expired"),
                result.getLocalDateTime("last_login").toEpochSecond(ZoneOffset.UTC),
                result.getLocalDateTime("created_at").toEpochSecond(ZoneOffset.UTC),
                role
        );
    }
}
