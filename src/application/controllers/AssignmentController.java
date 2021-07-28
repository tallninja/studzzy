package application.controllers;

import application.models.Assignment;
import application.models.Database;
import application.models.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class AssignmentController {

    private static final String TABLE_ASSIGNMENTS = "assignments";
    private static final String COLUMN_UUID = "uuid";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkAssignmentExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_ASSIGNMENTS, COLUMN_UUID);
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, uuid);
            results = statement.executeQuery();

            return results.next();

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return false;
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

    // save an Assignment to our DB
    public static void saveAssignment(Assignment assignment) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE, " +
                            "%s UUID primary key, %s UUID references units(uuid), %s DATE, %s INTEGER)",
                    TABLE_ASSIGNMENTS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkAssignmentExists(assignment.getUuid())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                        TABLE_ASSIGNMENTS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, assignment.getUuid());
                statement.setObject(2, assignment.getUnitObject().getUuid());
                statement.setDate(3, assignment.getDate());
                statement.setInt(4, assignment.getTypeInt());
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

    // get a single Assignment
    public static Assignment getAssignment(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkAssignmentExists(uuid)) {
                sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, assignments.uuid, assignments.unit, assignments.date, assignments.type FROM units INNER JOIN assignments ON units.uuid=unit WHERE assignments.uuid=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();

                if (results.next()) {
                    Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                            results.getString("code"), results.getString("lecturer"),
                            results.getInt("pages"));

                    return new Assignment((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE), results.getInt(COLUMN_TYPE));
                } else {
                    return null;
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
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

    // return all the Assignments in our DB
    public static List<Assignment> getAssignments() {

        assert conn != null;
        String sqlStatement;
        List<Assignment> cats = new ArrayList<>();

        try {

            sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, assignments.uuid, assignments.unit, assignments.date, assignments.type FROM units INNER JOIN assignments ON units.uuid=unit";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            while (results.next()) {
                Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                        results.getString("code"), results.getString("lecturer"),
                        results.getInt("pages"));

                cats.add(new Assignment((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE),
                        results.getInt(COLUMN_TYPE)));
            }

            return cats;

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
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

    // edit a assignment
    public static void editAssignment(Assignment assignment) {

        assert conn != null;
        String sqlStatement;

        try {

            if(checkAssignmentExists(assignment.getUuid())) {
                sqlStatement = String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=?", TABLE_ASSIGNMENTS, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, assignment.getUnitObject().getUuid());
                statement.setDate(2, assignment.getDate());
                statement.setInt(3, assignment.getTypeInt());
                statement.setObject(4, assignment.getUuid());
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

    // delete a assignment
    public static void deleteAssignment(Assignment assignment) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkAssignmentExists(assignment.getUuid())) {
                sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_ASSIGNMENTS, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, assignment.getUuid());
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

    // delete Assignments table
    public static void deleteAll() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DELETE FROM %s WHERE %s=*", TABLE_ASSIGNMENTS, COLUMN_UUID);
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

    // delete Assignments table
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DROP TABLE IF EXISTS %s", TABLE_ASSIGNMENTS);
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
