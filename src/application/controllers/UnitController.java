package application.controllers;

import application.models.Database;
import application.models.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnitController {

    public static final String TABLE_UNITS = "units";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_LECTURER = "lecturer";
    public static final String COLUMN_PAGES = "pages";

    private static final Connection conn = Database.getConn();

    // checks if a unit exists before creating or updating
    public static boolean checkUnitExists(UUID uuid) {

        assert conn != null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            String sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_UNITS, COLUMN_UUID);
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
            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE, %s UUID primary key, %s TEXT, %s TEXT, %s TEXT, %s INTEGER)",
                    TABLE_UNITS, COLUMN_UUID, COLUMN_NAME, COLUMN_CODE, COLUMN_LECTURER, COLUMN_PAGES);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            // first check if a user exists
            if (!checkUnitExists(unit.getUuid())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
                        TABLE_UNITS, COLUMN_UUID, COLUMN_NAME, COLUMN_CODE, COLUMN_LECTURER, COLUMN_PAGES);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, unit.getUuid());
                statement.setString(2, unit.getName());
                statement.setString(3, unit.getCode());
                statement.setString(4, unit.getLecturer());
                statement.setInt(5, unit.getPages());
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
                sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_UNITS, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();


                results.next();
                return new Unit((UUID) results.getObject(COLUMN_UUID), results.getString(COLUMN_NAME), results.getString(COLUMN_CODE),
                        results.getString(COLUMN_LECTURER), results.getInt(COLUMN_PAGES));


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
            sqlStatement = String.format("SELECT * FROM %s", TABLE_UNITS);
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            List<Unit> users = new ArrayList<>();
            while(results.next()){
                Unit unit = new Unit((UUID) results.getObject(COLUMN_UUID), results.getString(COLUMN_NAME), results.getString(COLUMN_CODE),
                        results.getString(COLUMN_LECTURER), results.getInt(COLUMN_PAGES));
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
                sqlStatement = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=? WHERE %s=?",
                        TABLE_UNITS, COLUMN_NAME, COLUMN_CODE, COLUMN_LECTURER, COLUMN_PAGES, COLUMN_UUID);
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
        String sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_UNITS, COLUMN_UUID);

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

            sqlStatement = String.format("DROP TABLE IF EXISTS %s CASCADE", TABLE_UNITS);
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
