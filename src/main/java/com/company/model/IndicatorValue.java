package com.company.model;

import java.math.BigDecimal;

/**
 * Значение показателя за период.
 * В реальной БД первичный ключ составной:
 * (id_enterprise, id_period, id_indicator, id_currency)
 */
public class IndicatorValue {
    private int id; // технический id для таблицы JavaFX (не используется в SQL)
    private int indicatorId;
    private String indicatorName;
    private int periodId;
    private String quarter;
    private BigDecimal value;
    private String currencyType;

    // Составной ключ для UPDATE/DELETE
    private int enterpriseId;
    private int currencyId;

    public IndicatorValue() {}

    public IndicatorValue(int id, int indicatorId, String indicatorName,
                          int periodId, String quarter, BigDecimal value, String currencyType) {
        this.id = id;
        this.indicatorId = indicatorId;
        this.indicatorName = indicatorName;
        this.periodId = periodId;
        this.quarter = quarter;
        this.value = value;
        this.currencyType = currencyType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIndicatorId() { return indicatorId; }
    public void setIndicatorId(int indicatorId) { this.indicatorId = indicatorId; }
    public String getIndicatorName() { return indicatorName; }
    public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }
    public int getPeriodId() { return periodId; }
    public void setPeriodId(int periodId) { this.periodId = periodId; }
    public String getQuarter() { return quarter; }
    public void setQuarter(String quarter) { this.quarter = quarter; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
    public String getCurrencyType() { return currencyType; }
    public void setCurrencyType(String currencyType) { this.currencyType = currencyType; }
    public int getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(int enterpriseId) { this.enterpriseId = enterpriseId; }
    public int getCurrencyId() { return currencyId; }
    public void setCurrencyId(int currencyId) { this.currencyId = currencyId; }
}
