package application.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Database {

    public static final String DB_USER = "studzzy";
    public static final String DB_PASSWORD = "57udzzy@123#";
    public static final String DB_CONNECTION_URL = "jdbc:postgresql://localhost:5432/studzzy-db";


    public static final String TABLE_EXAMS = "exams";
    public static final String TABLE_CATS = "cats";
    public static final String TABLE_REPORTS = "reports";
    public static final String TABLE_ASSIGNMENTS = "assignments";
    public static final String TABLE_REMINDERS = "reminders";


    private static Connection conn;
    private static Statement statement;


    public static Connection getConn() {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
        }
    }

    public static Statement getStatement() {
        return statement;
    }

    public static boolean open() {
        try {
            conn = DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);
            return true;

        } catch(Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return false;
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
