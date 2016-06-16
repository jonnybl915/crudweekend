package com.crooks;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

import static spark.Spark.halt;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS restroomLog(id IDENTITY, description VARCHAR, latitude DOUBLE, longitude DOUBLE, visitDate VARCHAR, isClean BOOLEAN, rating INT, userID INT)");
    }
    public static void insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL,?,?)");
        stmt.setString(1,name);
        stmt.setString(2,password);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        stmt.setString(1,name);
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            int id = results.getInt("id");
            String password = results.getString("password");
            return  new User(id,name,password);
        }
        return null;
    }

    public static void insertRestroom(Connection conn, String description, Double latitude, Double longitude, String visitDate, boolean isClean, Integer rating, Integer userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restroomLog VALUES (NULL,?,?,?,?,?,?,?)");
        stmt.setString(1, description);
        stmt.setDouble(2, latitude);
        stmt.setDouble(3, longitude);
        stmt.setString(4, visitDate);
        stmt.setBoolean(5, isClean);
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
            boolean isClean = results.getBoolean("restroomLog.isClean");
            Integer rating = results.getInt("restroomLog.rating");
            Integer userId = results.getInt("restroomLog.userID");

            Restroom r1 = new Restroom(description, latitude, longitude, visitDate, isClean, rating, userId);
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
        createTables(conn);
        Spark.staticFileLocation("public");
        Spark.init();
        Spark.get(
                "/skipToMyLoo",
                (request, response) -> {
//                    Session session = request.session();
//                    String username = session.attribute("username");
////                    String password = session.attribute("password");
//                    User user = selectUser(conn, username);
//
//                    if (user == null) {
//                        insertUser(conn, username, password);
//                    }
////                    else if(!password.equals(user.password)) {
////                        halt("Incorrect Username/Password Combination.\n" +
////                                "Please Go Back");
////                    }

                    insertUser(conn,"j","");
                    User user = selectUser(conn,"j");

                    insertRestroom(conn, "Very Clean, Could eat my breakfast off the floor", 32.109, -79.736, "Friday the 13th", true, 4, user.id);
                    ArrayList<Restroom> restrooms = selectRestrooms(conn, user.id);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(restrooms);
                }
        );
        Spark.post(
                "/skipToMyLoo",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    Restroom restroom = parser.parse(body, Restroom.class);
                    insertRestroom(conn, restroom.description, restroom.latitude, restroom.longitude, restroom.visitDate, restroom.isClean, restroom.rating, restroom.restroomId);
                    return "";
                }
        );
        Spark.put(
                "/skipToMyLoo",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    Restroom restroom = parser.parse(body, Restroom.class);
                    updateRestroom(conn, restroom.description, restroom.latitude, restroom.longitude, restroom.visitDate, restroom.isClean, restroom.rating, restroom.restroomId);
                    return "";
                }
        );
        Spark.delete(
                "/skipToMyLoo/:id",
                (request, response) -> {
                    Integer id = Integer.valueOf(request.params(":id"));
                    deleteRestroom(conn, id);
                    return "";
                }
        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    return "";
                }
        );
    }
    public static Restroom updateRestroom(Connection conn, String description, Double latitude, Double longitude, String visitDate, boolean isClean, Integer rating, Integer restroomId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("Update restroomLog SET description = ?, latitude = ?, longitude = ?, visitDate = ?, isClean = ?, rating = ?, WHERE RestroomId = ?");
        stmt.setString(1, description);
        stmt.setDouble(2, latitude);
        stmt.setDouble(3, longitude);
        stmt.setString(4, visitDate);
        stmt.setBoolean(5, isClean);
        stmt.setInt(6, rating);
        stmt.execute();
        return new Restroom(description, latitude, longitude, visitDate, isClean, rating, restroomId);
    }
}
