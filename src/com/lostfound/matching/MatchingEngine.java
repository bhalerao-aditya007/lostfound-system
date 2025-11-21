package com.lostfound.matching;

import com.lostfound.dao.LostItemDAO;
import com.lostfound.dao.FoundItemDAO;
import com.lostfound.db.DbConnection;
import com.lostfound.model.LostItem;
import com.lostfound.model.FoundItem;
import com.lostfound.model.Match;
import com.lostfound.util.SimpleLogger;

import java.util.*;

public class MatchingEngine {
    private int threshold;
    private LostItemDAO lostItemDAO;
    private FoundItemDAO foundItemDAO;

    public MatchingEngine() {
        String thresholdStr = DbConnection.getProperty("match.threshold");
        this.threshold = (thresholdStr != null) ? Integer.parseInt(thresholdStr) : 70;
        this.lostItemDAO = new LostItemDAO();
        this.foundItemDAO = new FoundItemDAO();
        SimpleLogger.log("MatchingEngine initialized with threshold: " + threshold);
    }

    public int calculateMatchScore(LostItem lostItem, FoundItem foundItem) {
        int score = 0;

        // Item name match: +50 points
        String lostName = normalize(lostItem.getItemName());
        String foundName = normalize(foundItem.getItemName());
        if (lostName.equals(foundName)) {
            score += 50;
        }

        // Color match: +20 points
        String lostColor = normalize(lostItem.getColor());
        String foundColor = normalize(foundItem.getColor());
        if (lostColor.equals(foundColor)) {
            score += 20;
        }

        // Location match: +20 points
        String lostLocation = normalize(lostItem.getLocation());
        String foundLocation = normalize(foundItem.getLocation());
        if (lostLocation.equals(foundLocation)) {
            score += 20;
        }

        // Description keyword overlap: +10 points
        Set<String> lostKeywords = extractKeywords(lostItem.getDescription());
        Set<String> foundKeywords = extractKeywords(foundItem.getDescription());

        Set<String> intersection = new HashSet<>(lostKeywords);
        intersection.retainAll(foundKeywords);

        if (!intersection.isEmpty()) {
            score += 10;
        }

        return score;
    }

    public List<Match> findMatchesForFoundItem(FoundItem foundItem) {
        List<Match> matches = new ArrayList<>();
        List<LostItem> allLostItems = lostItemDAO.getAllLostItems();

        SimpleLogger.log("Searching for matches among " + allLostItems.size() + " lost items...");

        for (LostItem lostItem : allLostItems) {
            int score = calculateMatchScore(lostItem, foundItem);

            if (score >= threshold) {
                Match match = new Match(lostItem.getId(), foundItem.getId(), score, "PENDING");
                matches.add(match);
                SimpleLogger.log(String.format("Match found: Lost ID %d <-> Found ID %d (Score: %d)",
                        lostItem.getId(), foundItem.getId(), score));
            }
        }

        SimpleLogger.log("Total matches found: " + matches.size());
        return matches;
    }

    public List<Match> findMatchesForLostItem(LostItem lostItem) {
        List<Match> matches = new ArrayList<>();
        List<FoundItem> allFoundItems = foundItemDAO.getAllFoundItems();

        SimpleLogger.log("Searching for matches among " + allFoundItems.size() + " found items...");

        for (FoundItem foundItem : allFoundItems) {
            int score = calculateMatchScore(lostItem, foundItem);

            if (score >= threshold) {
                Match match = new Match(lostItem.getId(), foundItem.getId(), score, "PENDING");
                matches.add(match);
                SimpleLogger.log(String.format("Match found: Lost ID %d <-> Found ID %d (Score: %d)",
                        lostItem.getId(), foundItem.getId(), score));
            }
        }

        SimpleLogger.log("Total matches found: " + matches.size());
        return matches;
    }

    private String normalize(String str) {
        if (str == null) {
            return "";
        }
        return str.toLowerCase().trim();
    }

    private Set<String> extractKeywords(String description) {
        Set<String> keywords = new HashSet<>();

        if (description == null || description.trim().isEmpty()) {
            return keywords;
        }

        String[] words = description.split("\\W+");

        for (String word : words) {
            String normalized = normalize(word);
            if (!normalized.isEmpty()) {
                keywords.add(normalized);
            }
        }

        return keywords;
    }

    public int getThreshold() {
        return threshold;
    }
}