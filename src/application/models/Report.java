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

    public Report(UUID uuid, UUID unit, Date date, int type) {
        super(unit, date);
        this.setUuid(uuid);
        this.setType(type);
    }

    public Report(UUID unit, Date date, int type) {
        super(unit, date);
        this.setUuid(UUID.randomUUID());
        this.setType(type);
        ReportController.saveReport(this);
    }

    public void update(Date date, int type) {
        this.setUnit(this.getUnit());
        this.setDate(date);
        this.setType(type);
        ReportController.editReport(this);
    }

    public void delete() {
        ReportController.deleteReport(this);
    }

    @Override
    public String toString() {
        return String.format("Report{'unit': %s, 'date': %s, 'type': %s}",
                this.getUnitObject(), this.getDate(), this.getTypeToString());
    }

    public String getTypeToString() {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
