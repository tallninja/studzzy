package application.models;

import application.controllers.ReminderController;
import application.controllers.UnitController;

import java.sql.Date;
import java.util.UUID;

public class Submission implements Remindable {

    private Unit unit;
    private Date date;

    public Submission(Unit unit, Date date) {
        this.setUnit(unit);
        this.setDate(date);
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnitObject() {
        return UnitController.getUnit(unit.getUuid());
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
