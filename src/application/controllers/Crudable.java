package application.controllers;

import java.util.List;

public interface Crudable {
    boolean checkObjectExists(Class type);
    void save(Object object);
    Object get(Object objects);
    List<Object> getAll();
    void update(Object object);
    void delete(Object object);
}
