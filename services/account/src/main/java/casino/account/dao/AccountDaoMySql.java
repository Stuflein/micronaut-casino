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
                "player, guest) VALUES (?, ?, ?, ?, ?, ?, ? )";
        Tuple tuple = Tuple.of(uuid, username, email, credit, player, guest);
        return client.preparedQuery(sql).rxExecute(tuple).map(RowSet::iterator).flatMap(it -> {
            if (!it.hasNext()) {
                logger.error("insert into account for username: " + username + "  FAILED");
                return Single.error(new SQLException());
            }
            return Single.just(fromSql(it.next()));
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
                        logger.info("Account not in DB:   " + username );
                        return Single.error(new SQLException("Account not in DB"));
                    }
                });
    }

    @Override
    public Single<List<Account>> getAll() {
        String sql = "SELECT * FROM account";
        //List<Account> liste = new ArrayList<>();
        return client.preparedQuery(sql).rxExecute().map(RowSet::iterator).flatMap(rows ->{
            Iterable<Row> it = rows::getDelegate;
          return  Observable.fromIterable(it).map((Row delegateRow) -> {
               return fromSql(Row.newInstance((io.vertx.sqlclient.Row) delegateRow)) ;
            }).toList();
        });
    }

    @Override
    public Single<Account> updateAccount(UUID id, String username, String email, long credit) {
        String sql = "UPDATE account SET username = ?, email = ?, credit = ? WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(username, email, credit, id.toString()))
                .flatMap(rows -> {
                    if (rows.rowCount() == 1) {
                        return getAccount(id).toSingle();
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
                result.getLocalDateTime("last_login").toEpochSecond(ZoneOffset.of("Europe/Berlin")),
                result.getLocalDateTime("created_at").toEpochSecond(ZoneOffset.of("Europe/Berlin")),
                role
        );
    }
}
