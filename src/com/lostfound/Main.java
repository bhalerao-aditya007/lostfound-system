package com.lostfound;

import com.lostfound.dao.*;
import com.lostfound.matching.MatchingEngine;
import com.lostfound.model.*;
import com.lostfound.notification.ConsoleNotificationService;
import com.lostfound.notification.NotificationService;
import com.lostfound.util.CsvExporter;
import com.lostfound.util.SimpleLogger;

import java.util.List;
import java.util.Scanner;

/**
 * Main application class for the Digital Lost & Found System.
 * Provides an interactive console menu for all system operations.
 */
public class Main {
    private static final LostItemDAO lostItemDAO = new LostItemDAO();
    private static final FoundItemDAO foundItemDAO = new FoundItemDAO();
    private static MatchDAO matchDAO = new MatchDAO();
    private static MatchingEngine matchingEngine = new MatchingEngine();
    private static NotificationService notificationService = new ConsoleNotificationService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("         DIGITAL LOST & FOUND SYSTEM");
        System.out.println("=".repeat(70));
        SimpleLogger.log("Application started");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1: reportLostItem(); break;
                case 2: reportFoundItem(); break;
                case 3: viewAllLostItems(); break;
                case 4: viewAllFoundItems(); break;
                case 5: viewAllMatches(); break;
                case 6: searchLostItemById(); break;
                case 7: searchFoundItemById(); break;
                case 8: updateMatchStatus(); break;
                case 9: exportDataMenu(); break;
                case 10: viewStatistics(); break;
                case 0:
                    running = false;
                    System.out.println("\nThank you for using the Lost & Found System!");
                    SimpleLogger.log("Application terminated");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(70));
        System.out.println("1.  Report a Lost Item");
        System.out.println("2.  Report a Found Item");
        System.out.println("3.  View All Lost Items");
        System.out.println("4.  View All Found Items");
        System.out.println("5.  View All Matches");
        System.out.println("6.  Search Lost Item by ID");
        System.out.println("7.  Search Found Item by ID");
        System.out.println("8.  Update Match Status");
        System.out.println("9.  Export Data (CSV)");
        System.out.println("10. View Statistics");
        System.out.println("0.  Exit");
        System.out.println("=".repeat(70));
    }

    private static void reportLostItem() {
        System.out.println("\n--- REPORT LOST ITEM ---");

        String itemName = getStringInput("Item Name (e.g., iPhone 13): ");
        String color = getStringInput("Color: ");
        String location = getStringInput("Location where lost: ");
        String description = getStringInput("Description (optional details): ");
        String contactInfo = getStringInput("Your contact info (email/phone): ");

        LostItem item = new LostItem(itemName, color, location, description, contactInfo);
        int id = lostItemDAO.addLostItem(item);

        if (id > 0) {
            System.out.println("\n✓ Lost item reported successfully! ID: " + id);

            System.out.println("\nSearching for potential matches...");
            List<Match> matches = matchingEngine.findMatchesForLostItem(item);

            if (matches.isEmpty()) {
                System.out.println("No matches found at this time.");
                System.out.println("You will be notified if a matching item is found.");
            } else {
                System.out.println("\n" + matches.size() + " potential match(es) found!");

                for (Match match : matches) {
                    int matchId = matchDAO.addMatch(match);
                    if (matchId > 0) {
                        FoundItem foundItem = foundItemDAO.getFoundItemById(match.getFoundItemId());
                        notificationService.notifyMatch(match, item, foundItem);
                    }
                }
            }
        } else {
            System.out.println("\n✗ Failed to report lost item. Please try again.");
        }
    }

    private static void reportFoundItem() {
        System.out.println("\n--- REPORT FOUND ITEM ---");

        String itemName = getStringInput("Item Name (e.g., Wallet): ");
        String color = getStringInput("Color: ");
        String location = getStringInput("Location where found: ");
        String description = getStringInput("Description (optional details): ");
        String finderInfo = getStringInput("Your contact info (email/phone): ");

        FoundItem item = new FoundItem(itemName, color, location, description, finderInfo);
        int id = foundItemDAO.addFoundItem(item);

        if (id > 0) {
            System.out.println("\n✓ Found item reported successfully! ID: " + id);

            System.out.println("\nSearching for potential matches...");
            List<Match> matches = matchingEngine.findMatchesForFoundItem(item);

            if (matches.isEmpty()) {
                System.out.println("No matches found at this time.");
                System.out.println("The item owner will be notified if they report it as lost.");
            } else {
                System.out.println("\n" + matches.size() + " potential match(es) found!");

                for (Match match : matches) {
                    int matchId = matchDAO.addMatch(match);
                    if (matchId > 0) {
                        LostItem lostItem = lostItemDAO.getLostItemById(match.getLostItemId());
                        notificationService.notifyMatch(match, lostItem, item);
                    }
                }
            }
        } else {
            System.out.println("\n✗ Failed to report found item. Please try again.");
        }
    }

    private static void viewAllLostItems() {
        System.out.println("\n--- ALL LOST ITEMS ---");
        List<LostItem> items = lostItemDAO.getAllLostItems();

        if (items.isEmpty()) {
            System.out.println("No lost items found in the system.");
        } else {
            System.out.println("Total: " + items.size() + " item(s)\n");
            for (LostItem item : items) {
                System.out.println(item);
                System.out.println("-".repeat(70));
            }
        }
    }

    private static void viewAllFoundItems() {
        System.out.println("\n--- ALL FOUND ITEMS ---");
        List<FoundItem> items = foundItemDAO.getAllFoundItems();

        if (items.isEmpty()) {
            System.out.println("No found items in the system.");
        } else {
            System.out.println("Total: " + items.size() + " item(s)\n");
            for (FoundItem item : items) {
                System.out.println(item);
                System.out.println("-".repeat(70));
            }
        }
    }

    private static void viewAllMatches() {
        System.out.println("\n--- ALL MATCHES ---");
        List<Match> matches = matchDAO.getAllMatches();

        if (matches.isEmpty()) {
            System.out.println("No matches found in the system.");
        } else {
            System.out.println("Total: " + matches.size() + " match(es)\n");

            for (Match match : matches) {
                System.out.println(match);

                LostItem lostItem = lostItemDAO.getLostItemById(match.getLostItemId());
                FoundItem foundItem = foundItemDAO.getFoundItemById(match.getFoundItemId());

                System.out.println("\nLost Item: " + lostItem.getItemName() + " (" + lostItem.getColor() + ")");
                System.out.println("Found Item: " + foundItem.getItemName() + " (" + foundItem.getColor() + ")");
                System.out.println("-".repeat(70));
            }
        }
    }

    private static void searchLostItemById() {
        int id = getIntInput("\nEnter Lost Item ID: ");
        LostItem item = lostItemDAO.getLostItemById(id);

        if (item != null) {
            System.out.println("\n" + item);

            List<Match> matches = matchDAO.getMatchesForLostItem(id);
            if (!matches.isEmpty()) {
                System.out.println("\nMatches for this item: " + matches.size());
                for (Match match : matches) {
                    System.out.println("  - Match ID: " + match.getMatchId() +
                            " | Score: " + match.getScore() +
                            " | Status: " + match.getStatus());
                }
            }
        } else {
            System.out.println("\n✗ Lost item not found with ID: " + id);
        }
    }

    private static void searchFoundItemById() {
        int id = getIntInput("\nEnter Found Item ID: ");
        FoundItem item = foundItemDAO.getFoundItemById(id);

        if (item != null) {
            System.out.println("\n" + item);

            List<Match> matches = matchDAO.getMatchesForFoundItem(id);
            if (!matches.isEmpty()) {
                System.out.println("\nMatches for this item: " + matches.size());
                for (Match match : matches) {
                    System.out.println("  - Match ID: " + match.getMatchId() +
                            " | Score: " + match.getScore() +
                            " | Status: " + match.getStatus());
                }
            }
        } else {
            System.out.println("\n✗ Found item not found with ID: " + id);
        }
    }

    private static void updateMatchStatus() {
        int matchId = getIntInput("\nEnter Match ID: ");

        System.out.println("\nSelect new status:");
        System.out.println("1. CONFIRMED");
        System.out.println("2. REJECTED");
        int statusChoice = getIntInput("Choice: ");

        String newStatus = "";
        if (statusChoice == 1) {
            newStatus = "CONFIRMED";
        } else if (statusChoice == 2) {
            newStatus = "REJECTED";
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        boolean success = matchDAO.updateMatchStatus(matchId, newStatus);
        if (success) {
            System.out.println("\n✓ Match status updated to: " + newStatus);
        } else {
            System.out.println("\n✗ Failed to update match status.");
        }
    }

    private static void exportDataMenu() {
        System.out.println("\n--- EXPORT DATA ---");
        System.out.println("1. Export Lost Items");
        System.out.println("2. Export Found Items");
        System.out.println("3. Export Matches");
        System.out.println("4. Export All");

        int choice = getIntInput("Choice: ");

        switch (choice) {
            case 1:
                CsvExporter.exportLostItems(lostItemDAO.getAllLostItems(), "lost_items.csv");
                System.out.println("✓ Exported to lost_items.csv");
                break;
            case 2:
                CsvExporter.exportFoundItems(foundItemDAO.getAllFoundItems(), "found_items.csv");
                System.out.println("✓ Exported to found_items.csv");
                break;
            case 3:
                CsvExporter.exportMatches(matchDAO.getAllMatches(), "matches.csv");
                System.out.println("✓ Exported to matches.csv");
                break;
            case 4:
                CsvExporter.exportLostItems(lostItemDAO.getAllLostItems(), "lost_items.csv");
                CsvExporter.exportFoundItems(foundItemDAO.getAllFoundItems(), "found_items.csv");
                CsvExporter.exportMatches(matchDAO.getAllMatches(), "matches.csv");
                System.out.println("✓ All data exported successfully");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void viewStatistics() {
        System.out.println("\n--- SYSTEM STATISTICS ---");

        int totalLost = lostItemDAO.getAllLostItems().size();
        int totalFound = foundItemDAO.getAllFoundItems().size();
        List<Match> allMatches = matchDAO.getAllMatches();
        int totalMatches = allMatches.size();

        long confirmedMatches = allMatches.stream()
                .filter(m -> "CONFIRMED".equals(m.getStatus()))
                .count();
        long pendingMatches = allMatches.stream()
                .filter(m -> "PENDING".equals(m.getStatus()))
                .count();
        long rejectedMatches = allMatches.stream()
                .filter(m -> "REJECTED".equals(m.getStatus()))
                .count();

        System.out.println("Total Lost Items Reported: " + totalLost);
        System.out.println("Total Found Items Reported: " + totalFound);
        System.out.println("Total Matches Generated: " + totalMatches);
        System.out.println("  - Confirmed: " + confirmedMatches);
        System.out.println("  - Pending: " + pendingMatches);
        System.out.println("  - Rejected: " + rejectedMatches);

        if (totalMatches > 0) {
            double matchRate = (confirmedMatches * 100.0) / totalMatches;
            System.out.printf("Match Confirmation Rate: %.2f%%\n", matchRate);
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
