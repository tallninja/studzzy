package application.models;

import application.controllers.UnitController;

import java.sql.Date;
import java.util.UUID;

public class Test implements Remindable {

    private User user;
    private Unit unit;
    private Date date;

    public Test(User user, Unit unit, Date date) {
        this.setUser(user);
        this.setUnit(unit);
        this.setDate(date);
    }



    public Unit getUnitObject() {
        return UnitController.getUnit(unit.getUuid(), unit.getUser());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUnit() {
        return this.unit.getName();
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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
