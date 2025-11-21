package com.lostfound.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FoundItem {
    private int id;
    private String itemName;
    private String color;
    private String location;
    private String description;
    private String finderInfo;
    private LocalDateTime foundDate;

    public FoundItem(int id, String itemName, String color, String location,
                     String description, String finderInfo, LocalDateTime foundDate) {
        this.id = id;
        this.itemName = itemName;
        this.color = color;
        this.location = location;
        this.description = description;
        this.finderInfo = finderInfo;
        this.foundDate = foundDate;
    }

    public FoundItem(String itemName, String color, String location,
                     String description, String finderInfo) {
        this.itemName = itemName;
        this.color = color;
        this.location = location;
        this.description = description;
        this.finderInfo = finderInfo;
        this.foundDate = LocalDateTime.now();
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
    public String getFinderInfo() { return finderInfo; }
    public void setFinderInfo(String finderInfo) { this.finderInfo = finderInfo; }
    public LocalDateTime getFoundDate() { return foundDate; }
    public void setFoundDate(LocalDateTime foundDate) { this.foundDate = foundDate; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Found Item [ID: %d]\n  Name: %s\n  Color: %s\n  Location: %s\n  Description: %s\n  Finder: %s\n  Found: %s",
                id, itemName, color, location, description, finderInfo, foundDate.format(formatter));
    }
}