package application.controllers;

import application.models.Database;
import application.models.Unit;
import application.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnitController {

    private static final Connection conn = Database.getConn();

    // checks if a unit exists before creating or updating
    public static boolean checkUnitExists(UUID uuid) {

        assert conn != null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            String sqlStatement = "SELECT * FROM users INNER JOIN units ON users.user_id=_user WHERE unit_id=?";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, uuid);
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

    // saves a unit to the DB
    public static void saveUnit(Unit unit) {

        assert conn != null;
        PreparedStatement statement = null;
        String sqlStatement;

        try {
            // create users table if it does not exist
            sqlStatement = "CREATE TABLE IF NOT EXISTS units (id SERIAL UNIQUE, unit_id UUID primary key, _user UUID references users(user_id), name TEXT, code TEXT, lecturer TEXT, pages INTEGER)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            // first check if a user exists
            if (!checkUnitExists(unit.getUuid())) {
                sqlStatement = "INSERT INTO units (unit_id, _user, name, code, lecturer, pages) VALUES (?, ?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, unit.getUuid());
                statement.setObject(2, unit.getUser().getUserId());
                statement.setString(3, unit.getName());
                statement.setString(4, unit.getCode());
                statement.setString(5, unit.getLecturer());
                statement.setInt(6, unit.getPages());
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

    // gets a single unit
    public static Unit getUnit(UUID uuid) {

        assert conn != null;
        PreparedStatement statement = null;
        String sqlStatement;
        ResultSet results = null;

        try {
            if(checkUnitExists(uuid)) {
                sqlStatement = "SELECT * FROM units INNER JOIN users ON users.user_id=_user WHERE unit_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();


                results.next();
                User user = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));
                return new Unit((UUID) results.getObject("unit_id"), user, results.getString("name"), results.getString("code"),
                        results.getString("lecturer"), results.getInt("pages"));


            } else {
                return null;
            }
        } catch (Exception e) {
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

    // gets a list of all the units in our DB
    public static List<Unit> getUnits() {

        assert  conn != null;
        PreparedStatement statement = null;
        String sqlStatement;
        ResultSet results = null;


        try {
            sqlStatement = "SELECT * FROM units INNER JOIN users ON users.user_id=_user";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            List<Unit> users = new ArrayList<>();
            while(results.next()){
                User user = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));
                Unit unit = new Unit((UUID) results.getObject("unit_id"), user, results.getString("name"), results.getString("code"),
                        results.getString("lecturer"), results.getInt("pages"));
                users.add(unit);
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

    // edit unit data
    public static void editUnit(Unit unit) {

        assert conn != null;
        PreparedStatement statement = null;
        String sqlStatement;

        try {
            if (checkUnitExists(unit.getUuid())) {
                sqlStatement = "UPDATE units SET name=?, code=?, lecturer=?, pages=? WHERE unit_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setString(1, unit.getName());
                statement.setString(2, unit.getCode());
                statement.setString(3, unit.getLecturer());
                statement.setInt(4, unit.getPages());
                statement.setObject(5, unit.getUuid());
                statement.executeUpdate();

            } else {
                System.out.println("Unit does not exist !");
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

    // delete a unit from our database
    public static void deleteUnit(Unit unit) {

        assert conn != null;
        PreparedStatement statement = null;
        String sqlStatement = "DELETE FROM units WHERE unit_id=?";

        try {
            if(checkUnitExists(unit.getUuid())) {
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, unit.getUuid());
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
        PreparedStatement statement = null;

        try {

            sqlStatement = "DROP TABLE IF EXISTS units CASCADE";
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
