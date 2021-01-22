package casino.account;

import casino.account.exceptions.AccountAlreadyExistsException;
import casino.account.exceptions.RefreshTokenException;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.security.authentication.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Produces
@Singleton
@Requires(classes = {Exception.class, ExceptionHandler.class})
public class GlobalErrorHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    private final Logger logger = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        logger.info("Global Exception Handler triggered");
        logger.info(exception.toString());
        logger.info(exception.getMessage());
        logger.info(exception.getClass().toString());
        logger.info("suppressed:  "  + Arrays.toString(exception.getSuppressed()));
        logger.error("Stacktrace: ", exception);
        Map<String, String> m = new HashMap<>();
        m.put("message", exception.getMessage());
        if (exception instanceof AuthenticationException) {
//            Map<String, String> m = new HashMap<>();
//            m.put("message", exception.getMessage());
            logger.info(exception.getMessage());
            return HttpResponse.unauthorized().body(m);
        } else if (exception instanceof AccountAlreadyExistsException) {
//            Map<String, String> m = new HashMap<>();
//            m.put("message", exception.getMessage() );
            return HttpResponse.badRequest().body(m);
        } else if (exception instanceof RefreshTokenException) {
            return HttpResponse.serverError(m);
        } else if (exception instanceof SQLException) {
//            Map<String, String> m = new HashMap<>();
//            m.put("message", exception.getMessage() );
            return HttpResponse.serverError(m);

        } else {
            logger.info("Caught other Exception:     " + exception.getMessage());
            m.put("Undefined", "Exception in casino-account Service");
            return HttpResponse.serverError().body(m);
        }
    }
}
