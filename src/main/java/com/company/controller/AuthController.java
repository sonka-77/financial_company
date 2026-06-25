package com.company.controller;

import com.company.db.UserDAO;
import com.company.model.User;

import java.sql.SQLException;
import java.util.List;


  //controller для авторизации, регистрации и управления пользователями.

public class AuthController {

    private UserDAO userDAO;

    public AuthController() {
        try {
            this.userDAO = new UserDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД: " + e.getMessage());
        }
    }

    //авторизация возвращает юзера со всеми данными
    public User login(String login, String password) {
        try {
            return userDAO.login(login, password);
        } catch (SQLException e) {
            System.err.println("Ошибка авторизации: " + e.getMessage());
            return null;
        }
    }

    //регистрация
    public String register(String login, String password) {
        try {
            if (userDAO.loginExists(login)) {
                return "Пользователь с таким логином уже существует.";
            }
            userDAO.register(login, password);
            return null;
        } catch (SQLException e) {
            return "Ошибка при регистрации: " + e.getMessage();
        }
    }

    /** Обновить логин и пароль пользователя. */
    public boolean updateUser(int userId, String newLogin, String newPassword) throws Exception {
        return userDAO.updateUser(userId, newLogin, newPassword);
    }

    /** Получить список всех пользователей. */
    public List<User> getAllUsers() throws Exception {
        return userDAO.getAllUsers();
    }

    /** Удалить пользователя по id. */
    public boolean deleteUser(int userId) throws Exception {
        return userDAO.deleteUser(userId);
    }

    /** Назначить пользователю предприятие (только одно, для роли user). */
    public boolean assignEnterprise(int userId, int enterpriseId) throws Exception {
        return userDAO.assignEnterprise(userId, enterpriseId);
    }

    /**
     * Сбросить предприятие пользователя.
     * При следующем входе ему снова покажется экран выбора предприятия.
     */
    public boolean resetEnterprise(int userId) throws Exception {
        return userDAO.resetEnterprise(userId);
    }

}
