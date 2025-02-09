package server.landingpage;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SignUpProcessor {

    public static boolean registerUser(String userID, String name, String password, String userType) {
        String xmlFilePath = userType.equalsIgnoreCase("Admin") ? "server/util/admin.xml" : "server/util/student.xml";

        try {
            // Parse the XML file
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            // Find the root element
            String rootTag = userType.equalsIgnoreCase("Admin") ? "Admins" : "Students";
            String userTag = userType.equalsIgnoreCase("Admin") ? "Admin" : "Student";
            Element root = (Element) document.getElementsByTagName(rootTag).item(0);

            // Create a new user element
            Element newUser = document.createElement(userTag);

            Element idElement = document.createElement(userType + "_ID");
            idElement.setTextContent(userID);
            newUser.appendChild(idElement);

            Element nameElement = document.createElement("Name");
            nameElement.setTextContent(name);
            newUser.appendChild(nameElement);

            Element passwordElement = document.createElement("Password");
            passwordElement.setTextContent(password);
            newUser.appendChild(passwordElement);

            if (userType.equalsIgnoreCase("Student")) {
                Element courseElement = document.createElement("Course");
                courseElement.setTextContent("Undeclared"); // Default value
                newUser.appendChild(courseElement);

                Element yearElement = document.createElement("Year");
                yearElement.setTextContent("1"); // Default value
                newUser.appendChild(yearElement);
            } else {
                Element typeElement = document.createElement("Type");
                typeElement.setTextContent("Regular Admin"); // Default value
                newUser.appendChild(typeElement);
            }

            // Append the new user to the root
            root.appendChild(newUser);

            // Write changes back to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
