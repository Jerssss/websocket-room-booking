package client.utility;
import java.util.UUID;

public class SessionTokenGenerator {
    public static String generateUniqueToken() {
        return UUID.randomUUID().toString();
    }
}
