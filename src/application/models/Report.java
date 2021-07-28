package application.models;

import application.controllers.ReportController;

import java.sql.Date;
import java.util.UUID;

public class Report extends  Submission {

    /*
    * 1 -> Group report
    * 2 -> Individual Report
    * */

    private UUID uuid;
    private int type;

    public Report(UUID uuid, Unit unit, Date date, int type) {
        super(unit, date);
        this.setUuid(uuid);
        this.setType(type);
    }

    public Report(Unit unit, Date date, int type) {
        super(unit, date);
        this.setUuid(UUID.randomUUID());
        this.setType(type);
    }

    public void save() {
        ReportController.saveReport(this);
        this.setReminder();
    }

    public void update(Date date, int type) {
        this.setUnit(this.getUnitObject());
        this.setDate(date);
        this.setType(type);
        ReportController.editReport(this);
    }

    public void delete() {
        ReportController.deleteReport(this);
    }

    @Override
    public void  setReminder() {
        String description = String.format("%s REPORT", this.getUnitObject().getName());
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
