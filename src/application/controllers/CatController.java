package application.controllers;

import application.models.Cat;
import application.models.Database;
import application.models.Unit;
import application.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class CatController {

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkCatExists(UUID uuid, User user) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "SELECT * FROM cats INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit WHERE ( users.user_id=? AND cat_id=? )";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, user.getUserId());
            statement.setObject(2, uuid);
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
    public static void saveCat(Cat cat) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "CREATE TABLE IF NOT EXISTS cats (id SERIAL UNIQUE, cat_id UUID primary key, _user UUID references users(user_id), unit UUID references units(unit_id), date DATE, type INTEGER)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkCatExists(cat.getUuid(), cat.getUser())) {
                sqlStatement = "INSERT INTO cats (cat_id, _user, unit, date, type) VALUES (?, ?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, cat.getUuid());
                statement.setObject(2, cat.getUser().getUserId());
                statement.setObject(3, cat.getUnitObject().getUuid());
                statement.setDate(4, cat.getDate());
                statement.setInt(5, cat.getTypeInt());
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
    public static Cat getCat(UUID uuid, User user) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(uuid, user)) {
                sqlStatement = "SELECT * FROM cats INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit WHERE ( users.user_id=? AND cats.cat_id=? )";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, user.getUserId());
                statement.setObject(2, uuid);
                results = statement.executeQuery();

                if (results.next()) {
                    User fetchedUser = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                            results.getString("registration_number"), results.getString("university"),
                            results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                            results.getString("email"), results.getString("password"));

                    Unit unit = new Unit((UUID) results.getObject("unit"), fetchedUser, results.getString("name"),
                                    results.getString("code"), results.getString("lecturer"),
                                    results.getInt("pages"));

                    return new Cat((UUID) results.getObject("cat_id"), fetchedUser, unit, results.getDate("date"), results.getInt("type"));
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
    public static List<Cat> getCats(User user) {

        assert conn != null;
        String sqlStatement;
        List<Cat> cats = new ArrayList<>();

        try {

            sqlStatement = "SELECT * FROM cats INNER JOIN users ON users.user_id=_user INNER JOIN units ON units.unit_id=unit WHERE users.user_id=?";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, user.getUserId());
            results = statement.executeQuery();

            while (results.next()) {
                User fetchedUser = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));

                Unit unit = new Unit((UUID) results.getObject("unit"), fetchedUser, results.getString("name"),
                        results.getString("code"), results.getString("lecturer"),
                        results.getInt("pages"));

                cats.add(new Cat((UUID) results.getObject("cat_id"), fetchedUser, unit, results.getDate("date"),
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

    // edit a cat
    public static void editCat(Cat cat) {

        assert conn != null;
        String sqlStatement;

        try {

            if(checkCatExists(cat.getUuid(), cat.getUser())) {
                sqlStatement = "UPDATE cats SET date=?, unit=?, type=? WHERE cat_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setDate(1, cat.getDate());
                statement.setObject(2, cat.getUnitObject().getUuid());
                statement.setInt(3, cat.getTypeInt());
                statement.setObject(4, cat.getUuid());
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
    public static void deleteCat(Cat cat) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(cat.getUuid(), cat.getUser())) {
                sqlStatement = "DELETE FROM cats WHERE cat_id=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, cat.getUuid());
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
    public static void dropTable() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = "DROP TABLE IF EXISTS cats";
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
