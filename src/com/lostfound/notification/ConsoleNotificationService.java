package com.lostfound.notification;

import com.lostfound.model.Match;
import com.lostfound.model.LostItem;
import com.lostfound.model.FoundItem;
import com.lostfound.util.SimpleLogger;

import java.time.format.DateTimeFormatter;

public class ConsoleNotificationService implements NotificationService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void notifyMatch(Match match, LostItem lostItem, FoundItem foundItem) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🔔 MATCH NOTIFICATION");
        System.out.println("=".repeat(70));

        System.out.println("\nMatch Details:");
        System.out.println("  Match ID: " + match.getMatchId());
        System.out.println("  Score: " + match.getScore() + "/100");
        System.out.println("  Status: " + match.getStatus());
        System.out.println("  Match Date: " + match.getMatchDate().format(DATE_FORMATTER));

        System.out.println("\nLost Item:");
        System.out.println("  ID: " + lostItem.getId());
        System.out.println("  Item Name: " + lostItem.getItemName());
        System.out.println("  Color: " + lostItem.getColor());
        System.out.println("  Location: " + lostItem.getLocation());
        System.out.println("  Description: " + lostItem.getDescription());
        System.out.println("  Contact: " + lostItem.getContactInfo());
        System.out.println("  Reported: " + lostItem.getReportedDate().format(DATE_FORMATTER));

        System.out.println("\nFound Item:");
        System.out.println("  ID: " + foundItem.getId());
        System.out.println("  Item Name: " + foundItem.getItemName());
        System.out.println("  Color: " + foundItem.getColor());
        System.out.println("  Location: " + foundItem.getLocation());
        System.out.println("  Description: " + foundItem.getDescription());
        System.out.println("  Finder: " + foundItem.getFinderInfo());
        System.out.println("  Found: " + foundItem.getFoundDate().format(DATE_FORMATTER));

        System.out.println("\n" + "=".repeat(70));
        System.out.println("Both parties have been notified. Please contact each other to verify.");
        System.out.println("=".repeat(70) + "\n");

        SimpleLogger.log("Notification sent for match ID: " + match.getMatchId());
    }
}