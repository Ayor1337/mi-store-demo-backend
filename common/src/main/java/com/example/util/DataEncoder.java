package com.example.util;

public class DataEncoder {


    public static String getAnonymousEmail(String email) {
        if (email == null || email.isEmpty()) {
            return null;
        }

        String top = email.substring(email.indexOf(".") + 1);
        String domain = email.substring(email.indexOf("@") + 1, email.indexOf("."));
        String username = email.substring(0, email.indexOf("@"));
        StringBuilder anonymousEmail = new StringBuilder();
        anonymousEmail
                .append(username, 0, 2)
                .append("*".repeat(Math.max(0, username.length() - 4)))
                .append(username, username.length() - 2, username.length())
                .append('@')
                .append(domain.charAt(0))
                .append("*".repeat(domain.length() - 1))
                .append('.')
                .append(top);
        return anonymousEmail.toString();
    }

    public static String getAnonymousPhone(String phone) {
        if (phone == null || phone.isEmpty())
            return null;

        return phone.substring(0, 2) + "*******" + phone.substring(9, 11);
    }
}
