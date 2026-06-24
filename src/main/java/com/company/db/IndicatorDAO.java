package com.company.db;

import com.company.model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//DAO для работы с показателями.
public class IndicatorDAO {

    private final Connection connection;

    public IndicatorDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public List<Indicator> getAllIndicators() throws SQLException {
        List<Indicator> list = new ArrayList<>();
        String sql = "SELECT i.id_indicator, i.name, ic.id_currency, c.type " +
                     "FROM indicators i " +
                     "LEFT JOIN indicator_currencies ic ON i.id_indicator = ic.id_indicator " +
                     "LEFT JOIN currencies c ON ic.id_currency = c.id_currency " +
                     "ORDER BY i.name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Indicator(
                    rs.getInt("id_indicator"),
                    rs.getString("name"),
                    rs.getInt("id_currency"),
                    rs.getString("type")
                ));
            }
        }
        return list;
    }

    public boolean addIndicator(String name, int currencyId) throws SQLException {
        String sql1 = "INSERT INTO indicators (name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int indicatorId = keys.getInt(1);
                String sql2 = "INSERT INTO indicator_currencies (id_indicator, id_currency) VALUES (?, ?)";
                try (PreparedStatement stmt2 = connection.prepareStatement(sql2)) {
                    stmt2.setInt(1, indicatorId);
                    stmt2.setInt(2, currencyId);
                    stmt2.executeUpdate();
                }
                return true;
            }
        }
        return false;
    }

    public boolean updateIndicator(int id, String name, int currencyId) throws SQLException {
        String sql1 = "UPDATE indicators SET name = ? WHERE id_indicator = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql1)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
        String sql2 = "UPDATE indicator_currencies SET id_currency = ? WHERE id_indicator = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql2)) {
            stmt.setInt(1, currencyId);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteIndicator(int id) throws SQLException {
        String sql = "DELETE FROM indicators WHERE id_indicator = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // значения показателей

    /**
     * для получения всех значений показателей для предприятия
     * таблица indicator_currencies_enterprise_periods
     * первичный составной ключ - [id_enterprise, id_period, id_indicator, id_currency]
     * */

    public List<IndicatorValue> getValuesByEnterprise(int enterpriseId) throws SQLException {
        List<IndicatorValue> list = new ArrayList<>();
        String sql = "SELECT f.id_enterprise, f.id_period, f.id_indicator, f.id_currency, f.value, " +
                     "i.name AS ind_name, p.quarter, c.type AS cur_type " +
                     "FROM indicator_currencies_enterprise_periods f " +
                     "JOIN indicators i  ON f.id_indicator = i.id_indicator " +
                     "JOIN periods p     ON f.id_period    = p.id_period " +
                     "JOIN currencies c  ON f.id_currency  = c.id_currency " +
                     "WHERE f.id_enterprise = ? " +
                     "ORDER BY p.quarter, i.name";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enterpriseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // В качестве "id" записи используем комбинацию полей через хэш
                // Для редактирования будем передавать составной ключ отдельно
                int compoundId = rs.getInt("id_enterprise") * 1000000
                               + rs.getInt("id_period") * 10000
                               + rs.getInt("id_indicator") * 100
                               + rs.getInt("id_currency");
                IndicatorValue iv = new IndicatorValue(
                    compoundId,
                    rs.getInt("id_indicator"),
                    rs.getString("ind_name"),
                    rs.getInt("id_period"),
                    rs.getString("quarter"),
                    rs.getBigDecimal("value"),
                    rs.getString("cur_type")
                );
                // Сохраняем отдельно для UPDATE
                iv.setEnterpriseId(enterpriseId);
                iv.setCurrencyId(rs.getInt("id_currency"));
                list.add(iv);
            }
        }
        return list;
    }


     //добавить новое значение показателя, которое пойдет в таблицу
     //indicator_currencies_enterprise_periods.

    public boolean addValue(int enterpriseId, int indicatorId, int periodId, BigDecimal value) throws SQLException {
        // 1.убедимся что связь enterprise-period существует
        String checkEP = "SELECT COUNT(*) FROM enterprise_periods WHERE id_enterprise=? AND id_period=?";
        try (PreparedStatement stmt = connection.prepareStatement(checkEP)) {
            stmt.setInt(1, enterpriseId);
            stmt.setInt(2, periodId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String insertEP = "INSERT INTO enterprise_periods (id_enterprise, id_period) VALUES (?, ?)";
                try (PreparedStatement s2 = connection.prepareStatement(insertEP)) {
                    s2.setInt(1, enterpriseId);
                    s2.setInt(2, periodId);
                    s2.executeUpdate();
                }
            }
        }

        // 2.получить валюту показателя
        int currencyId = getCurrencyForIndicator(indicatorId);

        // 3.вставить значение в таблицу фактов indicator_currencies_enterprise_periods.
        String sql = "INSERT INTO indicator_currencies_enterprise_periods " +
                     "(id_enterprise, id_period, id_indicator, id_currency, value) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enterpriseId);
            stmt.setInt(2, periodId);
            stmt.setInt(3, indicatorId);
            stmt.setInt(4, currencyId);
            stmt.setBigDecimal(5, value);
            return stmt.executeUpdate() > 0;
        }
    }


    //обновить значение показателя по составному ключу.
    public boolean updateValue(int enterpriseId, int periodId, int indicatorId, int currencyId,
                               BigDecimal value) throws SQLException {
        String sql = "UPDATE indicator_currencies_enterprise_periods SET value = ? " +
                     "WHERE id_enterprise=? AND id_period=? AND id_indicator=? AND id_currency=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, value);
            stmt.setInt(2, enterpriseId);
            stmt.setInt(3, periodId);
            stmt.setInt(4, indicatorId);
            stmt.setInt(5, currencyId);
            return stmt.executeUpdate() > 0;
        }
    }

    //удалить значение показателя по составному ключу.

    public boolean deleteValue(int enterpriseId, int periodId, int indicatorId, int currencyId)
            throws SQLException {
        String sql = "DELETE FROM indicator_currencies_enterprise_periods " +
                     "WHERE id_enterprise=? AND id_period=? AND id_indicator=? AND id_currency=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, enterpriseId);
            stmt.setInt(2, periodId);
            stmt.setInt(3, indicatorId);
            stmt.setInt(4, currencyId);
            return stmt.executeUpdate() > 0;
        }
    }

    private int getCurrencyForIndicator(int indicatorId) throws SQLException {
        String sql = "SELECT id_currency FROM indicator_currencies WHERE id_indicator = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, indicatorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id_currency");
        }
        throw new SQLException("Валюта для показателя не найдена");
    }

    // справочники

    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> list = new ArrayList<>();
        String sql = "SELECT * FROM currencies ORDER BY type";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                list.add(new Currency(rs.getInt("id_currency"), rs.getString("type")));
        }
        return list;
    }

    public List<Period> getAllPeriods() throws SQLException {
        List<Period> list = new ArrayList<>();
        String sql = "SELECT * FROM periods ORDER BY id_period";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                list.add(new Period(rs.getInt("id_period"), rs.getString("quarter")));
        }
        return list;
    }
}
