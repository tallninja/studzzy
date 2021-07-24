package application.models;

import application.controllers.ReminderController;

import java.sql.Date;
import java.util.UUID;

public class Reminder {

    private UUID uuid;
    private String description;
    private Date date;

    public Reminder(UUID uuid, String description, Date reminderDate) {
        this.setUuid(uuid);
        this.setDescription(description);
        this.setDate(reminderDate);
    }

    public Reminder(String description, Date reminderDate) {
        this.setUuid(UUID.randomUUID());
        this.setDescription(description);
        this.setDate(reminderDate);
        ReminderController.saveReminder(this);
    }

    public void update(String description, Date reminderDate) {
        this.setDescription(description);
        this.setDate(reminderDate);
        ReminderController.editReminder(this);
    }

    public static void setReminder(String description, Date date) {
        ReminderController.saveReminder(new Reminder(UUID.randomUUID(), description, date));
    }

    public void delete() {
        ReminderController.deleteReminder(this);
    }

    @Override
    public String toString() {
        return String.format("Reminder{'date': %s, 'description': %s}", this.getDate(), this.getDescription());
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
