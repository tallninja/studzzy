package application.models;

import java.sql.Date;

public interface Remindable {
    void setDate(Date date);
    Date getDate();
    void setReminder(String description);
}
