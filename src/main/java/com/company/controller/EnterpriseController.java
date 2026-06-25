package com.company.controller;

import com.company.db.EnterpriseDAO;
import com.company.model.Enterprise;

import java.sql.SQLException;
import java.util.List;


 //controller для работы с предприятиями.

public class EnterpriseController {

    private EnterpriseDAO enterpriseDAO;

    public EnterpriseController() {
        try {
            this.enterpriseDAO = new EnterpriseDAO();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка подключения к БД: " + e.getMessage());
        }
    }

    public List<Enterprise> getAllEnterprises() throws Exception {
        return enterpriseDAO.getAllEnterprises();
    }

    public List<Enterprise> getEnterprisesByUser(int userId) throws Exception {
        return enterpriseDAO.getEnterprisesByUser(userId);
    }

    public void addEnterprise(Enterprise e) throws Exception {
        enterpriseDAO.addEnterprise(e);
    }

    public void updateEnterprise(Enterprise e) throws Exception {
        enterpriseDAO.updateEnterprise(e);
    }

    public void deleteEnterprise(int id) throws Exception {
        enterpriseDAO.deleteEnterprise(id);
    }
}
