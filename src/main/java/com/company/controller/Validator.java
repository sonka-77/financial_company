package com.company.controller;

import java.util.regex.Pattern;

//класс для валидации данных
public class Validator {

    // пароль: минимум 8 символов, 1 цифра, 1 заглавная буква, 1 спецсимвол
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");

    private Validator() {}


     //проверка сложности пароля

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }


    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static String getPasswordRequirements() {
        return "Пароль должен содержать:\n" +
               "• Минимум 8 символов\n" +
               "• Хотя бы одну цифру\n" +
               "• Хотя бы одну заглавную букву\n" +
               "• Хотя бы один специальный символ (!@#$%^&* и т.д.)";
    }
}
