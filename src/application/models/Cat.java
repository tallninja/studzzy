package application.models;

import application.controllers.CatController;

import java.sql.Date;
import java.util.UUID;

public class Cat extends Test {

    private UUID uuid;
    private int type;

    public Cat(UUID uuid, Unit unit, Date date, int type) {
        super(unit, date);
        this.setUuid(uuid);
        this.setType(type);
    }

    public Cat(Unit unit, Date date, int type) {
        super(unit, date);
        this.setUuid(UUID.randomUUID());
        this.setType(type);
    }

    public void save() {
        CatController.saveCat(this);
        this.setReminder();
    }

    public void update(Date date, int type) {
        this.setUnit(this.getUnit());
        this.setDate(date);
        this.setType(type);
        CatController.editCat(this);
    }

    public void delete() {
        CatController.deleteCat(this);
    }

    public String getTypeToString() {
        return switch (this.getType()) {
            case 1 -> "sitting";
            case 2 -> "take-away";
            default -> "invalid-type";
        };
    }

    public void setTypeFromString(String type) {
        switch (type) {
            case "sitting" -> this.setType(1);
            case "take-away" -> this.setType(2);
            default -> this.setType(1);
        }
    }

    @Override
    public void  setReminder() {
        String description = String.format("%s CAT", this.getUnitObject().getName());
        Reminder reminder = new Reminder(description, this.getDate());
        reminder.save();
    }

    @Override
    public String toString() {
        return String.format("Cat{'unit': %s, 'date': %s, 'type': %s}",
                                this.getUnitObject(), this.getDate(), this.getTypeToString());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
