package application.controllers;

import application.models.Database;
import application.models.Reminder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReminderController {

    private static final String TABLE_REMINDERS = "reminders";
    private static final String COLUMN_UUID = "uuid";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    // checks if the reminder exists
    private static boolean checkReminderExists(UUID uuid) {

        assert conn != null;

        try {
            String sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_REMINDERS, COLUMN_UUID);
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

    // saves the reminder on our database
    public static void saveReminder(Reminder reminder) {

        assert conn != null;
        String sqlStatement;

        try {
            sqlStatement = String.format("CREATE TABLE IF NOT EXISTS %s (id SERIAL UNIQUE primary key, %s UUID, %s TEXT, %s TEXT)",
                                                 TABLE_REMINDERS, COLUMN_UUID, COLUMN_DATE, COLUMN_DESCRIPTION);
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkReminderExists(reminder.getUuid())) {
                sqlStatement = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)",
                                               TABLE_REMINDERS, COLUMN_UUID, COLUMN_DATE, COLUMN_DESCRIPTION);
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, reminder.getUuid());
                statement.setDate(2, reminder.getDate());
                statement.setString(3, reminder.getDescription());
                statement.executeUpdate();

            }

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
        } finally {
            try{
                if(statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            }
        }

    }

    // get a single reminder
    public static Reminder getReminder(UUID uuid) {

        assert conn != null;
        String sqlStatement;

        try {
            sqlStatement = String.format("SELECT * FROM %s WHERE %s=?", TABLE_REMINDERS, COLUMN_UUID);
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, uuid, Types.OTHER);
            results = statement.executeQuery();

            if (results.next()) {
                return new Reminder((UUID) results.getObject(COLUMN_UUID), results.getString(COLUMN_DESCRIPTION),
                        results.getDate(COLUMN_DATE));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return null;
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

    // get all the reminders
    public static List<Reminder> getReminders() {

        assert conn != null;
        String sqlStatement;
        List<Reminder> reminders = new ArrayList<>();

        try {

            sqlStatement = String.format("SELECT * FROM %s", TABLE_REMINDERS);
            statement = conn.prepareStatement(sqlStatement);
            results = statement.executeQuery();

            while (results.next()) {
                reminders.add(new Reminder((UUID) results.getObject(COLUMN_UUID) , results.getString(COLUMN_DESCRIPTION),
                        results.getDate(COLUMN_DATE)));
            }

            return  reminders;

        } catch (Exception e) {
            System.err.printf("%s: %s", e.getClass().getName(), e.getMessage());
            return reminders;
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

    // edit a reminder
    public static void editReminder(Reminder reminder) {

        assert conn != null;
        String sqlStatement;

        try {

            if (checkReminderExists(reminder.getUuid())) {
                sqlStatement = String.format("UPDATE %s SET %s=?, %s=? WHERE %s=?",
                        TABLE_REMINDERS, COLUMN_DATE, COLUMN_DESCRIPTION, COLUMN_UUID);
                statement = conn.prepareStatement(sqlStatement);
                statement.setDate(1, reminder.getDate());
                statement.setString(2, reminder.getDescription());
                statement.setObject(3, reminder.getUuid());
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

    // delete a reminder
    public static void deleteReminder(Reminder reminder) {

        assert conn != null;
        String sqlStatement;

        try {

            sqlStatement = String.format("DELETE FROM %s WHERE %s=?", TABLE_REMINDERS, COLUMN_UUID);
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, reminder.getUuid());
            statement.executeUpdate();

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

            sqlStatement = String.format("DROP TABLE IF EXISTS %s", TABLE_REMINDERS);
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
