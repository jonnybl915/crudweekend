package com.crooks;

import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Session;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

import static spark.Spark.halt;
import static spark.Spark.port;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, username VARCHAR, password VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS restroomLog(id IDENTITY, description VARCHAR, latitude DOUBLE, longitude DOUBLE, visitDate VARCHAR, isClean BOOLEAN, rating INT, userID INT)");
    }
    public static int insertUser(Connection conn, String name, String password) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL,?,?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1,name);
        stmt.setString(2,password);
        stmt.execute();

        // return id
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;

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

    public static int insertRestroom(Connection conn, String description, Double latitude, Double longitude, String visitDate, boolean isClean, Integer rating, Integer userID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO restroomLog VALUES (NULL,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, description);
        stmt.setDouble(2, latitude);
        stmt.setDouble(3, longitude);
        stmt.setString(4, visitDate);
        stmt.setBoolean(5, isClean);
        stmt.setInt(6, rating);
        stmt.setInt(7, userID);
        stmt.execute();

        // return id
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;

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
            boolean isClean = results.getBoolean("isClean");
            Integer rating = results.getInt("rating");
            Integer userId = results.getInt("userId");
            return new Restroom(description,latitude,longitude,visitDate,isClean,rating,restroomID, userId);
        }
        return null;
    }

    public static ArrayList<Restroom> selectRestrooms(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM restroomLog");
        ResultSet results = stmt.executeQuery();

        ArrayList<Restroom> restroomArrayList = new ArrayList<>();

        while (results.next()) {
            String description = results.getString("restroomLog.description");
            Double latitude = results.getDouble("restroomLog.latitude");
            Double longitude = results.getDouble("restroomLog.longitude");
            String visitDate = results.getString("restroomLog.visitDate");
            boolean isClean = results.getBoolean("restroomLog.isClean");
            Integer rating = results.getInt("restroomLog.rating");
            Integer restroomId = results.getInt("restroomLog.id");
            Integer userId = results.getInt("restroomLog.userID");

            Restroom r1 = new Restroom(description, latitude, longitude, visitDate, isClean, rating, restroomId, userId);
            restroomArrayList.add(r1);
        }
        return restroomArrayList;
    }
    public static Restroom updateRestroom(Connection conn, String description, Double latitude, Double longitude, String visitDate, boolean isClean, Integer rating,Integer userId, Integer restroomId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("Update restroomLog SET description = ?, latitude = ?, longitude = ?, visitDate = ?, isClean = ?, rating = ?, userId = ? WHERE id = ?");
        stmt.setString(1, description);
        stmt.setDouble(2, latitude);
        stmt.setDouble(3, longitude);
        stmt.setString(4, visitDate);
        stmt.setBoolean(5, isClean);
        stmt.setInt(6, rating);
        stmt.setInt(7, restroomId);
        stmt.setInt(8, userId);
        stmt.execute();
        return new Restroom(description, latitude, longitude, visitDate, isClean, rating, restroomId, userId);
    }

    public static void deleteRestroom(Connection conn, Integer restroomID) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM restroomLog WHERE id = ?");
        stmt.setInt(1, restroomID);
        stmt.execute();
    }

    public static void main(String[] args) throws SQLException {
        port(3000);
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);
        Spark.externalStaticFileLocation("public");
        Spark.init();
        Spark.post(
                "/login",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    if(username ==null) {

                        String body = request.body();
                        JsonParser parser = new JsonParser();
                        User loguser = parser.parse(body, User.class);

                        User user = selectUser(conn, loguser.username);
                        if (user == null) {
                            if(loguser.username.isEmpty()) {
                                throw new Exception("YOU MUST FILL IN ALL FIELDS");
                            } else {
                                insertUser(conn, loguser.username, loguser.password);
                            }
                        } else if (!user.password.equals(loguser.password)) {
                            //halt(400, "Incorrect Username/Password Combination.\n" +
                                    //"Please Go Back");
                            throw new Exception("you done goofed");
                        }
                        session.attribute("username", loguser.username);
                    }
                    return "success"; //or throw error
                }
        );
        Spark.get(
                "/skipToTheLoo",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        halt("YOU MUST LOG IN!!!");
                    }

                    ArrayList<Restroom> restrooms = selectRestrooms(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(restrooms);
                }
        );
        Spark.get(
                "/skipToTheLoo/:id",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        halt("YOU MUST LOG IN!!!");
                    }

                    Integer id = Integer.valueOf(request.params(":id"));
                    Restroom restroom = selectRestroom(conn, id );
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(restroom);
                }
        );

        Spark.post(
                "/skipToTheLoo",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    User user = selectUser(conn, username);
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    Restroom restroom = parser.parse(body, Restroom.class);
                    insertRestroom(conn, restroom.description, restroom.latitude, restroom.longitude, restroom.visitDate, restroom.isClean, restroom.rating, user.id);

//                    Integer id = Integer.valueOf(request.params("restroomId"));
//                    Restroom restroom1 = selectRestroom(conn, id );
//                    JsonSerializer s = new JsonSerializer();
                    return "";
                }
        );
        Spark.put(
                "/skipToTheLoo",
                (request, response) -> {
                    String body = request.body();
                    JsonParser parser = new JsonParser();
                    Restroom restroom = parser.parse(body, Restroom.class);
                    updateRestroom(conn, restroom.description, restroom.latitude, restroom.longitude, restroom.visitDate, restroom.isClean, restroom.rating, restroom.userId, restroom.restroomId);
                    return "";
                }
        );
        Spark.delete(
                "/skipToTheLoo/:id",
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
}
