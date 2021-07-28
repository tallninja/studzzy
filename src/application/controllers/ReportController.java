package application.controllers;

import application.models.Report;
import application.models.Database;
import application.models.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class ReportController {

    private static final String TABLE_REPORTS = "reports";
    private static final String COLUMN_UUID = "uuid";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkReportExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_REPORTS, COLUMN_UUID);
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

            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE, " +
                            "%s UUID primary key, %s UUID references units(uuid), %s DATE, %s INTEGER)",
                    TABLE_REPORTS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkReportExists(report.getUuid())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                        TABLE_REPORTS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, report.getUuid());
                statement.setObject(2, report.getUnitObject().getUuid());
                statement.setDate(3, report.getDate());
                statement.setInt(4, report.getTypeInt());
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
                sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, reports.uuid, reports.unit, reports.date, reports.type FROM units INNER JOIN reports ON units.uuid=unit WHERE reports.uuid=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();

                if (results.next()) {
                    Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                            results.getString("code"), results.getString("lecturer"),
                            results.getInt("pages"));

                    return new Report((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE), results.getInt(COLUMN_TYPE));
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

            sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, reports.uuid, reports.unit, reports.date, reports.type FROM units INNER JOIN reports ON units.uuid=unit";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            while (results.next()) {
                Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                        results.getString("code"), results.getString("lecturer"),
                        results.getInt("pages"));

                reports.add(new Report((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE),
                        results.getInt(COLUMN_TYPE)));
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
                sqlStatement = String.format("UPDATE %s SET %s=?, %s=? WHERE %s=?", TABLE_REPORTS, COLUMN_DATE, COLUMN_TYPE, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setDate(1, report.getDate());
                statement.setInt(2, report.getTypeInt());
                statement.setObject(3, report.getUuid());
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
                sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_REPORTS, COLUMN_UUID);
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
    public static void deleteAll() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DELETE FROM %s WHERE %s=*", TABLE_REPORTS, COLUMN_UUID);
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

    // delete Reports table
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DROP TABLE IF EXISTS %s", TABLE_REPORTS);
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
