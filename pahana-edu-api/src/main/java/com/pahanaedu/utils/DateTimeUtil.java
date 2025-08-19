package com.pahanaedu.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


public final class DateTimeUtil {

    private static final DateTimeFormatter MEDIUM_SHORT_FORMATTER =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);

    private DateTimeUtil() {}

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(MEDIUM_SHORT_FORMATTER);
    }

}