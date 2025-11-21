package com.lostfound.dao;

import com.lostfound.db.DbConnection;
import com.lostfound.model.LostItem;
import com.lostfound.util.SimpleLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LostItemDAO{

    public int addLostItem(LostItem item) {
        String sql = "INSERT INTO LOST_ITEMS (item_name, color, location, description, contact_info, reported_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getColor());
            pstmt.setString(3, item.getLocation());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getContactInfo());
            pstmt.setTimestamp(6, Timestamp.valueOf(item.getReportedDate()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        item.setId(id);
                        SimpleLogger.log("Lost item added with ID: " + id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error adding lost item: " + e.getMessage());
        }

        return -1;
    }

    public LostItem getLostItemById(int id) {
        String sql = "SELECT * FROM LOST_ITEMS WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractLostItemFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving lost item with ID " + id + ": " + e.getMessage());
        }

        return null;
    }

    public List<LostItem> getAllLostItems() {
        List<LostItem> items = new ArrayList<>();
        String sql = "SELECT * FROM LOST_ITEMS ORDER BY reported_date DESC";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(extractLostItemFromResultSet(rs));
            }

            SimpleLogger.log("Retrieved " + items.size() + " lost items");
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving all lost items: " + e.getMessage());
        }

        return items;
    }

    public boolean updateLostItem(LostItem item) {
        String sql = "UPDATE LOST_ITEMS SET item_name = ?, color = ?, location = ?, " +
                "description = ?, contact_info = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getColor());
            pstmt.setString(3, item.getLocation());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getContactInfo());
            pstmt.setInt(6, item.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                SimpleLogger.log("Lost item updated: ID " + item.getId());
                return true;
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error updating lost item: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteLostItem(int id) {
        String sql = "DELETE FROM LOST_ITEMS WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                SimpleLogger.log("Lost item deleted: ID " + id);
                return true;
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error deleting lost item: " + e.getMessage());
        }

        return false;
    }

    private LostItem extractLostItemFromResultSet(ResultSet rs) throws SQLException {
        return new LostItem(
                rs.getInt("id"),
                rs.getString("item_name"),
                rs.getString("color"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getString("contact_info"),
                rs.getTimestamp("reported_date").toLocalDateTime()
        );
    }
}