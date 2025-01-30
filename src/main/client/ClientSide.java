package main.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSide {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 4321;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Connected to the server.");

                boolean loggedIn = false;
                String userType = null;

                // Login loop
                while (!loggedIn) {
                    System.out.print("Enter user type (Student/Admin): ");
                    userType = scanner.nextLine();
                    System.out.print("Enter user ID: ");
                    String userID = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    // Send login request
                    String loginRequest = "<login><userType>" + userType + "</userType><userID>" + userID + "</userID><password>" + password + "</password></login>";
                    out.println(loginRequest);

                    String response = in.readLine();
                    System.out.println("Server response: " + response);

                    if (response.contains("<status>Success</status>")) {
                        loggedIn = true;
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                    }
                }

                // After login, show options for the specific user type
                if (userType.equalsIgnoreCase("Student")) {
                    if (!showStudentOptions(scanner, out, in)) {
                        continue;  // Logout or exit, go back to login
                    }
                } else if (userType.equalsIgnoreCase("Admin")) {
                    if (!showAdminOptions(scanner, out, in)) {
                        continue;  // Logout or exit, go back to login
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection issue: " + e.getMessage());
                break;
            }
        }
        scanner.close();
    }

    private static boolean showStudentOptions(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("\nStudent Options:");
        System.out.println("1. Create Reservation");
        System.out.println("2. Read Reservations");
        System.out.println("3. Update Reservation");
        System.out.println("4. Delete Reservation");
        System.out.println("5. Search Reservations");
        System.out.println("6. Logout");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (option) {
            case 6:
                out.println("<logout/>");
                System.out.println("Logged out successfully.");
                return false;
            case 7:
                out.println("<exit/>");
                System.out.println("Disconnecting from server...");
                return false;
            // Handle other options...
        }
        return true;
    }

    private static boolean showAdminOptions(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("\nAdmin Options:");
        System.out.println("1. Create Resource");
        System.out.println("2. Read Resources");
        System.out.println("3. Update Resource");
        System.out.println("4. Delete Resource");
        System.out.println("5. Search Resources");
        System.out.println("6. Generate Report");
        System.out.println("7. Approve Reservation");
        System.out.println("8. Logout");
        System.out.println("9. Exit");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (option) {
            case 8:
                out.println("<logout/>");
                System.out.println("Logged out successfully.");
                return false;
            case 9:
                out.println("<exit/>");
                System.out.println("Disconnecting from server...");
                return false;
            // Handle other options...
        }
        return true;
    }
}
