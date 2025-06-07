package com.example.dao;

import com.example.model.Role;
import java.sql.SQLException;
import java.util.List;

public interface RoleDAO {
    // Додати нову роль у таблицю
    void addRole(Role role) throws SQLException;
    // Отримати роль за її id
    Role getRoleById(int id) throws SQLException;
    // Отримати всі наявні ролі
    List<Role> getAllRoles() throws SQLException;
    // Оновити існуючу роль (name, description)
    void updateRole(Role role) throws SQLException;
    // Видалити роль за id
    void deleteRole(int id) throws SQLException;
}