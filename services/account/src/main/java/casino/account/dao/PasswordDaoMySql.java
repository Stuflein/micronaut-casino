package casino.account.dao;

import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowIterator;
import io.vertx.reactivex.sqlclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.login.CredentialNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Singleton
public class PasswordDaoMySql implements PasswordDao {
    /**
     * Class for simple CRUD operations in password Database
     * client  vertx.reactivex MySQLPool
     */

    private final Logger logger = LoggerFactory.getLogger(PasswordDaoMySql.class);
    @Inject
    MySQLPool client;


    @Override
    public Single<Boolean> addPassword(@NotNull UUID id, @NotNull String password) {
        logger.info("PasswordDaoMySql:  addPassword() for id:    {}", id);
        String sql = "INSERT INTO security (id, password) VALUES (?, ?)";
        return client.preparedQuery(sql).rxExecute(Tuple.of(id.toString(), password)).map(a -> a.rowCount() == 1);
    }

    @Override
    public Single<String> getPassword(UUID id) {
        logger.info("PasswordDaoMySql:  getPassword() for id:  {}", id);
        String sql = "SELECT password FROM security WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(id)).map(rows -> {
            RowIterator<Row> ite = rows.iterator();
            if (ite.hasNext()) {
                return ite.next().getString("password");
            } else {
                logger.info("PasswordDaoMySql:  getPassword() for id:  {}    no password in db", id);
                throw new CredentialNotFoundException("No password for accountId: " + id);
            }
        });
    }

    @Override
    public Single<Boolean> setPassword(UUID id, String password) {
        logger.info("PasswordDaoMySql:  setPassword()  for account:  {}", id);
        return null;
    }//TODO:setPassword

    @Override
    public Single<Boolean> deletePassword(UUID id) {
        logger.info("PasswordDaoMySql:  deletePassword() for id:   {}", id);
        String sql = "DELETE FROM security WHERE id = ?";
        return client.preparedQuery(sql).rxExecute(Tuple.of(id.toString())).map(rowSet -> rowSet.rowCount() == 1);
    }
}
