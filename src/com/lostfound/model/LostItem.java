package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LostItem {
    private int id;
    private String itemName;
    private String color;
    private String location;
    private String description;
    private String contactInfo;
    private LocalDateTime reportedDate;

    public LostItem(int id, String itemName, String color, String location,
                    String description, String contactInfo, LocalDateTime reportedDate) {
        this.id = id;
        this.itemName = itemName;
        this.color = color;
        this.location = location;
        this.description = description;
        this.contactInfo = contactInfo;
        this.reportedDate = reportedDate;
    }

    public LostItem(String itemName, String color, String location,
                    String description, String contactInfo) {
        this.itemName = itemName;
        this.color = color;
        this.location = location;
        this.description = description;
        this.contactInfo = contactInfo;
        this.reportedDate = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public LocalDateTime getReportedDate() { return reportedDate; }
    public void setReportedDate(LocalDateTime reportedDate) { this.reportedDate = reportedDate; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Lost Item [ID: %d]\n  Name: %s\n  Color: %s\n  Location: %s\n  Description: %s\n  Contact: %s\n  Reported: %s",
                id, itemName, color, location, description, contactInfo, reportedDate.format(formatter));
    }
}