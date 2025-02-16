package server.admin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import server.utility.LogReport;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportGeneratorProcessor {
    private static final String filePath = "src/main/java/server/util/logs.xml";

    public static List<LogReport> parseLogXML() {
        List<LogReport> logs = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            NodeList logNodes = document.getElementsByTagName("Log");
            for (int i = 0; i < logNodes.getLength(); i++) {
                Node logNode = logNodes.item(i);
                if (logNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element logElement = (Element) logNode;
                    String userID = logElement.getElementsByTagName("UserID").item(0).getTextContent();
                    String userType = logElement.getElementsByTagName("UserType").item(0).getTextContent();
                    String action = logElement.getElementsByTagName("Action").item(0).getTextContent();
                    String date = logElement.getElementsByTagName("Date").item(0).getTextContent();
                    String time = logElement.getElementsByTagName("Time").item(0).getTextContent();

                    logs.add(new LogReport(userID, userType, action, date, time));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }

}
