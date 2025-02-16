// File: server/landingpage/LoginProcessor.java
package server.landingpage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import server.utility.LogsXMLHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class LoginProcessor {
    public static String getUserName(String userID, String password, String userType) {
        String xmlFilePath = userType.equalsIgnoreCase("Admin")
                ? "src/main/java/server/util/admin.xml"
                : "src/main/java/server/util/student.xml";

        try {
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            String tag = userType.equalsIgnoreCase("Admin") ? "Admin" : "Student";
            NodeList userList = document.getElementsByTagName(tag);

            for (int i = 0; i < userList.getLength(); i++) {
                Element user = (Element) userList.item(i);
                String xmlID = user.getElementsByTagName(userType + "_ID").item(0).getTextContent();
                String xmlPassword = user.getElementsByTagName("Password").item(0).getTextContent();

                if (xmlID.equals(userID) && xmlPassword.equals(password)) {
                    String userName = user.getElementsByTagName("Name").item(0).getTextContent();
                    // Add login log
                    LogsXMLHandler.saveLog(userID, "Login", userType);
                    return userName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean validateUser(String userID, String password, String userType) {
        String baseDir = System.getProperty("user.dir");
        String xmlFilePath = userType.equalsIgnoreCase("Admin")
                ? baseDir + "/src/main/java/server/util/admin.xml"
                : baseDir + "/src/main/java/server/util/student.xml";

        try {
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            String tag = userType.equalsIgnoreCase("Admin") ? "Admin" : "Student";
            NodeList userList = document.getElementsByTagName(tag);

            for (int i = 0; i < userList.getLength(); i++) {
                Element user = (Element) userList.item(i);
                String xmlID = user.getElementsByTagName(userType + "_ID").item(0).getTextContent();
                String xmlPassword = user.getElementsByTagName("Password").item(0).getTextContent();

                if (xmlID.equals(userID) && xmlPassword.equals(password)) {
                    // Add login log (for validation route)
                    LogsXMLHandler.saveLog(userID, "Login", userType);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
