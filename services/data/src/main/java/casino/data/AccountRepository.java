package casino.data;

import casino.data.entity.Account;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

//@Repository(value = "default")
@JdbcRepository(dialect = Dialect.MYSQL)
public interface AccountRepository extends CrudRepository<Account, Long> {
    void update(@Id UUID id, String username);
    void update(@Id UUID id, long credit);

}
