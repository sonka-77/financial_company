package com.company.db;

import com.company.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для таблицы users_roles_enterprises.
 * Реальная структура БД: одна таблица хранит id_user, id_enterprise, id_role, login, password.
 */
public class UserDAO {

    private final Connection connection;

    public UserDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    //авторизация по логину и BCrypt-паролю
    public User login(String login, String password) throws SQLException {
        String sql = "SELECT u.id_user, u.login, u.password, u.id_enterprise, r.access_level " +
                     "FROM users_roles_enterprises u " +
                     "JOIN roles r ON u.id_role = r.id_role " +
                     "WHERE u.login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (BCryptUtil.checkPassword(password, storedHash)) {
                    int entId = rs.getInt("id_enterprise");
                    Integer enterpriseId = rs.wasNull() ? null : entId;
                    return new User(
                        rs.getInt("id_user"),
                        rs.getString("login"),
                        storedHash,
                        rs.getString("access_level"),
                        enterpriseId
                    );
                }
            }
        }
        return null;
    }

    // регистрация нового пользователя с ролью user (id_role=2), без предприятия
    public boolean register(String login, String password) throws SQLException {
        String hashedPassword = BCryptUtil.hashPassword(password);
        // вставляем без id_enterprise — NULL
        String sql = "INSERT INTO users_roles_enterprises (login, password, id_role) VALUES (?, ?, 2)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, hashedPassword);
            return stmt.executeUpdate() > 0;
        }
    }

    //обновить логин и пароль пользователя
    public boolean updateUser(int userId, String newLogin, String newPassword) throws SQLException {
        String hashedPassword = BCryptUtil.hashPassword(newPassword);
        String sql = "UPDATE users_roles_enterprises SET login = ?, password = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newLogin);
            stmt.setString(2, hashedPassword);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    //все пользователи (для админа)
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id_user, u.login, u.password, u.id_enterprise, r.access_level " +
                     "FROM users_roles_enterprises u " +
                     "JOIN roles r ON u.id_role = r.id_role " +
                     "ORDER BY u.login";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int entId = rs.getInt("id_enterprise");
                Integer enterpriseId = rs.wasNull() ? null : entId;
                users.add(new User(
                    rs.getInt("id_user"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("access_level"),
                    enterpriseId
                ));
            }
        }
        return users;
    }

    //удалить пользователя
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users_roles_enterprises WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    //назначить пользователю предприятие
    public boolean assignEnterprise(int userId, int enterpriseId) throws SQLException {
        String sql = "UPDATE users_roles_enterprises SET id_enterprise = ? WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enterpriseId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    //проверка занят ли логин
    public boolean loginExists(String login) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users_roles_enterprises WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }
    //отвязать пользователя от предприятия (установить NULL).
    public boolean resetEnterprise(int userId) throws SQLException {
        String sql = "UPDATE users_roles_enterprises SET id_enterprise = NULL WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    //проверка существования пользователя по ID.
    public boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users_roles_enterprises WHERE id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

}
