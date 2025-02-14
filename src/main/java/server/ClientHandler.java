package server;

import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;
import server.admin.AddNewTerminalProcessor;
import server.student.ViewReservationProcessor; // Import the ViewReservationProcessor class

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

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
                    writer.println("<Response><Status>SUCCESS</Status><Message>Goodbye!</Message></Response>");
                    break;
                }

                try {
                    if (clientMessage.contains("<Login>")) {
                        // Handle login request
                        String userID = extractField(clientMessage, "<UserID>", "</UserID>");
                        String password = extractField(clientMessage, "<Password>", "</Password>");
                        String userType = extractField(clientMessage, "<UserType>", "</UserType>");

                        if (userID == null || password == null || userType == null) {
                            writer.println("<Response><Status>ERROR</Status><Message>Missing fields for login.</Message></Response>");
                            continue;
                        }

                        boolean isValid = LoginProcessor.validateUser(userID, password, userType);
                        if (isValid) {
                            writer.println("<Response><Status>SUCCESS</Status><Message>Login Successful</Message></Response>");
                            isLoggedIn = true;
                        } else {
                            writer.println("<Response><Status>FAILURE</Status><Message>Invalid Credentials</Message></Response>");
                        }
                    } else if (clientMessage.contains("<SignUp>")) {
                        // Handle sign-up request
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
                    } else if (clientMessage.equals("FETCH_RESERVATIONS") && isLoggedIn) {
                        // Handle the fetch reservations request
                        String responseXML = processViewReservationsRequest();
                        writer.println(responseXML); // Send reservations in XML format
                    } else if (clientMessage.contains("<AddTerminal>") && isLoggedIn) {
                        // Handle Add Terminal request
                        String responseXML = processAddTerminalRequest(clientMessage);
                        writer.println(responseXML); // Send XML response
                    } else if (!isLoggedIn) {
                        writer.println("<Response><Status>ERROR</Status><Message>Please log in first.</Message></Response>");
                    } else {
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

    private String processViewReservationsRequest() {
        try {
            // Log the start of processing the request
            System.out.println("Processing view reservations request...");

            // Fetch reservations from the processor
            ViewReservationProcessor viewReservationProcessor = new ViewReservationProcessor();
            Map<String, Map<String, String>> reservations = viewReservationProcessor.fetchAllReservations();

            // Debug: Log the raw fetched reservations
            System.out.println("Fetched Reservations: " + reservations);

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
                return "<Response><Status>FAILURE</Status><Message>No Reservations Found</Message></Response>";
            }

            // Creating an XML response for reservations
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Reservations");
            doc.appendChild(root);

            // Iterate through the reservations and create XML elements
            for (Map.Entry<String, Map<String, String>> entry : reservations.entrySet()) {
                System.out.println("Processing reservation: " + entry.getKey());

                Element reservation = doc.createElement("Reservation");
                root.appendChild(reservation);

                // Create XML elements for each field
                Element reservationId = doc.createElement("ReservationID");
                reservationId.appendChild(doc.createTextNode(entry.getValue().get("reservation_id")));
                reservation.appendChild(reservationId);

                Element studentId = doc.createElement("StudentID");
                studentId.appendChild(doc.createTextNode(entry.getValue().get("Student_ID")));
                reservation.appendChild(studentId);

                Element terminalId = doc.createElement("TerminalID");
                terminalId.appendChild(doc.createTextNode(entry.getValue().get("terminal_id")));
                reservation.appendChild(terminalId);

                Element terminalRoom = doc.createElement("TerminalRoom");
                terminalRoom.appendChild(doc.createTextNode(entry.getValue().get("terminal_room")));
                reservation.appendChild(terminalRoom);

                Element terminalStatus = doc.createElement("TerminalStatus");
                terminalStatus.appendChild(doc.createTextNode(entry.getValue().get("terminal_status")));
                reservation.appendChild(terminalStatus);
            }

            // Transform the XML document to a string
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            // Log the generated XML response
            System.out.println("Generated XML Response: " + writer.toString());

            return writer.toString();

        } catch (Exception e) {
            // Catch any errors and log them
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Error fetching reservations</Message></Response>";
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
}
