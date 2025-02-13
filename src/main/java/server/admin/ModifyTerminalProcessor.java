package server.admin;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ModifyTerminalProcessor {
    private static final String XML_FILE = "src/main/java/server/util/terminal.xml";

    public String getTerminalData() {
        StringBuilder data = new StringBuilder();
        try {
            File file = new File(XML_FILE);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList terminalList = doc.getElementsByTagName("Terminal");

            for (int i = 0; i < terminalList.getLength(); i++) {
                Element terminal = (Element) terminalList.item(i);
                String id = terminal.getElementsByTagName("terminal_id").item(0).getTextContent();
                String room = terminal.getElementsByTagName("terminal_room").item(0).getTextContent();
                String os = terminal.getElementsByTagName("terminal_os").item(0).getTextContent();
                String status = terminal.getElementsByTagName("terminal_status").item(0).getTextContent();

                data.append(id).append(",").append(room).append(",").append(os).append(",").append(status).append(";");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public void processRequest(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = reader.readLine();
            if ("GET_TERMINALS".equals(request)) {
                writer.println(getTerminalData());
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
