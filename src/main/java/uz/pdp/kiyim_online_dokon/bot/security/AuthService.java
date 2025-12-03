
package uz.pdp.kiyim_online_dokon.bot.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    // Chat ID → JWT token
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public void saveToken(String chatId, String token) {
        tokenStore.put(chatId, token);
    }

    public String getToken(String chatId) {
        return tokenStore.get(chatId);
    }

    public void removeToken(String chatId) {
        tokenStore.remove(chatId);
    }
}
