package application.models;

import application.controllers.CatController;

import java.sql.Date;
import java.util.UUID;

public class Cat extends Test {

    private UUID uuid;
    private int type;

    public Cat(UUID uuid, User user, Unit unit, Date date, int type) {
        super(user, unit, date);
        this.setUuid(uuid);
        this.setType(type);
    }

    public Cat(User user, Unit unit, Date date, int type) {
        super(user, unit, date);
        this.setUuid(UUID.randomUUID());
        this.setType(type);
    }

    public void save() {
        CatController.saveCat(this);
        this.setReminder();
    }

    public void update(Date date, int type) {
        this.setUnit(this.getUnitObject());
        this.setDate(date);
        this.setType(type);
        CatController.editCat(this);
    }

    public void delete() {
        CatController.deleteCat(this);
    }

    public String getType() {
        return switch (this.getTypeInt()) {
            case 1 -> "Sitting";
            case 2 -> "Take Away";
            default -> "invalid-type";
        };
    }

    public void setTypeFromString(String type) {
        switch (type) {
            case "Sitting" -> this.setType(1);
            case "Take Away" -> this.setType(2);
            default -> this.setType(1);
        }
    }

    @Override
    public void  setReminder() {
        String description = String.format("%s CAT", this.getUnitObject().getName());
        Reminder reminder = new Reminder(this.getUser(), description, this.getDate());
        reminder.save();
    }

    @Override
    public String toString() {
        return String.format("Cat{'unit': %s, 'date': %s, 'type': %s}",
                                this.getUnitObject(), this.getDate(), this.getType());
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

    public int getTypeInt() {
        return this.type;
    }

}
