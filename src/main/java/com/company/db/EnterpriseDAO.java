package com.company.db;

import com.company.model.Enterprise;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//таблица четверки
public class EnterpriseDAO {

    private final Connection connection;

    public EnterpriseDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    //все предприятия (для админа)
    public List<Enterprise> getAllEnterprises() throws SQLException {
        List<Enterprise> list = new ArrayList<>();
        String sql = "SELECT * FROM enterprises ORDER BY name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }


     //предприятие конкретного пользователя.
     // в бд поле id_enterprise хранится в users_roles_enterprises.

    public List<Enterprise> getEnterprisesByUser(int userId) throws SQLException {
        List<Enterprise> list = new ArrayList<>();
        String sql = "SELECT e.* FROM enterprises e " +
                     "JOIN users_roles_enterprises u ON e.id_enterprise = u.id_enterprise " +
                     "WHERE u.id_user = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    //добавить предприятие
    public boolean addEnterprise(Enterprise e) throws SQLException {
        String sql = "INSERT INTO enterprises (name, details, phone_number, contact_person) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, e.getName());
            stmt.setString(2, e.getRequisites());
            stmt.setString(3, e.getPhone());
            stmt.setString(4, e.getContactPerson());
            return stmt.executeUpdate() > 0;
        }
    }

    //обновить предприятие.
    public boolean updateEnterprise(Enterprise e) throws SQLException {
        String sql = "UPDATE enterprises SET name=?, details=?, phone_number=?, contact_person=? WHERE id_enterprise=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, e.getName());
            stmt.setString(2, e.getRequisites());
            stmt.setString(3, e.getPhone());
            stmt.setString(4, e.getContactPerson());
            stmt.setInt(5, e.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    //удалить предприятие
    public boolean deleteEnterprise(int id) throws SQLException {
        String sql = "DELETE FROM enterprises WHERE id_enterprise = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Enterprise map(ResultSet rs) throws SQLException {
        return new Enterprise(
            rs.getInt("id_enterprise"),
            rs.getString("name"),
            rs.getString("details"),
            rs.getString("phone_number"),
            rs.getString("contact_person")
        );
    }
}
