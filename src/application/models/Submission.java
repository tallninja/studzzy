package application.models;

import application.controllers.ReminderController;
import application.controllers.UnitController;

import java.sql.Date;
import java.util.UUID;

public class Submission implements Remindable {

    private UUID unit;
    private Date date;

    public Submission(UUID unit, Date date) {
        this.setUnit(unit);
        this.setDate(date);
    }

    public UUID getUnit() {
        return unit;
    }

    public void setUnit(UUID unit) {
        this.unit = unit;
    }

    public Unit getUnitObject() {
        return UnitController.getUnit(unit);
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setReminder() {

    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
}
