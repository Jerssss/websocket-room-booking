package server;

import java.io.*;
import java.net.*;
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
            // Load the users.xml file
            Document document = loadXML("C:\\Users\\krist\\IdeaProjects\\9444-team1_preproject\\src\\users.xml");

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
        // Parse XML data and add a new terminal/equipment
    }

    public static String readAdminResources(String criteria) {
        // Retrieve terminal/equipment details as XML
        return "<data></data>";
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
