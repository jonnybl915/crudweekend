package com.crooks;

/**
 * Created by johncrooks on 6/16/16.
 */
public class Restroom {
    String description;
    String location;
    String visitDate;
    boolean isSingleOccupant;
    int rating;

    public Restroom(String description, String location, String visitDate, boolean isSingleOccupant, int rating) {
        this.description = description;
        this.location = location;
        this.visitDate = visitDate;
        this.isSingleOccupant = isSingleOccupant;
        this.rating = rating;
    }

    public Restroom() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public boolean isSingleOccupant() {
        return isSingleOccupant;
    }

    public void setSingleOccupant(boolean singleOccupant) {
        isSingleOccupant = singleOccupant;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
