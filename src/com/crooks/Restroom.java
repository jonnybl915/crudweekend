package com.crooks;

/**
 * Created by johncrooks on 6/16/16.
 */
public class Restroom {
    String description;
    String location;
    int rating;

    public Restroom() {
    }

    public Restroom(String description, String location, int rating) {

        this.description = description;
        this.location = location;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
