package casino.account.dao;

import casino.account.exceptions.RefreshTokenException;
import io.reactivex.Single;
import io.vertx.reactivex.mysqlclient.MySQLPool;
import io.vertx.reactivex.sqlclient.Tuple;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
//
//@Singleton
//public class RefreshTokenDaoMySql implements RefreshTokenDao {
//
//    @Inject
//    MySQLPool client;
//
//
//    @Override
//    public Single<String> getToken(UUID userId) {
//        String sql = "SELECT * FROM token WHERE id = ?";
//        return client.preparedQuery(sql).rxExecute(Tuple.of(userId.toString()))
//                .flatMap(rowSet -> {
//                    if (rowSet.iterator().hasNext()) {
//                        return Single.just(rowSet.iterator().next().getString("refresh_token"));
//                    } else {
//                        return Single.error(new RefreshTokenException("No Token for UserId", userId));
//                    }
//                });
//    }
//
//    @Override
//    public Single<UUID> getUserID(String token) {
//        String sql =  "SELECT " ;
//    }
//
//
//    @Override
//    public void setToken(UUID userId, String refreshToken) {
//        String sql = "INSERT INTO token (id, refresh_token) VALUES (?, ?)" +
//                "ON DUPLICATE KEY UPDATE refresh_token = ?";
//        client.preparedQuery(sql).rxExecute(Tuple.of(userId.toString(), refreshToken, refreshToken))
//                .map(rows -> {
//                    if (rows.rowCount() != 1) {
//                        throw new RefreshTokenException("RefreshToken for user " + userId + " not persisted Error", userId);
//                    }
//                    return rows;
//                });
//    }
//
//
//    @Override
//    public Single<Boolean> revokeToken(UUID userId) {
//
//        String sql = "UPDATE token SET revoked = ? WHERE id = ?";
//        return client.preparedQuery(sql).rxExecute(Tuple.of(false, userId))
//                .map(rowSet -> rowSet.rowCount() == 1);
//    }
//
//    @Override
//    public Single<Boolean> deleteToken(UUID userId) {
//
//        String sql = "DELETE FROM token WHERE id = ?";
//        return client.preparedQuery(sql).rxExecute(Tuple.of(userId.toString()))
//                .map(rowSet -> rowSet.rowCount() == 1);
//    }
//}
