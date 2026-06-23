package com.company.model;

/**
 * Модель периода — аналог таблицы периоды.
 */
public class Period {
    private int id;
    private String quarter;

    public Period() {}

    public Period(int id, String quarter) {
        this.id = id;
        this.quarter = quarter;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getQuarter() { return quarter; }
    public void setQuarter(String quarter) { this.quarter = quarter; }

    @Override
    public String toString() {
        return quarter;
    }
}
