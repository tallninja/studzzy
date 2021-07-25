package application.controllers.utils;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateStringConverter extends StringConverter<LocalDate> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private boolean hasParseError;

    public boolean hasParseError() {
        return hasParseError;
    }

    @Override
    public String toString(LocalDate localDate) {
        return DATE_FORMATTER.format(localDate);
    }

    @Override
    public LocalDate fromString(String dateFormatString) {
        try {
            LocalDate date=LocalDate.from(DATE_FORMATTER.parse(dateFormatString));
            hasParseError=false;
            return date;
        } catch (DateTimeParseException parseExc){
            hasParseError=true;
            return null;
        }
    }

    public long toMills(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date.getTime();
    }
}
