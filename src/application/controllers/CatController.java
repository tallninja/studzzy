package application.controllers;

import application.models.Cat;
import application.models.Database;
import application.models.Unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Universally Unique ID

public class CatController {

    private static final String TABLE_UNITS="units";
    private static final String TABLE_CATS = "cats";
    private static final String COLUMN_UUID = "uuid";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    private static boolean checkCatExists(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_CATS, COLUMN_UUID);
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
    public static void saveCat(Cat cat) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE, " +
                                        "%s UUID primary key, %s UUID references units(uuid), %s DATE, %s INTEGER)",
                                        TABLE_CATS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkCatExists(cat.getUuid())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                                            TABLE_CATS, COLUMN_UUID, COLUMN_UNIT, COLUMN_DATE, COLUMN_TYPE);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, cat.getUuid());
                statement.setObject(2, cat.getUnit().getUuid());
                statement.setDate(3, cat.getDate());
                statement.setInt(4, cat.getType());
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
    public static Cat getCat(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkCatExists(uuid)) {
                sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, cats.uuid, cats.unit, cats.date, cats.type FROM units INNER JOIN cats ON units.uuid=unit WHERE cats.uuid=?";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, uuid);
                results = statement.executeQuery();

                if (results.next()) {
                        Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                                        results.getString("code"), results.getString("lecturer"),
                                        results.getInt("pages"));

                    return new Cat((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE), results.getInt(COLUMN_TYPE));
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
    public static List<Cat> getCats() {

        assert conn != null;
        String sqlStatement;
        List<Cat> cats = new ArrayList<>();

        try {

            sqlStatement = "SELECT units.name, units.code, units.lecturer, units.pages, cats.uuid, cats.unit, cats.date, cats.type FROM units INNER JOIN cats ON units.uuid=unit";
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            while (results.next()) {
                Unit unit = new Unit((UUID) results.getObject(COLUMN_UNIT), results.getString("name"),
                        results.getString("code"), results.getString("lecturer"),
                        results.getInt("pages"));

                cats.add(new Cat((UUID) results.getObject(COLUMN_UUID), unit, results.getDate(COLUMN_DATE),
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

    // edit a cat
    public static void editCat(Cat cat) {

        assert conn != null;
        String sqlStatement;

        try {

            if(checkCatExists(cat.getUuid())) {
                sqlStatement = String.format("UPDATE %s SET %s=?, %s=? WHERE %s=?", TABLE_CATS, COLUMN_DATE, COLUMN_TYPE, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setDate(1, cat.getDate());
                statement.setInt(2, cat.getType());
                statement.setObject(3, cat.getUuid());
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

            if (checkCatExists(cat.getUuid())) {
                sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_CATS, COLUMN_UUID);
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
    public static void deleteAll() {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DELETE FROM %s WHERE %s=*", TABLE_CATS, COLUMN_UUID);
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

            sqlStatement = String.format("DROP TABLE IF EXISTS %s", TABLE_CATS);
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
