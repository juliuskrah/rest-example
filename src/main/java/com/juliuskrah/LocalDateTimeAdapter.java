package com.juliuskrah;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String dateInput) {
        return LocalDateTime.parse(dateInput, DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public String marshal(LocalDateTime localDateTime) {
        return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);
    }
}