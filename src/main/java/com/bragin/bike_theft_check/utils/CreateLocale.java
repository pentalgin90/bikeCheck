package com.bragin.bike_theft_check.utils;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class CreateLocale {
    public static Locale getLocale(String languageCode) {
        return switch (languageCode) {
            case "ru" -> Locale.forLanguageTag(languageCode);
            default -> Locale.ENGLISH;
        };
    }
}
