package casino.gateway;

import casino.api.v1.Operations;
import casino.api.v1.User;
import casino.api.v1.UserCreate;
import casino.client.v1.AccountClient;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
//@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("api/user")
public class AccountGateway implements Operations<User, UserCreate> {

    private final AccountClient client;
    private final Logger logger = LoggerFactory.getLogger(AccountGateway.class);

    public AccountGateway(AccountClient client) {
        this.client = client;
    }

    @Override
    @Post("/")
    @NewSpan("account.create.id")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<User> create(@SpanTag("account.create.id") @Body UserCreate createEntity) {
        logger.info("AccountGateway: create account for username:   {}" , createEntity.getUsername());
        return client.create(createEntity);
    }

    @Override
    @Get("/{id}")
    @NewSpan
    @Secured(SecurityRule.IS_ANONYMOUS)
//    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<User> get(@SpanTag("account.get.id") @PathVariable UUID id) {
        logger.info("AccountGateway: get account:   {}" , id);
        return client.get(id);
    }

    @Override
    @Put("/{id}")
    @NewSpan
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Single<User> update(@SpanTag("account.update.body") @PathVariable UUID id, @Body User updateEntity) {
        logger.info("AccountGateway: update account:   {}" , id );
        return client.update(id, updateEntity);
    }

    @Override
    @Delete("/{id}")
    @NewSpan
    @Secured(SecurityRule.IS_AUTHENTICATED)
//    @Produces(MediaType.TEXT_PLAIN)
    public Single<HttpStatus> delete(@SpanTag("account.delete.id") @PathVariable UUID id) {
        logger.info("AccountGateway: delete account:   {}" , id);
        return client.delete(id);
    }
}
