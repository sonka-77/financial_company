package com.company.model;

public class Indicator {
    private int id;
    private String name;
    private int currencyId;
    private String currencyType; // вид валюты (для отображения)

    public Indicator() {}

    public Indicator(int id, String name, int currencyId, String currencyType) {
        this.id = id;
        this.name = name;
        this.currencyId = currencyId;
        this.currencyType = currencyType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCurrencyId() { return currencyId; }
    public void setCurrencyId(int currencyId) { this.currencyId = currencyId; }

    public String getCurrencyType() { return currencyType; }
    public void setCurrencyType(String currencyType) { this.currencyType = currencyType; }

    @Override
    public String toString() {
        return name + " (" + currencyType + ")";
    }
}
