package com.crooks;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jonathandavidblack on 6/17/16.
 */
public class MainTest4 {

    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }
    @Test
    public void testInsertUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "");
        User testUser = Main.selectUser(conn, "Alice");
        conn.close();
        assertTrue(testUser.username.equals("Alice"));
    }
    @Test
    public void testInsertRestroom() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "");
        Main.insertRestroom(conn, "disgusting", 10.2222, 10.66666, "10/22/1985", false, 1, 1);
        Restroom restroom = Main.selectRestroom(conn, 1);
        conn.close();
        assertTrue(restroom != null);
    }
    @Test
    public void testSelectRestrooms() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Bob", "");
        Main.insertUser(conn, "Charlie", "");
        Main.insertRestroom(conn, "quality porcelain, freezing a/c", 10.22, -10.00, "10/1/11", true, 3, 1);
        Main.insertRestroom(conn, "gross", 20.44, -20.55, "10.22.99", false, 2, 2);
        ArrayList<Restroom> list = Main.selectRestrooms(conn);
        conn.close();
        assertTrue(list.size() > 1);
    }
    @Test
    public void testUpdateRestroom() throws SQLException {
        Connection conn = startConnection();
        Main.insertRestroom(conn, "dirty af", 10.88, -10.99, "3/05/16", false, 1, 1);
        Main.updateRestroom(conn, "GREAT", 10.33, -9.33, "2/2/12", true, 5, 1);
        Restroom test1;
        Main.selectRestroom(conn, 1);
        ArrayList<Restroom> testList = Main.selectRestrooms(conn);
        test1 = testList.get(0);

        conn.close();
        assertTrue(test1.description.equals("GREAT"));
    }
    @Test
    public void testDeleteRestroom() throws SQLException {
        Connection conn = startConnection();
        Main.insertRestroom(conn, "nasty", 10.88, -10.99, "2/2/22", true, 4, 1);
        Main.insertRestroom(conn, "sdfdsa", 10.99, -8.00, "3/33/11", false, 2, 2);
        Main.deleteRestroom(conn, 1);
        ArrayList<Restroom> restList = Main.selectRestrooms(conn);
        conn.close();
        assertTrue(restList.size() == 1);
        Restroom test5 = restList.get(0);
        assertTrue(test5.isClean == false);





    }
}