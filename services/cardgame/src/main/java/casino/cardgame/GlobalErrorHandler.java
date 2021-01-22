package casino.cardgame;

import casino.cardgame.exceptions.NotEnoughFundsException;
import casino.cardgame.exceptions.PersistMongoHandException;
import io.lettuce.core.RedisCommandExecutionException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        if (exception instanceof PersistMongoHandException){
            return HttpResponse.serverError(m);
        } else if(exception instanceof NotEnoughFundsException){
            m.put("wager", ((NotEnoughFundsException) exception).getWager() + "");
            return HttpResponse.unauthorized().body(m);
        } else if(exception instanceof RedisCommandExecutionException){
            return HttpResponse.serverError(m);
        }
        else {
            logger.info("Caught other Exception:     " + exception.getMessage());
            m.put("Undefined", "Exception in casino-account Service");
            return HttpResponse.serverError().body(m);
        }
    }
}
