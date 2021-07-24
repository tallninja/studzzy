package application.models;

import application.controllers.UnitController;

import java.sql.Date;
import java.util.UUID;

public class Test implements Remindable {

    private UUID unit;
    private Date date;

    public Test(UUID unit, Date date) {
        this.setUnit(unit);
        this.setDate(date);
    }

    @Override
    public void setReminder(String description) {
        Reminder.setReminder(description, this.getDate());
    }

    public Unit getUnitObject() {
        return UnitController.getUnit(unit);
    }

    public UUID getUnit() {
        return this.unit;
    }

    public void setUnit(UUID unit) {
        this.unit = unit;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
}
