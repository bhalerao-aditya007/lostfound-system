package com.lostfound.dao;

import com.lostfound.db.DbConnection;
import com.lostfound.model.FoundItem;
import com.lostfound.util.SimpleLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoundItemDAO {

    public int addFoundItem(FoundItem item) {
        String sql = "INSERT INTO FOUND_ITEMS (item_name, color, location, description, finder_info, found_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getColor());
            pstmt.setString(3, item.getLocation());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getFinderInfo());
            pstmt.setTimestamp(6, Timestamp.valueOf(item.getFoundDate()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        item.setId(id);
                        SimpleLogger.log("Found item added with ID: " + id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error adding found item: " + e.getMessage());
        }

        return -1;
    }

    public FoundItem getFoundItemById(int id) {
        String sql = "SELECT * FROM FOUND_ITEMS WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractFoundItemFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving found item with ID " + id + ": " + e.getMessage());
        }

        return null;
    }

    public List<FoundItem> getAllFoundItems() {
        List<FoundItem> items = new ArrayList<>();
        String sql = "SELECT * FROM FOUND_ITEMS ORDER BY found_date DESC";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                items.add(extractFoundItemFromResultSet(rs));
            }

            SimpleLogger.log("Retrieved " + items.size() + " found items");
        } catch (SQLException e) {
            SimpleLogger.error("Error retrieving all found items: " + e.getMessage());
        }

        return items;
    }

    public boolean updateFoundItem(FoundItem item) {
        String sql = "UPDATE FOUND_ITEMS SET item_name = ?, color = ?, location = ?, " +
                "description = ?, finder_info = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getColor());
            pstmt.setString(3, item.getLocation());
            pstmt.setString(4, item.getDescription());
            pstmt.setString(5, item.getFinderInfo());
            pstmt.setInt(6, item.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                SimpleLogger.log("Found item updated: ID " + item.getId());
                return true;
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error updating found item: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteFoundItem(int id) {
        String sql = "DELETE FROM FOUND_ITEMS WHERE id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                SimpleLogger.log("Found item deleted: ID " + id);
                return true;
            }
        } catch (SQLException e) {
            SimpleLogger.error("Error deleting found item: " + e.getMessage());
        }

        return false;
    }

    private FoundItem extractFoundItemFromResultSet(ResultSet rs) throws SQLException {
        return new FoundItem(
                rs.getInt("id"),
                rs.getString("item_name"),
                rs.getString("color"),
                rs.getString("location"),
                rs.getString("description"),
                rs.getString("finder_info"),
                rs.getTimestamp("found_date").toLocalDateTime()
        );
    }
}
