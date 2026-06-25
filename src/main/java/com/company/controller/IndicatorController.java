package com.company.controller;

import com.company.db.EnterpriseDAO;
import com.company.db.IndicatorDAO;
import com.company.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class IndicatorController {

    private IndicatorDAO indicatorDAO;
    private EnterpriseDAO enterpriseDAO;

    public IndicatorController() {
        try {
            this.indicatorDAO = new IndicatorDAO();
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

    public List<Indicator> getAllIndicators() throws Exception {
        return indicatorDAO.getAllIndicators();
    }

    public List<Period> getAllPeriods() throws Exception {
        return indicatorDAO.getAllPeriods();
    }

    public List<Currency> getAllCurrencies() throws Exception {
        return indicatorDAO.getAllCurrencies();
    }

    public List<IndicatorValue> getValuesByEnterprise(int enterpriseId) throws Exception {
        return indicatorDAO.getValuesByEnterprise(enterpriseId);
    }

    // Добавить значение — теперь передаём enterpriseId тоже
    public void addValue(int enterpriseId, int indicatorId, int periodId, BigDecimal value) throws Exception {
        indicatorDAO.addValue(enterpriseId, indicatorId, periodId, value);
    }

    // Обновить по составному ключу
    public void updateValue(int enterpriseId, int periodId, int indicatorId,
                            int currencyId, BigDecimal value) throws Exception {
        indicatorDAO.updateValue(enterpriseId, periodId, indicatorId, currencyId, value);
    }

    // удалить по составному ключу
    public void deleteValue(int enterpriseId, int periodId, int indicatorId, int currencyId) throws Exception {
        indicatorDAO.deleteValue(enterpriseId, periodId, indicatorId, currencyId);
    }

    public void addIndicator(String name, int currencyId) throws Exception {
        indicatorDAO.addIndicator(name, currencyId);
    }

    public void updateIndicator(int id, String name, int currencyId) throws Exception {
        indicatorDAO.updateIndicator(id, name, currencyId);
    }

    public void deleteIndicator(int id) throws Exception {
        indicatorDAO.deleteIndicator(id);
    }
}
