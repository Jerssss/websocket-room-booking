// File: server/landingpage/SignUpProcessor.java
package server.landingpage;

import server.utility.LogsXMLHandler;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class SignUpProcessor {
    public static boolean registerUser(String userID, String name, String password, String userType, String courseYear, String facultyType) {
        String baseDir = System.getProperty("user.dir");
        String xmlFilePath = userType.equalsIgnoreCase("Admin")
                ? baseDir + "/src/main/java/server/util/admin.xml"
                : baseDir + "/src/main/java/server/util/student.xml";

        try {
            File file = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;

            // Parse or create document
            if (file.exists() && file.length() > 0) {
                document = builder.parse(file);
                document.getDocumentElement().normalize();
                removeWhitespaceNodes(document);

                // Check for duplicate ID
                if (isDuplicateID(document, userType + "_ID", userID)) {
                    System.out.println("Duplicate ID found: " + userID);
                    return false;
                }
            } else {
                document = builder.newDocument();
                Element rootElement = document.createElement(userType.equalsIgnoreCase("Admin") ? "Admins" : "Students");
                document.appendChild(rootElement);
            }

            Element rootElement = document.getDocumentElement();
            Element newUser = document.createElement(userType);

            // Add ID
            newUser.appendChild(createElement(document, userType + "_ID", userID));

            // Add Name
            if (name != null && !name.isEmpty()) {
                newUser.appendChild(createElement(document, "Name", name));
            }

            // Add Password
            newUser.appendChild(createElement(document, "Password", password));

            // Add additional fields
            if ("Student".equalsIgnoreCase(userType) && courseYear != null) {
                newUser.appendChild(createElement(document, "CourseYear", courseYear));
            } else if ("Admin".equalsIgnoreCase(userType) && facultyType != null) {
                newUser.appendChild(createElement(document, "FacultyType", facultyType));
            }

            rootElement.appendChild(newUser);
            saveDocument(document, file);

            // Log the signup (fixed argument order)
            LogsXMLHandler.saveLog(userID, "SignUp", userType);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Element createElement(Document doc, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textContent.trim()));
        return element;
    }

    public static boolean isDuplicateID(Document document, String idTagName, String userID) {
        NodeList idNodes = document.getElementsByTagName(idTagName);
        for (int i = 0; i < idNodes.getLength(); i++) {
            Node idNode = idNodes.item(i);
            if (idNode.getTextContent().trim().equals(userID.trim())) {
                return true; // Duplicate found
            }
        }
        return false; // No duplicate
    }

    private static void saveDocument(Document document, File file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Configure transformer for clean formatting
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        // Remove the XML declaration
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        // Write the document to file
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    private static void removeWhitespaceNodes(Document document) {
        NodeList nodeList = document.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            removeWhitespaceNodesRecursive(nodeList.item(i));
        }
    }

    private static void removeWhitespaceNodesRecursive(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhitespaceNodesRecursive(child);
            }
        }
    }
}
