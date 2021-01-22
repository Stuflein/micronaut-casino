package casino.account;

import casino.api.v1.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountCache {

    private Map<String, String> convertUserToMap(User user) {
        Map<String, String> map = new HashMap<>();
        map.put("id", user.getUserId().toString());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("credit", String.valueOf(user.getCredit()));
        return map;
    }

    private User convertMapToUser(Map<String, String> userMap) {
        return new User(
                UUID.fromString(userMap.get("id")),
                userMap.get("username"),
                userMap.get("email"),
                Long.parseLong(userMap.get("credit"))
        );
    }
}
