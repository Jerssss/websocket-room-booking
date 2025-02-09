package client.utility;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

public class XMLParser {

    // Converts data into XML String for sending to the server
    public static String createLoginRequest(String userID, String password, String userType) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element loginElement = doc.createElement("Login");
            doc.appendChild(loginElement);

            Element userIDElement = doc.createElement("UserID");
            userIDElement.setTextContent(userID);
            loginElement.appendChild(userIDElement);

            Element passwordElement = doc.createElement("Password");
            passwordElement.setTextContent(password);
            loginElement.appendChild(passwordElement);

            Element userTypeElement = doc.createElement("UserType");
            userTypeElement.setTextContent(userType);
            loginElement.appendChild(userTypeElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));

            return writer.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Parses XML responses from the server
    public static String parseResponse(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));

            Element root = doc.getDocumentElement();
            return root.getTextContent(); // Return the root element's text content
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
