package server;

import client.utility.ServerConnectionManager;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;
import server.admin.AddNewTerminalProcessor;
import server.admin.ViewStudentReservationsProcessor;
import server.student.ModifyReservationProcessor;
import server.student.ViewReservationProcessor;
import server.utility.Reservation;
import server.utility.StudentReservation;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private String currentUser = null; // Track the current logged-in user

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("Welcome to the Server! Type 'exit' to logout.");
            String clientMessage;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from Client: " + clientMessage);

                // Handle exit command
                if ("exit".equalsIgnoreCase(clientMessage)) {
                    System.out.println(currentUser + " has logged out.");
                    writer.println("<Response><Status>SUCCESS</Status><Message>Goodbye!</Message></Response>");
                    break;
                }

                // Handle various requests using a switch-style approach
                try {
                    if (clientMessage.contains("<Login>")) {
                        handleLogin(clientMessage, writer);
                    } else if (clientMessage.contains("<SignUp>")) {
                        handleSignUp(clientMessage, writer);
                    } else if (clientMessage.contains("<AddTerminal>")) {
                        handleAddTerminal(clientMessage, writer);
                    } else if (clientMessage.contains("<Request><Type>ViewStudentReservations</Type></Request>")) {
                        handleViewStudentReservations(writer);
                    } else if (clientMessage.contains("<Type>FilterReservations</Type>")) {
                        String startDate = extractField(clientMessage, "<StartDate>", "</StartDate>");
                        String endDate = extractField(clientMessage, "<EndDate>", "</EndDate>");

                        if (startDate != null && endDate != null) {
                            handleFetchReservations(writer, startDate, endDate);
                        } else {
                            writer.println("<Response><Status>ERROR</Status><Message>Invalid date range</Message></Response>");
                        }
                    }
                    else if (clientMessage.startsWith("fetch_reservation")) {
                        handlefetchreservation(writer);
                    } else if (clientMessage.startsWith("fetch_reservations")) {
                        handleUpdateReservation( writer);
                    } else {
                        writer.println("<Response><Status>ERROR</Status><Message>Invalid Request.</Message></Response>");
                    }
                } catch (Exception e) {
                    writer.println("<Response><Status>ERROR</Status><Message>Malformed Request</Message></Response>");
                    System.out.println("Error processing client message: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles user login requests.
     */
    private void handleLogin(String clientMessage, PrintWriter writer) {
        // Extract fields
        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
        String password = extractField(clientMessage, "<Password>", "</Password>");
        String userType = extractField(clientMessage, "<UserType>", "</UserType>");

        // Validate fields
        if (userID == null || password == null || userType == null) {
            writer.println("<Response><Status>ERROR</Status><Message>Invalid login request. Missing fields.</Message></Response>");
            return;
        }

        // Attempt login
        String userName = LoginProcessor.getUserName(userID, password, userType);
        if (userName != null) {
            writer.println(String.format(
                    "<Response><Status>SUCCESS</Status><Name>%s</Name><Message>Login Successful</Message></Response>",
                    userName
            ));
            currentUser = userID;
            ServerConnectionManager.serverConnection.setLoggedInUserId(userID);  // Set the logged-in user ID
        } else {
            writer.println("<Response><Status>FAILURE</Status><Message>Invalid Credentials</Message></Response>");
        }
    }



    /**
     * Handles user sign-up requests.
     */
    private void handleSignUp(String clientMessage, PrintWriter writer) {
        // Extract fields
        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
        String name = extractField(clientMessage, "<Name>", "</Name>");
        String password = extractField(clientMessage, "<Password>", "</Password>");
        String userType = extractField(clientMessage, "<UserType>", "</UserType>");
        String courseYear = extractField(clientMessage, "<CourseYear>", "</CourseYear>");
        String facultyType = extractField(clientMessage, "<FacultyType>", "</FacultyType>");

        // Validate fields
        if (userID == null || name == null || password == null || userType == null) {
            writer.println("<Response><Status>ERROR</Status><Message>Missing fields for sign-up.</Message></Response>");
            return;
        }

        // Register user
        boolean isRegistered = SignUpProcessor.registerUser(userID, name, password, userType, courseYear, facultyType);
        if (isRegistered) {
            writer.println("<Response><Status>SUCCESS</Status><Message>Sign-up Successful</Message></Response>");
        } else {
            writer.println("<Response><Status>FAILURE</Status><Message>Sign-up Failed</Message></Response>");
        }
    }

    /**
     * Handles adding a new terminal.
     */
    /**
     * Handles adding a new terminal.
     */
    private void handleAddTerminal(String clientMessage, PrintWriter writer) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(clientMessage.getBytes()));

            Element root = doc.getDocumentElement();
            String terminalId = root.getElementsByTagName("TerminalID").item(0).getTextContent();
            String room = root.getElementsByTagName("Room").item(0).getTextContent();
            String osType = root.getElementsByTagName("OSType").item(0).getTextContent();
            String status = root.getElementsByTagName("Status").item(0).getTextContent();

            // Extract day and time safely
            String day = root.getElementsByTagName("Day").getLength() > 0 ?
                    root.getElementsByTagName("Day").item(0).getTextContent() : "N/A";
            String time = root.getElementsByTagName("Time").getLength() > 0 ?
                    root.getElementsByTagName("Time").item(0).getTextContent() : "N/A";

            boolean success = new AddNewTerminalProcessor().processTerminalData(terminalId, room, osType, day, time, status);
            writer.println(createXMLResponse(success, success ? "Terminal added successfully." : "Failed to add terminal."));
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("<Response><Status>ERROR</Status><Message>Invalid XML Format</Message></Response>");
        }
    }

    /**
     * Handles viewing student reservations.
     */
    private void handleViewStudentReservations(PrintWriter writer) {
        try {
            List<StudentReservation> reservations = ViewStudentReservationsProcessor.parseXML(
                    "src/main/java/server/util/reservations.xml"
            );
            writer.println(createReservationsXMLResponse(reservations));
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("<Response><Status>ERROR</Status><Message>Unable to fetch reservations.</Message></Response>");
        }
    }

    /**
     * Handles fetching user reservations.
     */
    private void handleFetchReservations(PrintWriter writer, String startDate, String endDate) {
        try {
            // Parse the XML reservations file
            List<Reservation> reservations = ViewReservationProcessor.parseXML("server/util/reservationapproval.xml");

            // Convert start and end date to Date objects
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            // Filter reservations based on the date range
            List<Reservation> filteredReservations = new ArrayList<>();
            for (Reservation res : reservations) {
                Date reservationDate = dateFormat.parse(res.getReservationDate());
                if ((reservationDate.after(start) || reservationDate.equals(start)) &&
                        (reservationDate.before(end) || reservationDate.equals(end))) {
                    filteredReservations.add(res);
                }
            }

            // Send the filtered reservations back to the client
            writer.println(createReservations2XMLResponse(filteredReservations));

        } catch (Exception e) {
            e.printStackTrace();
            writer.println("<Response><Status>ERROR</Status><Message>Unable to fetch reservations.</Message></Response>");
        }
    }



    private void handlefetchreservation(PrintWriter writer) {
        try {
            List<Reservation> reservations = ModifyReservationProcessor.parseXML(
                    "server/util/reservationapproval.xml"
            );
            writer.println(createReservations2XMLResponse(reservations));
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("<Response><Status>ERROR</Status><Message>Unable to fetch reservations.</Message></Response>");
        }
    }

    /**
     * Handles updating a reservation.
     */


    private void handleUpdateReservation (PrintWriter writer){
        try {
            List<Reservation> reservations = ModifyReservationProcessor.parseXML(
                    "server/util/reservationapproval.xml"
            );
            writer.println(createReservations2XMLResponse(reservations));
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("<Response><Status>ERROR</Status><Message>Unable to fetch reservations.</Message></Response>");
        }
    }


  /*


  wait lang/







  private void handleUpdateReservation(String clientMessage, PrintWriter writer) {
        Map<String, String> updates = parseUpdateRequest(clientMessage);
        String reservationId = updates != null ? updates.get("ReservationID") : null;

        // Remove ReservationID from updates since it's not modifiable
        updates.remove("ReservationID");

        ModifyReservationProcessor processor = new ModifyReservationProcessor(currentUser);
        String response = processor.updateReservation(reservationId, updates);
        writer.println(response);
    }
*/
    /**
     * Extracts a field from the XML message.
     */
    private String extractField(String message, String startTag, String endTag) {
        try {
            if (message.contains(startTag) && message.contains(endTag)) {
                String[] splitStart = message.split(startTag, 2);
                if (splitStart.length < 2) return null;
                String[] splitEnd = splitStart[1].split(endTag, 2);
                if (splitEnd.length < 2) return null;
                return splitEnd[0].trim();
            }
        } catch (Exception e) {
            System.out.println("Error extracting field: " + startTag + " - " + e.getMessage());
        }
        return null;
    }

    /**
     * Parses an update request XML into a map of updates.
     */
    private Map<String, String> parseUpdateRequest(String xmlRequest) {
        Map<String, String> updates = new HashMap<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlRequest.getBytes()));

            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String tagName = node.getNodeName();
                    String value = node.getTextContent();
                    updates.put(tagName, value);
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing update request: " + e.getMessage());
        }
        return updates;
    }

    /**
     * Creates an XML response.
     */
    private String createXMLResponse(boolean success, String message) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Response");
            doc.appendChild(root);

            Element status = doc.createElement("Status");
            status.appendChild(doc.createTextNode(success ? "SUCCESS" : "FAILURE"));
            root.appendChild(status);

            Element msg = doc.createElement("Message");
            msg.appendChild(doc.createTextNode(message));
            root.appendChild(msg);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Internal Server Error</Message></Response>";
        }
    }

    /**
     * Creates an XML response for student reservations.
     */
    private String createReservationsXMLResponse(List<StudentReservation> reservations) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Reservations");
            doc.appendChild(root);

            for (StudentReservation res : reservations) {
                Element reservation = doc.createElement("Reservation");

                Element resId = doc.createElement("reservation_id");
                resId.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(resId);

                Element terminalId = doc.createElement("terminal_id");
                terminalId.appendChild(doc.createTextNode(res.getTerminalId()));
                reservation.appendChild(terminalId);

                Element room = doc.createElement("terminal_room");
                room.appendChild(doc.createTextNode(res.getTerminalRoom()));
                reservation.appendChild(room);

                Element date = doc.createElement("date");
                date.appendChild(doc.createTextNode(res.getDate()));
                reservation.appendChild(date);

                Element status = doc.createElement("terminal_status");
                status.appendChild(doc.createTextNode(res.getTerminalStatus()));
                reservation.appendChild(status);

                root.appendChild(reservation);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Internal Server Error</Message></Response>";
        }
    }


    private String createReservations2XMLResponse(List<Reservation> reservations) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Reservations");
            doc.appendChild(root);

            for (Reservation res : reservations) {
                Element reservation = doc.createElement("Reservation");

                Element reservationId = doc.createElement("reservation_id");
                reservationId.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(reservationId);

                Element userId = doc.createElement("user_id");
                userId.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(userId);

                Element terminalId = doc.createElement("terminal_id");
                terminalId.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(terminalId);

                Element reservationDate = doc.createElement("reservation_date");
                reservationDate.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(reservationDate);

                Element startTime = doc.createElement("start_time");
                startTime.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(startTime);

                Element endTime = doc.createElement("end_time");
                endTime.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(endTime);

                Element status = doc.createElement("status");
                status.appendChild(doc.createTextNode(res.getReservationId()));
                reservation.appendChild(status);

                root.appendChild(reservation);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Internal Server Error</Message></Response>";
        }
    }
}
