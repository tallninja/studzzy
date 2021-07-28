package application.models;

import application.controllers.AssignmentController;
import application.controllers.ReportController;

import java.sql.Date;
import java.util.UUID;

public class Assignment extends Submission {

    /*
     * 1 -> Group assignment
     * 2 -> Individual individual assignment
     * */

    private UUID uuid;
    private int type;

    public Assignment(UUID uuid, Unit unit, Date date, int type) {
        super(unit, date);
        this.setUuid(uuid);
        this.setType(type);
    }

    public Assignment(Unit unit, Date date, int type) {
        super(unit, date);
        this.setUuid(UUID.randomUUID());
        this.setType(type);
    }

    public void save() {
        AssignmentController.saveAssignment(this);
        this.setReminder();
    }

    public void update(Date date, int type) {
        this.setUnit(this.getUnitObject());
        this.setDate(date);
        this.setType(type);
        AssignmentController.editAssignment(this);
    }

    public void delete() {
        AssignmentController.deleteAssignment(this);
    }

    @Override
    public void  setReminder() {
        String description = String.format("%s ASSIGNMENT", this.getUnitObject().getName());
        Reminder reminder = new Reminder(description, this.getDate());
        reminder.save();
    }

    @Override
    public String toString() {
        return String.format("Report{'unit': %s, 'date': %s, 'type': %s}",
                this.getUnitObject(), this.getDate(), this.getType());
    }

    public String getType() {
        return switch (this.type) {
            case 1 -> "group";
            case 2 -> "individual";
            default -> "invalid-type";
        };
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getTypeInt() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
