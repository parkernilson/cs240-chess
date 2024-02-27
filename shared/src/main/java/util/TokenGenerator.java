package util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    public static String generateToken() {
        byte[] tokenBytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
