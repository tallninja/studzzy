package application.models;

import application.controllers.ReminderController;
import application.controllers.UnitController;

import java.sql.Date;
import java.util.UUID;

public class Submission implements Remindable {

    private User user;
    private Unit unit;
    private Date date;

    public Submission(User user, Unit unit, Date date) {
        this.setUser(user);
        this.setUnit(unit);
        this.setDate(date);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUnit() {
        return unit.getName();
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
