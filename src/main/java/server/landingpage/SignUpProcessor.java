package server.landingpage;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SignUpProcessor {

    public static boolean registerUser(String userID, String password, String userType, String courseYear, String facultyType) {
        String baseDir = System.getProperty("user.dir");
        String xmlFilePath = userType.equalsIgnoreCase("Admin")
                ? baseDir + "/src/main/java/server/util/admin.xml"
                : baseDir + "/src/main/java/server/util/student.xml";

        try {
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            Element rootElement = document.getDocumentElement();
            Element newUser = document.createElement(userType);

            Element idElement = document.createElement(userType + "_ID");
            idElement.appendChild(document.createTextNode(userID));
            newUser.appendChild(idElement);

            Element passwordElement = document.createElement("Password");
            passwordElement.appendChild(document.createTextNode(password));
            newUser.appendChild(passwordElement);

            if (userType.equalsIgnoreCase("Student")) {
                Element courseYearElement = document.createElement("CourseYear");
                courseYearElement.appendChild(document.createTextNode(courseYear));
                newUser.appendChild(courseYearElement);
            } else if (userType.equalsIgnoreCase("Admin")) {
                Element facultyTypeElement = document.createElement("FacultyType");
                facultyTypeElement.appendChild(document.createTextNode(facultyType));
                newUser.appendChild(facultyTypeElement);
            }

            rootElement.appendChild(newUser);

            // Save the updated document back to the XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
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