package com.crooks;

/**
 * Created by johncrooks on 6/16/16.
 */
public class Restroom {
    String description;
    Double latitude;
    Double longitude;
    String visitDate;
    boolean isClean;
    Integer rating;
    Integer restroomId;
    Integer userId;

    public Restroom(String description, Double latitude, Double longitude, String visitDate, boolean isClean, Integer rating, Integer restroomId, Integer UserId) {

        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visitDate = visitDate;
        this.isClean = isClean;
        this.rating = rating;
        this.restroomId = restroomId;
        this.userId = userId;
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

    public boolean isClean() {
        return isClean;
    }

    public void setClean(boolean clean) {
        isClean = clean;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRestroomId() {
        return restroomId;
    }

    public void setRestroomId(Integer restroomId) {
        this.restroomId = restroomId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
