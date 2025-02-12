package server.admin;

import client.admin.model.AddNewTerminalModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

public class AddNewTerminalProcessor {

    private static final String FILE_PATH = "src/main/java/server/util/terminal.xml";

    public boolean processTerminalData(AddNewTerminalModel terminalModel) {
        try {
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            Element newTerminal = doc.createElement("Terminal");

            Element id = doc.createElement("terminal_id");
            id.appendChild(doc.createTextNode("PC" + terminalModel.getTerminalId().trim()));
            newTerminal.appendChild(id);

            Element room = doc.createElement("terminal_room");
            room.appendChild(doc.createTextNode("D" + terminalModel.getRoom().trim()));
            newTerminal.appendChild(room);

            Element os = doc.createElement("terminal_os");
            os.appendChild(doc.createTextNode(terminalModel.getOsType().trim()));
            newTerminal.appendChild(os);

            // Check if room needs a default schedule
            if (terminalModel.getRoom().equals("526") || terminalModel.getRoom().equals("524") || terminalModel.getRoom().equals("426")) {
                Element schedule = doc.createElement("default_schedule");

                String[][] scheduleData = {
                        {"Monday", "09:30-17:30"},
                        {"Tuesday", "09:30-17:30"},
                        {"Wednesday", "11:30-16:30"},
                        {"Thursday", "11:30-16:30"},
                        {"Friday", "07:30-15:30"},
                        {"Saturday", "07:30-15:30"}
                };

                for (String[] day : scheduleData) {
                    Element dayElement = doc.createElement(day[0]);
                    dayElement.appendChild(doc.createTextNode(day[1]));
                    schedule.appendChild(dayElement);
                }
                newTerminal.appendChild(schedule);
            }

            Element status = doc.createElement("terminal_status");
            status.appendChild(doc.createTextNode(terminalModel.getStatus().trim()));
            newTerminal.appendChild(status);

            root.appendChild(newTerminal);

            removeWhiteSpaces(doc);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(FILE_PATH));
            transformer.transform(source, result);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void removeWhiteSpaces(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhiteSpaces(child);
            }
        }
    }
}