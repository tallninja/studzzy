package application.controllers;

import application.models.User;
import application.models.Database;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_REGISTRATION_NUMBER = "registration_number";
    public static final String COLUMN_UNIVERSITY = "university";
    public static final String COLUMN_START_SEM_DATE = "start_sem_date";
    public static final String COLUMN_END_SEM_DATE = "end_sem_date";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    // checks if a user exists before creating or updating
    public static boolean checkUserExists(String email) {

        assert conn != null;

        try {
            String sqlStatement = String.format("SELECT * FROM %s WHERE email=?", TABLE_USERS);
            statement = conn.prepareStatement(sqlStatement);
            statement.setString(1, email);
            results = statement.executeQuery();
            return results.next();
        } catch(Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return true;
        } finally {
            try {
                if (results != null) {
                    results.close();
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    // saves a user to the DB
    public static void saveUser(User user) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE primary key, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s DATE, %s DATE, %s TEXT, %s TEXT)",
                    TABLE_USERS, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_REGISTRATION_NUMBER, COLUMN_UNIVERSITY, COLUMN_START_SEM_DATE, COLUMN_END_SEM_DATE, COLUMN_EMAIL, COLUMN_PASSWORD);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkUserExists(user.getEmail())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        TABLE_USERS, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_REGISTRATION_NUMBER, COLUMN_UNIVERSITY, COLUMN_START_SEM_DATE, COLUMN_END_SEM_DATE, COLUMN_EMAIL, COLUMN_PASSWORD);
                statement = conn.prepareStatement(sqlStatement);
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getRegistrationNumber());
                statement.setString(4, user.getUniversity());
                statement.setDate(5, user.getStartSemDate());
                statement.setDate(6, user.getEndSemDate());
                statement.setString(7, user.getEmail());
                statement.setString(8, user.getPassword());
                statement.executeUpdate();
            }

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    // gets a single user
    public static @Nullable
    User getUser(String email) {

        assert conn != null;
        String sqlStatement;

        try {
            if(checkUserExists(email)) {
                sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_USERS, COLUMN_EMAIL);
                statement = conn.prepareStatement(sqlStatement);
                statement.setString(1, email);
                results = statement.executeQuery();


                results.next();
                return new User(results.getString(COLUMN_FIRST_NAME), results.getString(COLUMN_LAST_NAME),
                        results.getString(COLUMN_REGISTRATION_NUMBER), results.getString(COLUMN_UNIVERSITY),
                        results.getDate(COLUMN_START_SEM_DATE), results.getDate(COLUMN_END_SEM_DATE),
                        results.getString(COLUMN_EMAIL), results.getString(COLUMN_PASSWORD));


            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
        } finally {
            try {
                if(results != null) {
                    results.close();
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    // gets a list of all the users in our system
    public static List<User> getUsers() {

        assert conn != null;
        String sqlStatement;
        ResultSet results = null;

        try {
            sqlStatement = String.format("SELECT * FROM %s", TABLE_USERS);
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while(results.next()){
                User user = new User(results.getString(COLUMN_FIRST_NAME), results.getString(COLUMN_LAST_NAME),
                        results.getString(COLUMN_REGISTRATION_NUMBER), results.getString(COLUMN_UNIVERSITY),
                        results.getDate(COLUMN_START_SEM_DATE), results.getDate(COLUMN_END_SEM_DATE),
                        results.getString(COLUMN_EMAIL), results.getString(COLUMN_PASSWORD));
                users.add(user);
            }
            return users;

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
        } finally {
            try {
                if(results != null) {
                    results.close();
                    statement.close();
                }
            } catch(Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    // edit user data
    public static void editUser(User user) {

        assert conn != null;
        String sqlStatement;

        try {
            if (checkUserExists(user.getEmail())) {
                sqlStatement = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
                        TABLE_USERS, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_REGISTRATION_NUMBER, COLUMN_UNIVERSITY,
                        COLUMN_START_SEM_DATE, COLUMN_END_SEM_DATE, COLUMN_EMAIL);
                statement = conn.prepareStatement(sqlStatement);
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getRegistrationNumber());
                statement.setString(4, user.getUniversity());
                statement.setDate(5, user.getStartSemDate());
                statement.setDate(6, user.getEndSemDate());
                statement.setString(7, user.getEmail());
                statement.executeUpdate();
            } else {
                System.out.println("User does not exist !");
            }

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    // delete a user from our database
    public static void deleteUser(User user) {

        assert conn != null;
        String sqlStatement;


        try {
            if(checkUserExists(user.getEmail())) {
                sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_USERS, COLUMN_EMAIL);
                statement = conn.prepareStatement(sqlStatement);
                statement.setString(1, user.getEmail());
                statement.executeUpdate();
            } else {
                System.out.println("User does not exist");
            }
        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }
    }

    // delete CATs table
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DROP TABLE IF EXISTS %s", TABLE_USERS);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }

    }
}
