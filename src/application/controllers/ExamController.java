package application.controllers;

import application.models.Database;
import application.models.Exam;
import application.models.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class ExamController {

    private static final String TABLE_EXAMS = "exams";
    private static final String COLUMN_UUID = "uuid";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_DATE = "date";

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkCatExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_EXAMS, COLUMN_UUID);
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

    // save a cat to our DB
    public static void saveExam(Exam exam) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE, " +
                            "%s UUID primary key, %s UUID references units(uuid), %s DATE)",
                    TABLE_EXAMS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkCatExists(exam.getUuid())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                        TABLE_EXAMS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, exam.getUuid());
                statement.setObject(2, exam.getUnit().getUuid());
                statement.setDate(3, exam.getDate());
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

    // get a single cat
    public static Exam getExam(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(uuid)) {
                sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, exams.uuid, exams.unit, exams.date FROM units INNER JOIN exams ON units.uuid=unit WHERE exams.uuid=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();

                if (results.next()) {
                    Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                            results.getString("code"), results.getString("lecturer"),
                            results.getInt("pages"));

                    return new Exam((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE));
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

    // return all the cats in our DB
    public static List<Exam> getExams() {

        assert conn != null;
        String sqlStatement;
        List<Exam> cats = new ArrayList<>();

        try {

            sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, exams.uuid, exams.unit, exams.date FROM units INNER JOIN exams ON units.uuid=unit";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            while (results.next()) {

                Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                        results.getString("code"), results.getString("lecturer"),
                        results.getInt("pages"));

                cats.add(new Exam((UUID) results.getObject(COLUMN_UUID), unit,
                                    results.getDate(COLUMN_DATE)));
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

    // edit a cat
    public static void editExam(Exam exam) {

        assert conn != null;
        String sqlStatement;

        try {

            if(checkCatExists(exam.getUuid())) {
                sqlStatement = String.format("UPDATE %s SET %s=? WHERE %s=?", TABLE_EXAMS, COLUMN_DATE, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setDate(1, exam.getDate());
                statement.setObject(2, exam.getUuid());
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

    // delete a cat
    public static void deleteExam(Exam exam) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(exam.getUuid())) {
                sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_EXAMS, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, exam.getUuid());
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

    // delete CATs table
    public static void deleteAll() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DELETE FROM %s WHERE %s=*", TABLE_EXAMS, COLUMN_UUID);
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

    // delete CATs table
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DROP TABLE IF EXISTS %s", TABLE_EXAMS);
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
