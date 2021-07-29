package application.controllers;

import application.models.User;
import application.models.Database;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserController {

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    // checks if a user exists before creating or updating
    public static boolean checkUserExists(String email) {

        assert conn != null;

        try {
            String sqlStatement = "SELECT * FROM users WHERE email=?";
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

            sqlStatement = "CREATE TABLE IF NOT EXISTS users (id SERIAL UNIQUE, user_id UUID primary key, first_name TEXT, last_name TEXT, registration_number TEXT, university TEXT, start_sem_date DATE, end_sem_date DATE, email TEXT, password TEXT)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkUserExists(user.getEmail())) {
                sqlStatement = "INSERT INTO users (user_id, first_name, last_name, registration_number, university, start_sem_date, end_sem_date, email, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, user.getUserId());
                statement.setString(2, user.getFirstName());
                statement.setString(3, user.getLastName());
                statement.setString(4, user.getRegistrationNumber());
                statement.setString(5, user.getUniversity());
                statement.setDate(6, user.getStartSemDate());
                statement.setDate(7, user.getEndSemDate());
                statement.setString(8, user.getEmail());
                statement.setString(9, user.getPassword());
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
    public static User getUser(UUID userId) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "SELECT * FROM users WHERE user_id=?";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, userId);
            results = statement.executeQuery();


            if (results.next()) {
                return new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));
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

    // gets a single user
    public static User getUser(String email) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "SELECT * FROM users WHERE email=?";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, email);
            results = statement.executeQuery();


            results.next();
            return new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                    results.getString("registration_number"), results.getString("university"),
                    results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                    results.getString("email"), results.getString("password"));



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
            sqlStatement = "SELECT * FROM users";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while(results.next()){
                User user = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));
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
                sqlStatement = "UPDATE users SET first_name=?, last_name=?, registration_number=?, university=?, start_sem_date=?, end_sem_date=? WHERE user_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setString(1, user.getFirstName());
                statement.setString(2, user.getLastName());
                statement.setString(3, user.getRegistrationNumber());
                statement.setString(4, user.getUniversity());
                statement.setDate(5, user.getStartSemDate());
                statement.setDate(6, user.getEndSemDate());
                statement.setObject(7, user.getUserId());
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
                sqlStatement = "DELETE FROM users WHERE user_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, user.getUserId());
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

            sqlStatement = "DROP TABLE IF EXISTS users CASCADE";
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
