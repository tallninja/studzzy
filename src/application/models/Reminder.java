package application.models;

import application.controllers.ReminderController;

import java.sql.Date;
import java.util.UUID;

public class Reminder {

    private UUID uuid;
    private User user;
    private String description;
    private Date date;

    public Reminder(UUID uuid, User user, String description, Date reminderDate) {
        this.setUser(user);
        this.setUuid(uuid);
        this.setDescription(description);
        this.setDate(reminderDate);
    }

    public Reminder(User user, String description, Date reminderDate) {
        this.setUuid(UUID.randomUUID());
        this.setUser(user);
        this.setDescription(description);
        this.setDate(reminderDate);
    }

    public void save() {
        ReminderController.saveReminder(this);
    }

    public void update(String description, Date reminderDate) {
        this.setDescription(description);
        this.setDate(reminderDate);
        ReminderController.editReminder(this);
    }

    public void delete() {
        ReminderController.deleteReminder(this);
    }

    @Override
    public String toString() {
        return String.format("Reminder{'date': %s, 'description': %s}", this.getDate(), this.getDescription());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
