// File: server/admin/ReservationApprovalProcessor.java
package server.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.List;

public class ReservationApprovalProcessor {
    private static final String FILE_PATH = "src/main/java/server/util/reservation_approval.xml";

    /** Load all approval requests from XML */
    public ObservableList<ApprovalTerminal> loadAllApprovalRequests() {
        ObservableList<ApprovalTerminal> approvalList = FXCollections.observableArrayList();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return approvalList;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("ApprovalRequest");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String terminalId = getTagValue("terminal_id", element);
                    String terminalRoom = getTagValue("terminal_room", element);
                    String terminalStatus = getTagValue("terminal_status", element);
                    String reservationId = getTagValue("reservation_id", element);
                    String userId = getTagValue("user_id", element);
                    String reservationDate = getTagValue("reservation_date", element);

                    ApprovalTerminal approval = new ApprovalTerminal(
                            terminalId, terminalRoom, terminalStatus,
                            reservationId, userId, reservationDate
                    );
                    approvalList.add(approval);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return approvalList;
    }

    /** Approve a reservation */
    public boolean approveReservation(String reservationId) {
        List<ApprovalTerminal> approvals = loadAllApprovalRequests();
        for (ApprovalTerminal approval : approvals) {
            if (approval.getReservationId().equals(reservationId)) {
                approval.setTerminalStatus("Approved");
                saveAllApprovals(approvals);
                return true;
            }
        }
        return false;
    }

    /** Reject a reservation */
    public boolean rejectReservation(String reservationId) {
        List<ApprovalTerminal> approvals = loadAllApprovalRequests();
        for (ApprovalTerminal approval : approvals) {
            if (approval.getReservationId().equals(reservationId)) {
                approval.setTerminalStatus("Rejected");
                saveAllApprovals(approvals);
                return true;
            }
        }
        return false;
    }

    /** Save all approvals to XML */
    private void saveAllApprovals(List<ApprovalTerminal> approvals) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("ApprovalRequests");
            document.appendChild(root);

            for (ApprovalTerminal approval : approvals) {
                Element approvalElement = document.createElement("ApprovalRequest");

                appendElement(document, approvalElement, "terminal_id", approval.getTerminalId());
                appendElement(document, approvalElement, "terminal_room", approval.getTerminalRoom());
                appendElement(document, approvalElement, "terminal_status", approval.getTerminalStatus());
                appendElement(document, approvalElement, "reservation_id", approval.getReservationId());
                appendElement(document, approvalElement, "user_id", approval.getUserId());
                appendElement(document, approvalElement, "reservation_date", approval.getReservationDate());

                root.appendChild(approvalElement);
            }

            server.utility.XMLUtility.saveXMLDocument(FILE_PATH, document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Helper: Get XML tag value */
    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    /** Helper: Append XML element */
    private void appendElement(Document document, Element parent, String tag, String value) {
        Element element = document.createElement(tag);
        element.appendChild(document.createTextNode(value));
        parent.appendChild(element);
    }
}
