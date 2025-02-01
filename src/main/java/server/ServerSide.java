package server;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ServerSide {

    private static final int PORT = 4321;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for client connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private String userType = null; // Tracks the logged-in user type (Student or Admin)
        private String userID = null;  // Tracks the logged-in user ID

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String clientRequest;

                while ((clientRequest = in.readLine()) != null) {
                    System.out.println("Received: " + clientRequest);
                    String response = processRequest(clientRequest);
                    out.println(response);

                    // End session if the client disconnects or logs out
                    if (clientRequest.equalsIgnoreCase("<logout/>")) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String processRequest(String requestXML) {
            try {
                // Parse the XML request
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document requestDoc = builder.parse(new ByteArrayInputStream(requestXML.getBytes()));

                String action = requestDoc.getDocumentElement().getTagName();

                if (action.equals("login")) {
                    String userType = requestDoc.getElementsByTagName("userType").item(0).getTextContent();
                    String userID = requestDoc.getElementsByTagName("userID").item(0).getTextContent();
                    String password = requestDoc.getElementsByTagName("password").item(0).getTextContent();

                    // Authenticate user
                    if (login(userType, userID, password)) {
                        this.userType = userType;
                        this.userID = userID;
                        return "<response><status>Success</status><message>Welcome, " + userID + "!</message></response>";
                    } else {
                        return "<response><status>Failure</status><message>Invalid credentials</message></response>";
                    }
                }

                if (action.equals("signup")) {
                    String name = requestDoc.getElementsByTagName("name").item(0).getTextContent();
                    String userType = requestDoc.getElementsByTagName("userType").item(0).getTextContent();
                    String userID = requestDoc.getElementsByTagName("userID").item(0).getTextContent();
                    String password = requestDoc.getElementsByTagName("password").item(0).getTextContent();

                    // Handle user signup
                    if (signup(userType, name, userID, password)) {
                        return "<response><status>Success</status><message>Signup successful! You can now log in.</message></response>";
                    } else {
                        return "<response><status>Failure</status><message>User ID already exists</message></response>";
                    }
                }

                // Ensure the user is logged in before processing further requests
                if (this.userType == null || this.userID == null) {
                    return "<response><status>Failure</status><message>Please log in first</message></response>";
                }

                // Handle Student operations
                if (this.userType.equals("Student")) {
                    switch (action) {
                        case "createReservation":
                            createStudentReservation(requestXML);
                            return "<response><status>Success</status><message>Reservation created</message></response>";
                        case "readReservations":
                            return readStudentReservations(this.userID);
                        case "updateReservation":
                            updateStudentReservation(requestXML);
                            return "<response><status>Success</status><message>Reservation updated</message></response>";
                        case "deleteReservation":
                            deleteStudentReservation(requestDoc.getElementsByTagName("reservationID").item(0).getTextContent());
                            return "<response><status>Success</status><message>Reservation deleted</message></response>";
                        case "searchReservations":
                            return searchStudentReservations(requestXML);
                        default:
                            return "<response><status>Failure</status><message>Invalid action for Student</message></response>";
                    }
                }

                // Handle Admin operations
                if (this.userType.equals("Admin")) {
                    switch (action) {
                        case "createResource":
                            createAdminResource(requestXML);
                            return "<response><status>Success</status><message>Resource created</message></response>";
                        case "readResources":
                            return readAdminResources(requestXML);
                        case "updateResource":
                            updateAdminResource(requestXML);
                            return "<response><status>Success</status><message>Resource updated</message></response>";
                        case "deleteResource":
                            deleteAdminResource(requestDoc.getElementsByTagName("resourceID").item(0).getTextContent());
                            return "<response><status>Success</status><message>Resource deleted</message></response>";
                        case "searchResources":
                            return searchAdminResources(requestXML);
                        case "generateReport":
                            String startDate = requestDoc.getElementsByTagName("startDate").item(0).getTextContent();
                            String endDate = requestDoc.getElementsByTagName("endDate").item(0).getTextContent();
                            return generateAdminReport(startDate, endDate);
                        case "approveReservation":
                            String reservationID = requestDoc.getElementsByTagName("reservationID").item(0).getTextContent();
                            boolean isApproved = Boolean.parseBoolean(requestDoc.getElementsByTagName("isApproved").item(0).getTextContent());
                            approveReservation(reservationID, isApproved);
                            return "<response><status>Success</status><message>Reservation processed</message></response>";
                        default:
                            return "<response><status>Failure</status><message>Invalid action for Admin</message></response>";
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "<response><status>Failure</status><message>Error processing request</message></response>";
            }

            return "<response><status>Failure</status><message>Unknown error</message></response>";
        }
    }

    public static boolean login(String userType, String userID, String password) {
        try {
            // Convert userType to lowercase for case-insensitive comparison
            userType = userType.toLowerCase();

            // Load the users.xml file
            Document document = loadXML("src/main/resources/data/users.xml");

            // Parse the students or admins based on userType
            NodeList users = document.getElementsByTagName(userType);

            // Loop through the users and check credentials
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);

                String id = user.getElementsByTagName("ID").item(0).getTextContent();
                String pass = user.getElementsByTagName("Password").item(0).getTextContent();

                // Validate user credentials
                if (id.equals(userID) && pass.equals(password)) {
                    return true;  // User found with matching credentials
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Invalid credentials
    }

    public static boolean signup(String userType, String name, String userID, String password) {
        try {
            // Convert userType to lowercase for case-insensitive comparison
            userType = userType.toLowerCase();

            // Load the users.xml file
            Document document = loadXML("src/main/resources/data/users.xml");

            // Check if user ID already exists
            NodeList users = document.getElementsByTagName(userType);
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String id = user.getElementsByTagName("ID").item(0).getTextContent();
                if (id.equals(userID)) {
                    return false; // User ID already exists
                }
            }

            // Create new user element
            Element newUser = document.createElement(userType);
            Element nameElement = document.createElement("Name");
            nameElement.setTextContent(name);
            Element idElement = document.createElement("ID");
            idElement.setTextContent(userID);
            Element passwordElement = document.createElement("Password");
            passwordElement.setTextContent(password);

            newUser.appendChild(nameElement);
            newUser.appendChild(idElement);
            newUser.appendChild(passwordElement);

            // Append the new user to the root element
            document.getDocumentElement().appendChild(newUser);

            // Debugging: Print the XML before saving
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult); // Print to console

            // Save the updated XML
            saveXML(document, "src/main/resources/data/users.xml");
            return true; // Signup successful
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Signup failed
    }

    // Utility method to load and parse XML data
    public static Document loadXML(String filePath) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }

    // Utility method to save data to an XML file
    public static void saveXML(Document document, String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }


    //JERS AND GAB
    // Placeholder methods for Student CRUD operations
    public static void createStudentReservation(String xmlData) {
        // Parse XML data and add a new student reservation
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document inputDocument = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            Element root = inputDocument.getDocumentElement();
            String studID = root.getElementsByTagName("studID").item(0).getTextContent();
            NodeList equipmentNodes = root.getElementsByTagName("equipment");

            // Load or create the pending reservations file
            Document pendingDoc = loadOrCreateXML("src/main/resources/data/PendingReservations.xml", "PendingReservations");

            for (int i = 0; i < equipmentNodes.getLength(); i++) {
                Element equipElement = (Element) equipmentNodes.item(i);
                String equipmentId = equipElement.getElementsByTagName("equipmentId").item(0).getTextContent();
                int amountBorrowed = Integer.parseInt(equipElement.getElementsByTagName("amountBorrowed").item(0).getTextContent());

                // Create a new pending record
                Element pendingRecord = pendingDoc.createElement("pendingRecord");
                pendingRecord.appendChild(createElement(pendingDoc, "reservationID", String.valueOf(System.currentTimeMillis())));
                pendingRecord.appendChild(createElement(pendingDoc, "studentID", studID));
                pendingRecord.appendChild(createElement(pendingDoc, "equipmentID", equipmentId));
                pendingRecord.appendChild(createElement(pendingDoc, "quantity", String.valueOf(amountBorrowed)));
                pendingRecord.appendChild(createElement(pendingDoc, "requestDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date())));

                pendingDoc.getDocumentElement().appendChild(pendingRecord);
            }
            saveXML(pendingDoc, "src/main/resources/data/PendingReservations.xml");

            System.out.println("Reservation submitted for admin approval.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to create an element in the PendingReservations.xml file
    private static Element createElement(Document doc, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent));
        return element;
    }

    public static String readStudentReservations(String studentID) {
        // Retrieve reservations for a specific student as XML
        return "<data></data>";
    }

    public static void updateStudentReservation(String xmlData) {
        // Parse XML data and update an existing student reservation
    }

    public static void deleteStudentReservation(String reservationID) {
        // Delete a student reservation based on reservation ID
    }

    public static String searchStudentReservations(String criteria) {
        // Search and return available reservations for students as XML
        return "<searchResults></searchResults>";
    }


    // SEB AND YANA
    // Placeholder methods for Admin CRUD operations
    public static void createAdminResource(String xmlData) {
        try {
            // Load or create the equipment XML file
            String filePath = "src/main/resources/data/Equipment.xml";
            Document document = loadOrCreateXML(filePath, "Equipment");

            // Parse incoming XML data
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document inputDoc = builder.parse(new ByteArrayInputStream(xmlData.getBytes()));

            Element newEquipment = inputDoc.getDocumentElement();
            Node importedNode = document.importNode(newEquipment, true);

            document.getDocumentElement().appendChild(importedNode);
            saveXML(document, filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String TERMINAL_FILE_PATH = "src/main/resources/data/Terminal.xml";  // Set the path to where you want to store the terminals

    public static void createAdminTerminalResource(String terminalXML) {
        try {
            // Read the existing terminal file, if it exists
            File terminalFile = new File(TERMINAL_FILE_PATH);
            StringBuilder currentXML = new StringBuilder();

            // Check if the file exists
            if (terminalFile.exists()) {
                // Read the existing XML content into the StringBuilder
                currentXML.append(new String(Files.readAllBytes(Paths.get(TERMINAL_FILE_PATH))));

                // Remove the closing </Terminal> tag if it already exists in the file (to add it correctly later)
                int lastTerminalTagIndex = currentXML.lastIndexOf("</Terminal>");
                if (lastTerminalTagIndex != -1) {
                    currentXML.delete(lastTerminalTagIndex, currentXML.length());
                }
            } else {
                // Initialize the file with the root <Terminal> if it doesn't exist
                currentXML.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<Terminal>\n");
            }

            // Append the new terminal XML to the existing XML content
            currentXML.append(terminalXML);

            // Close the root <Terminal> tag at the end
            currentXML.append("</Terminal>");

            // Write the updated content back to the file
            Files.write(Paths.get(TERMINAL_FILE_PATH), currentXML.toString().getBytes());
        } catch (IOException e) {
            System.err.println("Error while saving terminal resource: " + e.getMessage());
        }
    }

    // Load or create an XML file if it does not exist
    public static Document loadOrCreateXML(String filePath, String rootElement) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        if (file.exists()) {
            return builder.parse(file);
        } else {
            Document doc = builder.newDocument();
            Element root = doc.createElement(rootElement);
            doc.appendChild(root);
            try {
                saveXML(doc, filePath);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
            return doc;
        }
    }

    public static String readAdminResources(String criteria) {

        return criteria;
    }

    public static void updateAdminResource(String xmlData) {
        // Parse XML data and update terminal/equipment details
    }

    public static void deleteAdminResource(String resourceID) {
        // Delete terminal/equipment based on resource ID
    }

    public static String searchAdminResources(String criteria) {
        // Search and return terminal/equipment details as XML
        return "<searchResults></searchResults>";
    }

    // Additional Operations for Admin
    public static String generateAdminReport(String startDate, String endDate) {
        // Generate and return an XML report for the specified date range
        return "<report></report>";
    }

    public static boolean approveReservation(String reservationID, boolean isApproved) {
        // Approve or reject a reservation
        return true;
    }
}
