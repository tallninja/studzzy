package application.models;

import application.controllers.UnitController;

import java.util.UUID;

public class Unit {

    private UUID uuid;
    private String name;
    private String code;
    private String lecturer;
    private int pages;

    public Unit(UUID uuid, String name, String code, String lecturer, int pages) {
        this.setUuid(uuid);
        this.setName(name);
        this.setCode(code);
        this.setLecturer(lecturer);
        this.setPages(pages);
        UnitController.saveUnit(this);
    }

    public Unit(String name, String code, String lecturer, int pages) {
        this.setUuid(UUID.randomUUID());
        this.setName(name);
        this.setCode(code);
        this.setLecturer(lecturer);
        this.setPages(pages);
    }

    public void save() {
        UnitController.saveUnit(this);
    }

    public void update(String name, String code, String lecturer, int pages) {
        this.setUuid(this.getUuid());
        this.setName(name);
        this.setCode(code);
        this.setLecturer(lecturer);
        this.setPages(pages);
        UnitController.editUnit(this);
    }

    public void delete() {
        UnitController.deleteUnit(this);
    }

    @Override
    public String toString() {
        return String.format("Unit{'name': %s, 'code': %s, 'lecturer': %s, 'pages': %d}",
                                this.getName(), this.getCode(), this.getLecturer(), this.getPages());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
