package com.company.model;

public class User {
    private int id;
    private String login;
    private String password;
    private String role;
    private Integer enterpriseId; // id предприятия из users_roles_enterprises

    public User() {}

    public User(int id, String login, String password, String role, Integer enterpriseId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.enterpriseId = enterpriseId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getEnterpriseId() { return enterpriseId; }
    public void setEnterpriseId(Integer enterpriseId) { this.enterpriseId = enterpriseId; }

    public boolean isAdmin() { return "admin".equalsIgnoreCase(role); }

    @Override
    public String toString() { return login + " (" + role + ")"; }
}
