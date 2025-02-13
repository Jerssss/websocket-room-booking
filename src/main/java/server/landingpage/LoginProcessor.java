package server.landingpage;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import server.utility.LogsXMLHandler;

public class LoginProcessor {

    public static boolean validateUser(String userID, String password, String userType) {
        // Resolve the correct path to student.xml or admin.xml
        String baseDir = System.getProperty("user.dir"); // Gets the root of the project
        String xmlFilePath = userType.equalsIgnoreCase("Admin")
                ? baseDir + "/src/main/java/server/util/admin.xml"
                : baseDir + "/src/main/java/server/util/student.xml";

        try {
            System.out.println("Resolved XML File Path: " + xmlFilePath); // Debug log

            // Parse the XML file
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // Validate credentials
            String tag = userType.equalsIgnoreCase("Admin") ? "Admin" : "Student";
            NodeList userList = document.getElementsByTagName(tag);

            for (int i = 0; i < userList.getLength(); i++) {
                Element user = (Element) userList.item(i);
                String xmlID = user.getElementsByTagName(userType + "_ID").item(0).getTextContent();
                String xmlPassword = user.getElementsByTagName("Password").item(0).getTextContent();

                if (xmlID.equals(userID) && xmlPassword.equals(password)) {
                    LogsXMLHandler.saveLog(userID, userType, "Login");
                    return true; // Login successful
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Login failed
    }
}
