package casino.client.v1;

import io.micronaut.http.client.annotation.Client;

import javax.inject.Singleton;

@Singleton
@Client(id = "account-casino")
public interface InternalAccountClient {

}
