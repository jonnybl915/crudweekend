package com.crooks;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;
import com.sun.org.apache.xpath.internal.operations.Bool;
import static spark.Spark.halt;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS restroomLog(id IDENTITY, location VARCHAR, visitDate VARCHAR, isSingleOccupant BOOLEAN, rating INT, userID)");
    }

    public static void insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL,?,?)");
        stmt.setString(1,name);
        stmt.setString(2,password);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1,name);
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            int id = results.getInt("id");
            String password = results.getString("password");
            return  new User(id,name,password);
        }
        return null;
    }

    public static void insertRestroom(Connection conn, String description, Double latitude, Double longitude, String visitDate, boolean isSingleOccupant, Integer rating, Integer userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restroomLog VALUES (NULL,?,?,?,?,?,?,?)");
        stmt.setString(1, description);
        stmt.setDouble(2, latitude);
        stmt.setDouble(3, longitude);
        stmt.setString(4, visitDate);
        stmt.setBoolean(5, isSingleOccupant);
        stmt.setInt(6, rating);
        stmt.setInt(7, userID);
        stmt.execute();

    }

    public static Restroom selectRestroom(Connection conn, Integer restroomID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restroomLog INNER JOIN users ON restroomLog.userID = users.id WHERE users.id=?");
        stmt.setInt(1,restroomID);
        ResultSet results = stmt.executeQuery();
        if(results.next()){
            String description = results.getString("description");
            Double latitude = results.getDouble("latitude");
            Double longitude = results.getDouble("longitude");
            String visitDate = results.getString("visitDate");
            boolean isSingleOccupant = results.getBoolean("isSingleOccupant");
            Integer rating = results.getInt("rating");
            return new Restroom(description,latitude,longitude,visitDate,isSingleOccupant,rating,restroomID);
        }
        return null;
    }

    public static ArrayList<Restroom> selectRestrooms(Connection conn, Integer userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restroomLog INNER JOIN users ON restroomLog.userID = users.id WHERE users.id=?");
        stmt.setInt(1, userID);
        ResultSet results = stmt.executeQuery();

        ArrayList<Restroom> restroomArrayList = new ArrayList<>();

        while (results.next()) {
            String description = results.getString("restroomLog.description");
            Double latitude = results.getDouble("restroomLog.latitude");
            Double longitude = results.getDouble("restroomLog.longitude");
            String visitDate = results.getString("restroomLog.visitDate");
            boolean isSingleOccupant = results.getBoolean("restroomLog.isSingleOccupant");
            Integer rating = results.getInt("restroomLog.rating");
            Integer userId = results.getInt("restroomLog.userID");

            Restroom r1 = new Restroom(description, latitude, longitude, visitDate, isSingleOccupant, rating, userId);
            restroomArrayList.add(r1);
        }
        return restroomArrayList;
    }

    public static void deleteRestroom(Connection conn, Integer restroomID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restroomLog WHERE id = ?");
        stmt.setInt(1, restroomID);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Spark.staticFileLocation("public");
        Spark.init();
    }
}
