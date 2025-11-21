package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Match {
    private int matchId;
    private int lostItemId;
    private int foundItemId;
    private int score;
    private LocalDateTime matchDate;
    private String status;

    public Match(int matchId, int lostItemId, int foundItemId, int score,
                 LocalDateTime matchDate, String status) {
        this.matchId = matchId;
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.score = score;
        this.matchDate = matchDate;
        this.status = status;
    }

    public Match(int lostItemId, int foundItemId, int score, String status) {
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.score = score;
        this.matchDate = LocalDateTime.now();
        this.status = status;
    }

    // Getters and Setters
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }
    public int getLostItemId() { return lostItemId; }
    public void setLostItemId(int lostItemId) { this.lostItemId = lostItemId; }
    public int getFoundItemId() { return foundItemId; }
    public void setFoundItemId(int foundItemId) { this.foundItemId = foundItemId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public LocalDateTime getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Match [ID: %d] - Score: %d - Status: %s\n  Lost Item ID: %d\n  Found Item ID: %d\n  Match Date: %s",
                matchId, score, status, lostItemId, foundItemId, matchDate.format(formatter));
    }
}