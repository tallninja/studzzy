package application.controllers;

import java.util.List;

public interface Crudable<T> {
    boolean checkObjectExists(T object);
    void save(T object);
    T get(T object);
    List<T> getAll();
    void update(T object);
    void delete(T object);
}
