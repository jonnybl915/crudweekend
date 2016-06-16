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

    }













































































    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        Spark.staticFileLocation("public");
        Spark.init();
        Spark.get(
                "/skipToMyLoo",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = selectUser(conn, username);
                    if (user == null) {
                        insertUser(conn, name, pass);
                    }
                    else if(!pass.equals(user.password)) {
                        halt("Incorrect Username/Password Combination.\n" +
                                "Please Go Back");
                    }
                    ArrayList<Restroom> restrooms = selectRestrooms(conn);
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
                    insertRestroom(conn, restroom);
                    return "";
                }
        );
        Spark.put(
                "/skipToMyLoo",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    Restroom restroom = parser.parse(body, Restroom.class);
                    updateRestroom(conn, restroom);
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
    public static Restroom updateRestroom(Connection conn, String description, Double latitude, Double longitude, String visitDate, boolean isSingleOccupant, Integer rating, Integer restroomId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("Update restroomLog SET description = ?, latitude = ?, longitude = ?, visitDate = ?, isSingleOccupant = ?, rating = ?, WHERE RestroomId = ?");
        stmt.setString(1, description);
        stmt.setDouble(2, latitude);
        stmt.setDouble(3, longitude);
        stmt.setString(4, visitDate);
        stmt.setBoolean(5, isSingleOccupant);
        stmt.setInt(6, rating);
        stmt.execute();
        return new Restroom(description, latitude, longitude, visitDate, isSingleOccupant, rating, restroomId);
    }
}
