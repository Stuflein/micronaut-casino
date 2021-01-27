package casino.account;

import casino.account.domain.Account;
import casino.account.exceptions.AccountAlreadyExistsException;
import casino.api.v1.Operations;
import casino.api.v1.User;
import casino.api.v1.UserCreate;
import casino.api.v1.UserRole;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
//@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("casino/account")
public class AccountController implements Operations<User, UserCreate> {

    /**
     * Simple Controller for CRUD Operations,
     * secured via Id check in Header
     * Accessible only via Gateway
     *
     *  accountService  the Service to connect Controller with Database
     *  securityService provided by Micronaut
     */

    @Inject
    protected AccountService accountService; //protected so no reflection
    @Inject
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Override
    @Post("/")
    @ContinueSpan
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<User> create(@SpanTag("account.create.id") @Body UserCreate createEntity) {
        logger.info("AccountController:  create Account for username:  {}  and email:  {}", createEntity.getUsername(), createEntity.getEmail());
        return accountService.isNotAccount(createEntity.getUsername(), createEntity.getEmail())
                .flatMap((Boolean isNotAccount) -> {
                    if (Boolean.TRUE.equals(isNotAccount)) {
                        logger.info("AccountController:  create Account for username: Account CREATED ");
                        return accountService.createAccount(createEntity).map(Account::getApiUser);
                    } else {
                        logger.info("AccountController:  create Account for username: Account NOT CREATED ");
                        return Single.error(new AccountAlreadyExistsException("Username: " + createEntity.getUsername() + ",   and/or Email: " + createEntity.getEmail() + "  already used"));
                    }
                });
    }

    @Override
    @Get("/{id}")
    @ContinueSpan
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<User> get(@SpanTag("account.get.id") @PathVariable UUID id) {
        logger.info("AccountController:  get Account {}  ", id);
        UUID accountId = UUID.fromString(securityService.getAuthentication().orElseThrow(() -> new AuthenticationException("Get Account " + id + ":   Not authenticated Account"))
                .getAttributes().get("id").toString());
        if (accountId.equals(id)) {
            return accountService.getAccount(id).map(Account::getApiUser);
        } else {
            return Single.error(new AuthenticationException("Get Account: " + id + ":  Not Account Owner"));
        }
    }

    @Override
    @Put("/{id}")
    @ContinueSpan
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Single<User> update(@SpanTag("account.update.body") @PathVariable UUID id, @Body User updateEntity) {
        logger.info("AccountController:  update Account  {}", id);
        UUID accountId = UUID.fromString(securityService.getAuthentication().orElseThrow(() -> new AuthenticationException("Update Account " + id + ":   Not authenticated Account"))
                .getAttributes().get("id").toString());
        if (accountId.equals(id)) {
            return accountService.updateAccount(id, updateEntity.getUsername(), updateEntity.getEmail(), updateEntity.getCredit())
                    .map(Account::getApiUser);
        } else {
            return Single.error(new AuthenticationException("Update Account: " + id + " -> Not Account Owner"));
        }
    }

    @Override
    @Delete("/{id}")
    @ContinueSpan
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<HttpStatus> delete(@SpanTag("account.delete.id") @PathVariable UUID id) {
        logger.info("AccountController:   delete  {}", id);
        UUID accountId = UUID.fromString(securityService.getAuthentication().orElseThrow(() -> new AuthenticationException("Delete Account " + id + ":   Not authenticated Account"))
                .getAttributes().get("id").toString());
        if (accountId.equals(id)) {
            return accountService.deleteAccount(id).map((Boolean b) -> {
                if (Boolean.TRUE.equals(b)) return HttpStatus.OK;
                else return HttpStatus.UNAUTHORIZED;
            });
        } else {
            return Single.error(new AuthenticationException("Delete Account: " + id + " -> Not Account Owner"));
        }
    }

    @Get("/all")
    @ContinueSpan
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Single<List<User>> getAll() {
        logger.info("AccountController:  getAll()");
        if (securityService.getAuthentication().isPresent()) {
            UUID accountId = UUID.fromString(securityService.getAuthentication().orElseThrow(() -> new AuthenticationException("Get all Accounts :   Not authenticated Account"))
                    .getAttributes().get("id").toString());
            return accountService.getAccount(accountId).flatMap(account -> {
                if (account.getRole() == UserRole.ADMIN) {
                    return accountService.getAll().map(accountList -> accountList
                            .stream().map(Account::getApiUser).collect(Collectors.toList()));
                } else {
                    return Single.error(new AuthenticationException("Only Admin allowed, User: " + accountId + "  is not Admin"));
                }
            });
        } else return Single.error(new AuthenticationException("Only Admin allowed, User ist not Auhtneticated"));
    }

}
