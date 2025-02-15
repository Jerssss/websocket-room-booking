package server;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;
import server.admin.AddNewTerminalProcessor;
import server.admin.ViewStudentReservationsProcessor;
import server.student.ModifyReservationProcessor;
import server.utility.StudentReservation;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final ConcurrentMap<String, Boolean> activeUsers = new ConcurrentHashMap<>();
    private final Socket clientSocket;
    private String currentUser = null;
    private String loggedInUserId = null; // Track logged-in user in this session.

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            writer.println("Welcome to the Server! Type 'exit' to logout.");
            String clientMessage;
            boolean isLoggedIn = false;

            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from Client: " + clientMessage);

                if ("exit".equalsIgnoreCase(clientMessage)) {
                    if (currentUser != null) {
                        activeUsers.remove(currentUser);  // Remove user on logout
                        System.out.println(currentUser + " has logged out.");
                    }
                    writer.println("<Response><Status>SUCCESS</Status><Message>Goodbye!</Message></Response>");
                    break;
                }

                try {
                    if (clientMessage.contains("<Login>")) {
                        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                        String password = extractField(clientMessage, "<Password>", "</Password>");
                        String userType = extractField(clientMessage, "<UserType>", "</UserType>");

                        if (userID == null || password == null || userType == null) {
                            writer.println("<Response><Status>ERROR</Status><Message>Missing fields for login.</Message></Response>");
                            continue;
                        }

                        if (activeUsers.containsKey(userID)) {
                            writer.println("<Response><Status>FAILURE</Status><Message>User is already logged in from another terminal.</Message></Response>");
                        } else {
                            boolean isValid = LoginProcessor.validateUser(userID, password, userType);
                            if (isValid) {
                                activeUsers.put(userID, true);  // Add user to active users
                                currentUser = userID;
                                writer.println("<Response><Status>SUCCESS</Status><Message>Login Successful</Message></Response>");
                                isLoggedIn = true;
                            } else {
                                writer.println("<Response><Status>FAILURE</Status><Message>Invalid Credentials</Message></Response>");
                            }
                        }

                    } else if (clientMessage.contains("<SignUp>")) {
                        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                        String name = extractField(clientMessage, "<Name>", "</Name>");
                        String password = extractField(clientMessage, "<Password>", "</Password>");
                        String userType = extractField(clientMessage, "<UserType>", "</UserType>");
                        String courseYear = extractField(clientMessage, "<CourseYear>", "</CourseYear>");
                        String facultyType = extractField(clientMessage, "<FacultyType>", "</FacultyType>");

                        if (userID == null || name == null || password == null || userType == null) {
                            writer.println("<Response><Status>ERROR</Status><Message>Missing fields for sign-up.</Message></Response>");
                            continue;
                        }

                        boolean isRegistered = SignUpProcessor.registerUser(userID, name, password, userType, courseYear, facultyType);
                        if (isRegistered) {
                            writer.println("<Response><Status>SUCCESS</Status><Message>Sign-up Successful</Message></Response>");
                        } else {
                            writer.println("<Response><Status>FAILURE</Status><Message>Sign-up Failed</Message></Response>");
                        }
                    } else if (clientMessage.contains("<AddTerminal>") && isLoggedIn) {
                        String responseXML = processAddTerminalRequest(clientMessage);
                        writer.println(responseXML);
                    } else if (clientMessage.contains("<Request><Type>ViewStudentReservations</Type></Request>") && isLoggedIn) {
                        String responseXML = processViewStudentReservationsRequest();
                        writer.println(responseXML);
                    } else if (!isLoggedIn) {
                        writer.println("<Response><Status>ERROR</Status><Message>Please log in first.</Message></Response>");
                    } else if (clientMessage.startsWith("FETCH_RESERVATIONS")) {
                        ModifyReservationProcessor processor = new ModifyReservationProcessor(loggedInUserId);
                        String response = processor.fetchReservations();
                        writer.println(response);
                    }
                    else if (clientMessage.startsWith("UPDATE_RESERVATION")) {
                        // Extract reservation ID and updates from XML
                        Map<String, String> updates = parseUpdateRequest(clientMessage);
                        String reservationId = extractField(clientMessage, "<ReservationID>", "</ReservationID>");
                        ModifyReservationProcessor processor = new ModifyReservationProcessor(loggedInUserId);
                        String response = processor.updateReservation(reservationId, updates);
                        writer.println(response);
                    }
                    else if (clientMessage.startsWith("<UpdateRequest>")) {
                        Map<String, String> updates = parseUpdateRequest(clientMessage);
                        String reservationId = updates != null ? updates.get("ReservationID") : null;

                        // Remove ReservationID from updates as it's not an updateable field
                        updates.remove("ReservationID");

                        ModifyReservationProcessor processor = new ModifyReservationProcessor(loggedInUserId);
                        String response = processor.updateReservation(reservationId, updates);
                        writer.println(response);
                    }
                    else {
                        writer.println("<Response><Status>ERROR</Status><Message>Invalid Request.</Message></Response>");
                    }
                } catch (Exception e) {
                    writer.println("<Response><Status>ERROR</Status><Message>Malformed Request</Message></Response>");
                    System.out.println("Error processing client message: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            if (currentUser != null) {
                activeUsers.remove(currentUser);  // Ensure user is removed even on unexpected disconnect
                System.out.println(currentUser + " has logged out.");
            }
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processAddTerminalRequest(String xmlRequest) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlRequest.getBytes()));

            Element root = doc.getDocumentElement();
            String terminalId = root.getElementsByTagName("TerminalID").item(0).getTextContent();
            String room = root.getElementsByTagName("Room").item(0).getTextContent();
            String osType = root.getElementsByTagName("OSType").item(0).getTextContent();
            String status = root.getElementsByTagName("Status").item(0).getTextContent();

            boolean success = new AddNewTerminalProcessor().processTerminalData(terminalId, room, osType, status);
            return createXMLResponse(success, success ? "Terminal added successfully." : "Failed to add terminal.");
        } catch (Exception e) {
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Invalid XML Format</Message></Response>";
        }
    }

    private String processViewStudentReservationsRequest() {
        try {
            List<StudentReservation> reservations = ViewStudentReservationsProcessor.parseXML("src/main/java/server/util/reservations.xml");
            return createReservationsXMLResponse(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Unable to fetch reservations.</Message></Response>";
        }
    }

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

    private String extractField(String message, String startTag, String endTag) {
        try {
            if (message.contains(startTag) && message.contains(endTag)) {
                return message.split(startTag)[1].split(endTag)[0].trim();
            }
        } catch (Exception e) {
            System.out.println("Error extracting field: " + startTag);
        }
        return null;
    }

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
            return null;
        }
        return updates;
    }
}
