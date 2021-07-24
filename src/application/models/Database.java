package application.models;

import java.sql.*;

public class Database {

    public static final String DB_USER = "studzzy";
    public static final String DB_PASSWORD = "57udzzy@123#";
    public static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/studzzy-db";

    private static Connection conn;

    public static Connection getConn() {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
        }
    }

    public static void open() {
        try {
            conn = getConn();
        } catch(Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

    public static void close() {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch(Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        }
    }

}
