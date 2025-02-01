package client;

import client.model.ClientModel;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import client.controller.ClientController;
import client.view.ClientView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import references.Equipment;
import references.Terminal;
import server.ServerSide;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSide extends Application {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 4321;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        launch(args);

        while (true) {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Connected to the server.");

                boolean loggedIn = false;
                String userType = null;

                // Login loop
                while (!loggedIn) {
                    System.out.println("1. Login");
                    System.out.println("2. Sign Up");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();  // Consume newline

                    if (choice == 1) {
                        System.out.print("Enter user type (Student/Admin): ");
                        userType = scanner.nextLine().toLowerCase(); // Convert to lowercase
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
                    } else if (choice == 2) {
                        if (!signUp(scanner, out, in)) {
                            continue;  // Signup failed, go back to login
                        }
                    } else if (choice == 3) {
                        out.println("<exit/>");
                        System.out.println("Disconnecting from server...");
                        return;
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
            case 1:
                handleStudentReservation(scanner);
                break;
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
        System.out.println("1. Create Equipment");
        System.out.println("2. Create Terminal");
        System.out.println("3. Read Resources");
        System.out.println("4. Update Resource");
        System.out.println("5. Delete Resource");
        System.out.println("6. Search Resource");
        System.out.println("7. Generate Report");
        System.out.println("8. Approve Reservation");
        System.out.println("9. Logout");
        System.out.println("10. Exit");
        System.out.print("Choose an option: ");

        int option = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (option) {
            case 1:
                addNewEquipment(scanner);
                break;
            case 2:
                addNewTerminal(scanner);
                break;
            case 3:
                System.out.print("Enter the criteria you want to view (equipment/terminal): ");
                String criteria = scanner.nextLine();
                String result = ServerSide.readAdminResources(criteria);
                System.out.println(result);
                break;
            case 8:
                break;
            case 9:
                out.println("<logout/>");
                System.out.println("Logged out successfully.");
                return false;
            case 10:
                out.println("<exit/>");
                System.out.println("Disconnecting from server...");
                return false;
        }
        return true;
    }

    private static boolean signUp(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter user type (Student/Admin): ");
        String userType = scanner.nextLine().toLowerCase(); // Convert to lowercase
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Send signup request
        String signupRequest = "<signup><userType>" + userType + "</userType><name>" + name + "</name><userID>" + userID + "</userID><password>" + password + "</password></signup>";
        out.println(signupRequest);

        String response = in.readLine();
        System.out.println("Server response: " + response);

        return response.contains("<status>Success</status>");
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image(getClass().getResource("/images/client/app_icon.png").toExternalForm()));
        ClientView view = new ClientView(stage);
        view.runGUI();
        new ClientController(view);
    }

    private static void handleStudentReservation(Scanner scanner) {
        // Gather student information
        System.out.print("Enter Student ID: ");
        String studentID = scanner.nextLine();

        // Start the XML data structure
        StringBuilder xmlData = new StringBuilder();
        xmlData.append("<reservation>\n");
        xmlData.append("    <studID>").append(studentID).append("</studID>\n");

        // Ask the user whether they're borrowing a Terminal or Hardware
        System.out.print("Are you borrowing a Terminal or Hardware? (T/H): ");
        String borrowType = scanner.nextLine().trim().toUpperCase();

        if (!borrowType.equals("T") && !borrowType.equals("H")) {
            System.out.println("Invalid input. Please enter 'T' for Terminal or 'H' for Hardware.");
            return;  // Exit if input is invalid
        }

        // Append borrow type to XML data
        xmlData.append("    <borrowType>").append(borrowType).append("</borrowType>\n");

        // Ask for equipment details
        System.out.print("Enter number of " + (borrowType.equals("T") ? "Terminals" : "Hardware") + ": ");
        int numItems = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        xmlData.append("    <equipment>\n");

        for (int i = 0; i < numItems; i++) {
            System.out.print("Enter Equipment ID for item " + (i + 1) + ": ");
            String equipmentID = scanner.nextLine();

            System.out.print("Enter Amount Borrowed for Equipment ID " + equipmentID + ": ");
            int amountBorrowed = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Append equipment details to XML data
            xmlData.append("        <item>\n");
            xmlData.append("            <equipmentId>").append(equipmentID).append("</equipmentId>\n");
            xmlData.append("            <amountBorrowed>").append(amountBorrowed).append("</amountBorrowed>\n");
            xmlData.append("        </item>\n");
        }

        xmlData.append("    </equipment>\n");
        xmlData.append("</reservation>\n");

        // Call the method from ServerSide with the constructed XML data
        ServerSide.createStudentReservation(xmlData.toString());
        System.out.println("Reservation submitted for admin approval.");
    }

    private static void addNewEquipment(Scanner scanner) {
        System.out.print("Enter Equipment ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Equipment Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Equipment Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter Available Quantity: ");
        int available = scanner.nextInt();

        scanner.nextLine();

        Equipment equipment = new Equipment(id, name, description, quantity, available);

        String equipmentXML = "\n<item>\n" +
                "        <equipmentId>" + equipment.getEquipmentId() + "</equipmentId>\n" +
                "        <equipmentName>" + equipment.getEquipmentName() + "</equipmentName>\n" +
                "        <equipmentDescription>" + equipment.getEquipmentDescription() + "</equipmentDescription>\n" +
                "        <totalQuantity>" + equipment.getTotalQuantity() + "</totalQuantity>\n" +
                "        <availableQuantity>" + equipment.getAvailableQuantity() + "</availableQuantity>\n" +
                "    </item>\n";

        ServerSide.createAdminResource(equipmentXML);
        System.out.println("Equipment added successfully!");
    }

    private static void addNewTerminal(Scanner scanner) {
        System.out.print("Enter Terminal ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Terminal Type (Windows/Linux/mac): ");
        String type = scanner.nextLine();

        System.out.print("Enter Terminal Room Number: ");
        String room = scanner.nextLine();

        System.out.print("Enter Terminal Room Type (Classroom/Open Lab): ");
        String roomType = scanner.nextLine();

        System.out.print("Enter Terminal Status (Available/Reserved/Maintenance/Down): ");
        String status = scanner.nextLine();

        Terminal terminal = new Terminal(id, type, room, roomType, status);

        String terminalXML = "    <terminal>\n" +
                "        <terminalId>" + terminal.getTerminalId() + "</terminalId>\n" +
                "        <terminalType>" + terminal.getTerminalType() + "</terminalType>\n" +
                "        <terminalRoom>" + terminal.getTerminalRoom() + "</terminalRoom>\n" +
                "        <terminalRoomType>" + terminal.getTerminalRoomType() + "</terminalRoomType>\n" +
                "        <terminalStatus>" + terminal.getTerminalStatus() + "</terminalStatus>\n" +
                "    </terminal>\n";
        ServerSide.createAdminTerminalResource(terminalXML);
        System.out.println("Terminal added successfully!");
    }
}
