package application.models;

import application.controllers.UnitController;

import java.util.UUID;

public class Unit {

    private User user;
    private UUID uuid;
    private String name;
    private String code;
    private String lecturer;
    private int pages;

    public Unit(UUID uuid, User user, String name, String code, String lecturer, int pages) {
        this.setUser(user);
        this.setUuid(uuid);
        this.setName(name);
        this.setCode(code);
        this.setLecturer(lecturer);
        this.setPages(pages);
        UnitController.saveUnit(this);
    }

    public Unit(User user, String name, String code, String lecturer, int pages) {
        this.setUuid(UUID.randomUUID());
        this.setUser(user);
        this.setName(name);
        this.setCode(code);
        this.setLecturer(lecturer);
        this.setPages(pages);
    }

    public void save() {
        UnitController.saveUnit(this);
    }

    public void update(String name, String code, String lecturer, int pages) {
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
        return this.getName();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
