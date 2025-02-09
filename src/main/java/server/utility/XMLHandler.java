package server.utility;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLHandler {

    // Reads a value from an XML file by tag name
    public static String readValue(String filePath, String tagName, String matchingTag, String matchingValue) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodeList = doc.getElementsByTagName(tagName);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String value = element.getElementsByTagName(matchingTag).item(0).getTextContent();
                if (value.equals(matchingValue)) {
                    return element.getTextContent(); // Return the matching value
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Adds a new node to an XML file
    public static void addNode(String filePath, String parentTag, String newNodeTag, String[][] data) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            Node parentNode = doc.getElementsByTagName(parentTag).item(0);

            Element newElement = doc.createElement(newNodeTag);
            for (String[] keyValue : data) {
                Element childElement = doc.createElement(keyValue[0]);
                childElement.setTextContent(keyValue[1]);
                newElement.appendChild(childElement);
            }

            parentNode.appendChild(newElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Deletes a node from an XML file
    public static void deleteNode(String filePath, String parentTag, String tagName, String matchingValue) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList nodeList = doc.getElementsByTagName(parentTag);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element parentElement = (Element) nodeList.item(i);
                NodeList children = parentElement.getElementsByTagName(tagName);
                for (int j = 0; j < children.getLength(); j++) {
                    Element child = (Element) children.item(j);
                    if (child.getTextContent().equals(matchingValue)) {
                        parentElement.removeChild(child);
                        break;
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
