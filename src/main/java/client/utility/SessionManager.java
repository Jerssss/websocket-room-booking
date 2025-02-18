package client.utility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class SessionManager {
    // Thread-safe map to store sessions: <SessionToken, UserID>
    public static final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    // Creates a session and stores it
    public static void createSession(String sessionToken, String userId) {
        activeSessions.put(sessionToken, userId);
        SessionManager.sessionToken = sessionToken; // Store the session token globally
    }

    // Gets the user ID for a given session token
    public static String getUserId(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    // Invalidates a session
    public static void invalidateSession(String sessionToken) {
        activeSessions.remove(sessionToken);
        // Optionally clear the global sessionToken
        if (SessionManager.sessionToken != null && SessionManager.sessionToken.equals(sessionToken)) {
            SessionManager.sessionToken = null;
        }
    }

    // Method to check if a session is valid
    public static boolean isValidSession(String sessionToken) {
        return activeSessions.containsKey(sessionToken);
    }

    // Optionally, you can add a method to directly get the active session token if required:
    public static String getActiveSessionToken() {
        return sessionToken;
    }
}
