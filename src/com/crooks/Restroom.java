package com.crooks;

/**
 * Created by johncrooks on 6/16/16.
 */
public class Restroom {
    String description;
    Double latitude;
    Double longitude;
    String visitDate;
    boolean isSingleOccupant;
    Integer rating;
    Integer restroomID;

    public Restroom(String description, Double latitude, Double longitude, String visitDate, boolean isSingleOccupant, Integer rating,  Integer restroomID) {

        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visitDate = visitDate;
        this.isSingleOccupant = isSingleOccupant;
        this.rating = rating;
        this.restroomID = restroomID;
    }

    public Restroom() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public void setRating(Integer rating) {
        this.rating = rating;
    }


}
