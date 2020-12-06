package casino.account.dao;

import casino.account.domain.Account;
import casino.api.v1.UserRole;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowIterator;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Singleton
public class AccountDaoMySql implements AccountDao {

    @Inject
    MySQLPool client;

    private Logger logger = LoggerFactory.getLogger(AccountDaoMySql.class);


    @Override
    public Single<Account> createAccount(String username, String email, long credit, boolean player) {
        String uuid = UUID.randomUUID().toString();
        boolean guest = !player;
        String sql = "INSERT INTO account (id, username, email, credit, " +
                "player, guest) VALUES (?, ?, ?, ?, ?, ?)";
        Tuple tuple = Tuple.of(uuid, username, email, credit, player, guest);
        return client.preparedQuery(sql).rxExecute(tuple)
                .flatMap(rowSet -> {
                    if (rowSet.rowCount() == 1) {
                        logger.info("INSERT COMPLETE");
                        return getAccount(UUID.fromString(uuid));
                    } else {
                        logger.info("INSERT ACCOUNT FAILED for username " + username + " for id:  " + uuid );
                        return Single.error(new SQLException());
                    }
                });
    }

    @Override
    public Single<Account> getAccount(UUID id) {

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
        String sql = "SELECT * FROM account WHERE username = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(username))
                .map(RowSet::iterator).flatMap(rowIterator -> {
                    if (rowIterator.hasNext()) {
                        return Single.just(fromSql(rowIterator.next()));
                    } else {
                        logger.info("Account not in DB:   " + username);
                        return Single.error(new SQLException("Account not in DB"));
                    }
                });
    }

    @Override
    public Single<List<Account>> getAll() {
        String sql = "SELECT * FROM account";
        return client.preparedQuery(sql).rxExecute().flatMap(rows -> {


            Iterable<Row> it = rows::iterator;
            return Observable.fromIterable(it).map(this::fromSql).toList() ;
        });
    }

    @Override
    public Single<Account> updateAccount(UUID id, String username, String email, long credit) {
        String sql = "UPDATE account SET username = ?, email = ?, credit = ? WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(username, email, credit, id.toString()))
                .flatMap(rows -> {
                    if (rows.rowCount() == 1) {
                        return getAccount(id);
                    } else {
                        return Single.error(new SQLException("UPDATE account failed"));
                    }
                });
    }

    @Override
    public Single<Boolean> delete(UUID id) {
        String sql = "DELETE FROM account WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(id.toString())).flatMap(rows -> {
            if (rows.rowCount() != 1) {
                return Single.just(false);
            } else {
                return Single.just(true);
            }
        });
    }

    private Account fromSql(Row result) {
        UserRole role = null;
        for (UserRole userRole : UserRole.values()) {
            if (result.getBoolean(userRole.name().toLowerCase())) {
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
