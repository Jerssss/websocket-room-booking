package client.utility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static String sessionToken;
    // Thread-safe map to store sessions: <SessionToken, UserID>
    public static final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    public static void createSession(String sessionToken, String userId) {
        activeSessions.put(sessionToken, userId);
        SessionManager.sessionToken = sessionToken; // Store the session token globally
    }

    public static String getUserId(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    public static void invalidateSession(String sessionToken) {
        activeSessions.remove(sessionToken);
        if (SessionManager.sessionToken != null && SessionManager.sessionToken.equals(sessionToken)) {
            SessionManager.sessionToken = null;
        }
    }

    // Method to check if a session is valid
    public static boolean isValidSession(String sessionToken) {
        return activeSessions.containsKey(sessionToken);
    }
    public static String getActiveSessionToken() {
        return sessionToken;
    }

}