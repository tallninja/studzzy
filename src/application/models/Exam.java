package application.models;

import application.controllers.ExamController;

import java.sql.Date;
import java.util.UUID;

public class Exam extends Test {

    private UUID uuid;
    private User user;

    public Exam(UUID uuid, User user, Unit unit, Date date) {
        super(user, unit, date);
        this.setUuid(uuid);
    }

    public Exam(User user, Unit unit, Date date) {
        super(user, unit, date);
        this.setUuid(UUID.randomUUID());
    }

    public void save() {
        ExamController.saveExam(this);
        this.setReminder();
    }

    public void update(Date date) {
        this.setUnit(this.getUnitObject());
        this.setDate(date);
        ExamController.editExam(this);
    }

    public void delete() {
        ExamController.deleteExam(this);
    }

    @Override
    public void  setReminder() {
        String description = String.format("%s EXAM", this.getUnitObject().getName());
        Reminder reminder = new Reminder(this.getUser(), description, this.getDate());
        reminder.save();
    }

    @Override
    public String toString() {
        return String.format("Exam{'unit': %s, 'date': %s}",
                this.getUnitObject(), this.getDate());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

}
