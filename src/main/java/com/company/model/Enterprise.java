package com.company.model;

/**
 * Модель предприятия — аналог таблицы предприятия.
 */
public class Enterprise {
    private int id;
    private String name;
    private String requisites;
    private String phone;
    private String contactPerson;

    public Enterprise() {}

    public Enterprise(int id, String name, String requisites, String phone, String contactPerson) {
        this.id = id;
        this.name = name;
        this.requisites = requisites;
        this.phone = phone;
        this.contactPerson = contactPerson;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRequisites() { return requisites; }
    public void setRequisites(String requisites) { this.requisites = requisites; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    @Override
    public String toString() {
        return name;
    }
}
