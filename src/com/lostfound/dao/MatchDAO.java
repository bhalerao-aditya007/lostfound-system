package com.lostfound.dao;

import com.lostfound.db.DbConnection;
import com.lostfound.model.Match;
import com.lostfound.util.SimpleLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {

    public int addMatch(Match match) {
        String sql = "INSERT INTO MATCHES (lost_item_id, found_item_id, score, match_date, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, match.getLostItemId());
            pstmt.setInt(2, match.getFoundItemId());
            pstmt.setInt(3, match.getScore());
            pstmt.setTimestamp(4, Timestamp.valueOf(match.getMatchDate()));
            pstmt.setString(5, match.getStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        match.setMatchId(id);
                        SimpleLogger.log("Match added with ID: " + id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error adding match: " + e.getMessage());
        }

        return -1;
    }

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT * FROM MATCHES ORDER BY match_date DESC";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                matches.add(extractMatchFromResultSet(rs));
            }

            SimpleLogger.log("Retrieved " + matches.size() + " matches");
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving all matches: " + e.getMessage());
        }

        return matches;
    }

    public List<Match> getMatchesForLostItem(int lostItemId) {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT * FROM MATCHES WHERE lost_item_id = ? ORDER BY score DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lostItemId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(extractMatchFromResultSet(rs));
                }
            }

            SimpleLogger.log("Retrieved " + matches.size() + " matches for lost item ID " + lostItemId);
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving matches for lost item: " + e.getMessage());
        }

        return matches;
    }

    public List<Match> getMatchesForFoundItem(int foundItemId) {
        List<Match> matches = new ArrayList<>();
        String sql = "SELECT * FROM MATCHES WHERE found_item_id = ? ORDER BY score DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, foundItemId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(extractMatchFromResultSet(rs));
                }
            }

            SimpleLogger.log("Retrieved " + matches.size() + " matches for found item ID " + foundItemId);
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving matches for found item: " + e.getMessage());
        }

        return matches;
    }

    public boolean updateMatchStatus(int matchId, String status) {
        String sql = "UPDATE MATCHES SET status = ? WHERE match_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, matchId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                SimpleLogger.log("Match status updated: ID " + matchId + " -> " + status);
                return true;
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error updating match status: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteMatch(int matchId) {
        String sql = "DELETE FROM MATCHES WHERE match_id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, matchId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                SimpleLogger.log("Match deleted: ID " + matchId);
                return true;
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error deleting match: " + e.getMessage());
        }

        return false;
    }

    private Match extractMatchFromResultSet(ResultSet rs) throws SQLException {
        return new Match(
                rs.getInt("match_id"),
                rs.getInt("lost_item_id"),
                rs.getInt("found_item_id"),
                rs.getInt("score"),
                rs.getTimestamp("match_date").toLocalDateTime(),
                rs.getString("status")
        );
    }
}
