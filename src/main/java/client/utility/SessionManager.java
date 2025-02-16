package client.utility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // Thread-safe map to store sessions: <SessionToken, UserID>
    private static final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    public static void createSession(String sessionToken, String userId) {
        activeSessions.put(sessionToken, userId);
    }

    public static String getUserId(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    public static void invalidateSession(String sessionToken) {
        activeSessions.remove(sessionToken);
    }
}