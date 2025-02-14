package server;

import server.landingpage.LoginProcessor;
import server.landingpage.SignUpProcessor;
import server.admin.AddNewTerminalProcessor;
import server.admin.ViewStudentReservationsProcessor;

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
import java.util.List;
import server.utility.StudentReservation;

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
                    } else if (clientMessage.contains("<AddTerminal>") && isLoggedIn) {
                        // Handle Add Terminal request
                        String responseXML = processAddTerminalRequest(clientMessage);
                        writer.println(responseXML);
                    } else if (clientMessage.contains("<Request><Type>ViewStudentReservations</Type></Request>") && isLoggedIn) {
                        // Handle View Student Reservations request
                        String responseXML = processViewStudentReservationsRequest();
                        writer.println(responseXML);
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

    // Process Add Terminal Request
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

    // Process View Student Reservations Request
    private String processViewStudentReservationsRequest() {
        try {
            List<StudentReservation> reservations = ViewStudentReservationsProcessor.parseXML("src/main/java/server/util/reservations.xml");
            return createReservationsXMLResponse(reservations);
        } catch (Exception e) {
            e.printStackTrace();
            return "<Response><Status>ERROR</Status><Message>Unable to fetch reservations.</Message></Response>";
        }
    }

    // Create XML Response for Student Reservations
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
}
