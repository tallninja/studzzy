package application.controllers;

import application.models.Database;
import application.models.Reminder;
import application.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReminderController {

    private static final Connection conn = Database.getConn();
    private static PreparedStatement statement = null;
    private static ResultSet results = null;

    // checks if the reminder exists
    private static boolean checkReminderExists(UUID uuid, User user) {

        assert conn != null;

        try {
            String sqlStatement = "SELECT * FROM reminders INNER JOIN users ON users.user_id=_user WHERE ( users.user_id=? AND reminder_id=? )";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, user.getUserId());
            statement.setObject(2, uuid);
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
            sqlStatement = "CREATE TABLE IF NOT EXISTS reminders (id SERIAL UNIQUE, reminder_id UUID primary key, _user UUID references users(user_id), date DATE, description TEXT)";
            statement = conn.prepareStatement(sqlStatement);
            statement.execute();

            if (!checkReminderExists(reminder.getUuid(), reminder.getUser())) {
                sqlStatement = "INSERT INTO reminders (reminder_id, _user, date, description) VALUES (?, ?, ?, ?)";
                statement = conn.prepareStatement(sqlStatement);
                statement.setObject(1, reminder.getUuid());
                statement.setObject(2, reminder.getUser().getUserId());
                statement.setDate(3, reminder.getDate());
                statement.setString(4, reminder.getDescription());
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
    public static Reminder getReminder(UUID uuid, User user) {

        assert conn != null;
        String sqlStatement;

        try {
            sqlStatement = "SELECT * FROM reminders INNER JOIN users ON users.user_id=_user WHERE ( users.user_id=? AND reminder_id=? )";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, user.getUserId());
            statement.setObject(2, uuid, Types.OTHER);
            results = statement.executeQuery();

            if (results.next()) {
                User fetchedUser = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));

                return new Reminder((UUID) results.getObject("reminder_id"), fetchedUser, results.getString("description"),
                        results.getDate("date"));
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
    public static List<Reminder> getReminders(User user) {

        assert conn != null;
        String sqlStatement;
        List<Reminder> reminders = new ArrayList<>();

        try {

            sqlStatement = "SELECT * FROM reminders INNER JOIN users ON users.user_id=_user WHERE users.user_id=?";
            statement = conn.prepareStatement(sqlStatement);
            statement.setObject(1, user.getUserId());
            results = statement.executeQuery();

            while (results.next()) {
                User fetchedUser = new User((UUID) results.getObject("user_id"), results.getString("first_name"), results.getString("last_name"),
                        results.getString("registration_number"), results.getString("university"),
                        results.getDate("start_sem_date"), results.getDate("end_sem_date"),
                        results.getString("email"), results.getString("password"));

                reminders.add(new Reminder((UUID) results.getObject("reminder_id"), fetchedUser, results.getString("description"),
                        results.getDate("date")));
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

            if (checkReminderExists(reminder.getUuid(), reminder.getUser())) {
                sqlStatement = "UPDATE reminders SET date=?, description=? WHERE reminder_id=?";
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

            sqlStatement = "DELETE FROM reminders WHERE reminder_id=?";
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

            sqlStatement = "DROP TABLE IF EXISTS reminders";
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
