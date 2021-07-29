package application.controllers;

import application.models.Assignment;
import application.models.Database;
import application.models.Unit;
import application.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class AssignmentController {

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkAssignmentExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "SELECT * FROM assignments INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit WHERE assignment_id=?";
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

            sqlStatement = "CREATE TABLE IF NOT EXISTS assignments (id SERIAL UNIQUE, assignment_id UUID primary key, _user UUID references users(user_id), unit UUID references units(uuid), date DATE, type INTEGER)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkAssignmentExists(assignment.getUuid())) {
                sqlStatement = "INSERT INTO assignments (cat_id, _user, unit, date, type) VALUES (?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, assignment.getUuid());
                statement.setObject(2, assignment.getUser().getUserId());
                statement.setObject(3, assignment.getUnitObject().getUuid());
                statement.setDate(4, assignment.getDate());
                statement.setInt(5, assignment.getTypeInt());
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
                sqlStatement = "SELECT * FROM assignments INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.units_id=unit WHERE assignments.assignment_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();

                if (results.next()) {
                    User user = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                            results.getString("registration_number"), results.getString("university"),
                            results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                            results.getString("email"), results.getString("password"));

                    Unit unit = new Unit((UUID) results.getObject("unit"), user, results.getString("name"),
                            results.getString("code"), results.getString("lecturer"),
                            results.getInt("pages"));

                    return new Assignment((UUID) results.getObject("assignment_id"), user, unit, results.getDate("date"), results.getInt("type"));
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

            sqlStatement = "SELECT * FROM assignments INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            while (results.next()) {
                User user = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));

                Unit unit = new Unit((UUID) results.getObject("unit"), user, results.getString("name"),
                        results.getString("code"), results.getString("lecturer"),
                        results.getInt("pages"));

                cats.add(new Assignment((UUID) results.getObject("assignment_id"), user, unit, results.getDate("date"),
                        results.getInt("type")));
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
                sqlStatement = "UPDATE assignments SET unit=?, date=?, %s=? WHERE assignment_id=?";
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
                sqlStatement = "DELETE FROM assignments WHERE assignment_id=?";
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
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "DROP TABLE IF EXISTS assignments";
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
