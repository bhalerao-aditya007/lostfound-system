package com.lostfound.util;

import com.lostfound.model.LostItem;
import com.lostfound.model.FoundItem;
import com.lostfound.model.Match;

import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class for exporting data to CSV files.
 */
public class CsvExporter {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static boolean exportLostItems(List<LostItem> items, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("ID,Item Name,Color,Location,Description,Contact Info,Reported Date\n");

            for (LostItem item : items) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        item.getId(),
                        escapeCsv(item.getItemName()),
                        escapeCsv(item.getColor()),
                        escapeCsv(item.getLocation()),
                        escapeCsv(item.getDescription()),
                        escapeCsv(item.getContactInfo()),
                        item.getReportedDate().format(DATE_FORMATTER)
                ));
            }

            SimpleLogger.log("Exported " + items.size() + " lost items to " + filename);
            return true;
        } catch (IOException e) {
            SimpleLogger.error("Failed to export lost items: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportFoundItems(List<FoundItem> items, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("ID,Item Name,Color,Location,Description,Finder Info,Found Date\n");

            for (FoundItem item : items) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                        item.getId(),
                        escapeCsv(item.getItemName()),
                        escapeCsv(item.getColor()),
                        escapeCsv(item.getLocation()),
                        escapeCsv(item.getDescription()),
                        escapeCsv(item.getFinderInfo()),
                        item.getFoundDate().format(DATE_FORMATTER)
                ));
            }

            SimpleLogger.log("Exported " + items.size() + " found items to " + filename);
            return true;
        } catch (IOException e) {
            SimpleLogger.error("Failed to export found items: " + e.getMessage());
            return false;
        }
    }

    public static boolean exportMatches(List<Match> matches, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Match ID,Lost Item ID,Found Item ID,Score,Match Date,Status\n");

            for (Match match : matches) {
                writer.write(String.format("%d,%d,%d,%d,%s,%s\n",
                        match.getMatchId(),
                        match.getLostItemId(),
                        match.getFoundItemId(),
                        match.getScore(),
                        match.getMatchDate().format(DATE_FORMATTER),
                        match.getStatus()
                ));
            }

            SimpleLogger.log("Exported " + matches.size() + " matches to " + filename);
            return true;
        } catch (IOException e) {
            SimpleLogger.error("Failed to export matches: " + e.getMessage());
            return false;
        }
    }

    private static String escapeCsv(String field) {
        if (field == null) {
            return "";
        }

        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }
}