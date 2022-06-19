package com.example.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    public static boolean   isDigit(String  id){
        int length = id.length();
        for(int i=0;i<length;++i){
            if(id.charAt(i)<'0'||id.charAt(i)>'9'){
                return false;
            }
        }
        return true;
    }
}

