package application.controllers;

import application.models.Report;
import application.models.Database;
import application.models.Unit;
import application.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class ReportController {

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkReportExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "SELECT * FROM users INNER JOIN reports ON users.user_id=_user WHERE report_id=?";
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

    // save a Report to our DB
    public static void saveReport(Report report) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "CREATE TABLE IF NOT EXISTS reports (id SERIAL UNIQUE, report_id UUID primary key, _user UUID references users(user_id), unit UUID references units(unit_id), date DATE, type INTEGER)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkReportExists(report.getUuid())) {
                sqlStatement = "INSERT INTO reports (report_id, _user, unit, date, type) VALUES (?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, report.getUuid());
                statement.setObject(2, report.getUser().getUserId());
                statement.setObject(3, report.getUnitObject().getUuid());
                statement.setDate(4, report.getDate());
                statement.setInt(5, report.getTypeInt());
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

    // get a single Report
    public static Report getReport(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkReportExists(uuid)) {
                sqlStatement = "SELECT * FROM reports INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit WHERE reports.report_id=?";
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

                    return new Report((UUID) results.getObject("report_id"), user, unit, results.getDate("date"), results.getInt("type"));
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

    // return all the Reports in our DB
    public static List<Report> getReports() {

        assert conn != null;
        String sqlStatement;
        List<Report> reports = new ArrayList<>();

        try {

            sqlStatement = "SELECT * FROM reports INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit";
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

                reports.add(new Report((UUID) results.getObject("report_id"), user, unit, results.getDate("date"),
                        results.getInt("type")));
            }

            return reports;

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

    // edit a report
    public static void editReport(Report report) {

        assert conn != null;
        String sqlStatement;

        try {

            if(checkReportExists(report.getUuid())) {
                sqlStatement = "UPDATE reports SET  unit=?, date=?, type=? WHERE report_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, report.getUnitObject().getUuid());
                statement.setDate(2, report.getDate());
                statement.setInt(3, report.getTypeInt());
                statement.setObject(4, report.getUuid());
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

    // delete a report
    public static void deleteReport(Report report) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkReportExists(report.getUuid())) {
                sqlStatement = "DELETE FROM exams WHERE exam_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, report.getUuid());
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


    // delete Reports table
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "DROP TABLE IF EXISTS reports";
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
