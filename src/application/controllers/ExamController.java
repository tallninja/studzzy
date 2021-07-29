package application.controllers;

import application.models.Database;
import application.models.Exam;
import application.models.Unit;
import application.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class ExamController {

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkCatExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "SELECT * FROM exams INNER JOIN users ON users.user_id=_user WHERE exam_id=?";
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

    // save a exams to our DB
    public static void saveExam(Exam exam) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "CREATE TABLE IF NOT EXISTS exams (id SERIAL UNIQUE, exam_id UUID primary key, _user UUID references users(user_id), unit UUID references units(unit_id), date DATE)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkCatExists(exam.getUuid())) {
                sqlStatement = "INSERT INTO exams (exam_id, _user, unit, date) VALUES (?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, exam.getUuid());
                statement.setObject(2, exam.getUser().getUserId());
                statement.setObject(3, exam.getUnitObject().getUuid());
                statement.setDate(4, exam.getDate());
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

    // get a single exam
    public static Exam getExam(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(uuid)) {
                sqlStatement = "SELECT * FROM exams INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit WHERE exams.exam_id=?";
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

                    return new Exam((UUID) results.getObject("exam_id"), user, unit, results.getDate("date"));
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

    // return all the exams in our DB
    public static List<Exam> getExams() {

        assert conn != null;
        String sqlStatement;
        List<Exam> cats = new ArrayList<>();

        try {

            sqlStatement = "SELECT * FROM exams INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit";
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

                cats.add(new Exam((UUID) results.getObject("exam_id"), user, unit,
                                    results.getDate("date")));
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

    // edit an exam
    public static void editExam(Exam exam) {

        assert conn != null;
        String sqlStatement;

        try {

            if(checkCatExists(exam.getUuid())) {
                sqlStatement = "UPDATE exams SET date=?, unit=? WHERE exam_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setDate(1, exam.getDate());
                statement.setObject(2, exam.getUnitObject().getUuid());
                statement.setObject(3, exam.getUuid());
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

    // delete an exam
    public static void deleteExam(Exam exam) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(exam.getUuid())) {
                sqlStatement = "DELETE FROM exams WHERE exam_id=?";
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


    // delete exams table
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "DROP TABLE IF EXISTS exams";
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
