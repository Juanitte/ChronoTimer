package com.juanite.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String regex = "^([0-9][0-9]):([0-5][0-9]):([0-5][0-9])$";
    private static final Pattern pattern = Pattern.compile(regex);

    public static boolean isValid(String string) {
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
