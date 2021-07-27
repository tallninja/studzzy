package application.models;

import application.controllers.UnitController;

import java.sql.Date;
import java.util.UUID;

public class Test implements Remindable {

    private Unit unit;
    private Date date;

    public Test(Unit unit, Date date) {
        this.setUnit(unit);
        this.setDate(date);
    }



    public Unit getUnitObject() {
        return UnitController.getUnit(unit.getUuid());
    }

    public Unit getUnit() {
        return this.unit;
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
