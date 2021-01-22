package casino.account.auth;

import casino.account.dao.AccountDao;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class CasinoAuthoritiesFetcherImpl implements CasinoAuthoritiesFetcher {
    @Inject
    AccountDao accountDao;

    @Override
    public Publisher<List<String>> findAuthoritiesByUsername(String username) {

        return accountDao.getAccount( username).map(account -> {

            List<String> userRoleList = new ArrayList<>();
            userRoleList.add(account.getRole().toString());
            return userRoleList;
        }).toFlowable();
    }

}
