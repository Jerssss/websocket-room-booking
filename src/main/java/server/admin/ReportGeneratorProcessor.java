package server.admin;

import server.utility.LogReport;
import server.utility.ReservationReport;

import java.util.List;
import java.util.stream.Collectors;

public class ReportGeneratorProcessor {

    // For LogReport filtering and sorting
    public static List<LogReport> applySortingAndFiltering(List<LogReport> logs, String sortOption, String dateFilter) {
        if (logs == null) return null;

        // Apply filtering by date if dateFilter is not null or empty
        if (dateFilter != null && !dateFilter.isEmpty()) {
            logs = logs.stream()
                    .filter(log -> log.getDate().contains(dateFilter))
                    .collect(Collectors.toList());
        }

        // Apply sorting based on the selected sortOption
        switch (sortOption) {
            case "Sort by Students":
                return logs.stream()
                        .sorted((log1, log2) -> log1.getUserID().compareTo(log2.getUserID()))
                        .collect(Collectors.toList());

            case "Sort by Admin":
                return logs.stream()
                        .sorted((log1, log2) -> log1.getUserType().compareTo(log2.getUserType()))
                        .collect(Collectors.toList());

            case "Filter by Date":
                return logs.stream()
                        .sorted((log1, log2) -> log1.getDate().compareTo(log2.getDate()))
                        .collect(Collectors.toList());

            default:
                return logs;
        }
    }

    // For ReservationReport filtering and sorting
    public static List<ReservationReport> applySortingAndFilteringForReservations(List<ReservationReport> reservations, String sortOption, String dateFilter) {
        if (reservations == null) return null;

        // Apply filtering by date if dateFilter is not null or empty
        if (dateFilter != null && !dateFilter.isEmpty()) {
            reservations = reservations.stream()
                    .filter(reservation -> reservation.getDate().contains(dateFilter))
                    .collect(Collectors.toList());
        }

        // Apply sorting based on the selected sortOption
        switch (sortOption) {
            case "Sort by Students":
                return reservations.stream()
                        .sorted((reservation1, reservation2) -> reservation1.getReservationId().compareTo(reservation2.getReservationId()))
                        .collect(Collectors.toList());

            case "Sort by Admin":
                return reservations.stream()
                        .sorted((reservation1, reservation2) -> reservation1.getTerminalId().compareTo(reservation2.getTerminalId()))
                        .collect(Collectors.toList());

            case "Filter by Date":
                return reservations.stream()
                        .sorted((reservation1, reservation2) -> reservation1.getDate().compareTo(reservation2.getDate()))
                        .collect(Collectors.toList());

            default:
                return reservations;
        }
    }

    // Method to parse LogReport XML
    public static List<LogReport> parseLogXML() {
        // Implement your XML parsing logic here
        return null; // Return a list of LogReport objects
    }

    // Method to parse ReservationReport XML
    public static List<ReservationReport> parseReservationXML() {
        // Implement your XML parsing logic here
        return null; // Return a list of ReservationReport objects
    }
}
