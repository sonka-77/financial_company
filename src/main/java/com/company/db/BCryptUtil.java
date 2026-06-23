package com.company.db;

import org.mindrot.jbcrypt.BCrypt;


public class BCryptUtil {

    private BCryptUtil() {}

    //хеширование пароля перед сохранением в бд
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    //проверка введеного пароля на хеш из бд
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
